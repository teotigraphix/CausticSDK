
package com.teotigraphix.caustk.system.bank;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teotigraphix.caustic.internal.utils.PatternUtils;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.osc.OutputPanelMessage;
import com.teotigraphix.caustic.osc.PatternSequencerMessage;
import com.teotigraphix.caustic.osc.RackMessage;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.tone.ToneDescriptor;

/**
 * The {@link MemoryLoader} will load a .caustic file from disk load it into
 * memory unserializing all machine patterns into banks of presets.
 * <p>
 * Future versions could actually load new banks from the server at runtime, but
 * the user could not be in the middle of writing a song, the rack needs to be
 * reinitialized during this type of loading operations.
 */
public class MemoryLoader {
    private Map<Integer, MemoryDescriptor> map = new HashMap<Integer, MemoryDescriptor>();

    private ICaustkController controller;

    public ICaustkController getController() {
        return controller;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MemoryLoader(ICaustkController controller) {
        this.controller = controller;
    }

    /**
     * Returns the {@link MemoryDescriptor} at the specified index.
     * 
     * @param index The index of the descriptor.
     */
    public MemoryDescriptor getDescriptor(int index) {
        return map.get(index);
    }

    /**
     * Loads a <code>.caustic</code> file from disk using the information
     * provided in the {@link MemoryDescriptor}.
     * <p>
     * This method will populate the descriptor's public API with data
     * unserialized from the <code>.caustic</code> song file.
     * 
     * @param descriptor The {@link MemoryDescriptor} used to load the bank.
     */
    public void load(MemoryDescriptor descriptor, List<ToneDescriptor> tones) {
        // if the file is not null, this descriptor has not been created
        // the patterns, phrases and patches are not in the collections, load it
        if (descriptor instanceof CausticFileMemoryDescriptor) {
            loadDescriptor((CausticFileMemoryDescriptor)descriptor, tones);
        }

        addDescriptor(descriptor);
    }

    private void loadDescriptor(CausticFileMemoryDescriptor descriptor, List<ToneDescriptor> tones) {
        File file = descriptor.getFile();
        if (!file.exists())
            throw new IllegalArgumentException("MemoryBank not found; " + file.getPath());

        // clear the rack of any data
        // XXX Should I do this here? seems out of place
        RackMessage.BLANKRACK.send(controller);

        // load the file into the rack.
        RackMessage.LOAD_SONG.send(controller, descriptor.getFile().getPath());

        // XXX Can I do better than this?
        if (tones == null) {
            tones = new ArrayList<ToneDescriptor>();
            // pre load the descriptors
            for (int i = 0; i < 6; i++) {
                String name = RackMessage.QUERY_MACHINE_NAME.queryString(controller, i);
                String type = RackMessage.QUERY_MACHINE_TYPE.queryString(controller, i);
                if (name != null && !name.isEmpty() && type != null && !type.isEmpty()) {
                    tones.add(new ToneDescriptor(i, name, MachineType.fromString(type)));
                }
            }
        }

        List<PatternItem> patterns = new ArrayList<PatternItem>();
        List<PhraseItem> phrases = new ArrayList<PhraseItem>();
        List<PatchItem> patches = new ArrayList<PatchItem>();

        //  patternIndex[0-63]  [PatternData][PhraseData]
        // each pattern is the 'same' pattern in each machine
        // for each tone. All the machine patterns get added to
        // the main Pattern as phrases, total possible phrases for
        // a Pattern is 6, 2 phrases each for the BL1
        Map<String, PatternItem> map = new HashMap<String, PatternItem>();

        // now we loop through all patterns
        for (ToneDescriptor toneDescriptor : tones) {
            int toneIndex = toneDescriptor.getIndex();

            String name = RackMessage.QUERY_MACHINE_NAME.queryString(controller, toneIndex);
            String type = RackMessage.QUERY_MACHINE_TYPE.queryString(controller, toneIndex);
            MachineType machineType = MachineType.fromString(type);

            if (toneDescriptor.getMachineType() != machineType)
                throw new RuntimeException("Tone MachineType not equal to loaded tone");

            //------------------------------------------------------------------
            // Load Patch
            //------------------------------------------------------------------

            // create the Patch for the Tone preset
            PatchItem item = createPatch(name, toneIndex, machineType);
            patches.add(item);

            // get the patterns
            String result = PatternSequencerMessage.QUERY_PATTERNS_WITH_DATA.queryString(
                    controller, toneIndex);

            for (String patternName : result.split(" ")) {
                int bankIndex = PatternUtils.toBank(patternName);
                int patternIndex = PatternUtils.toPattern(patternName);

                // set the current bank and pattern of the machine to query
                // the string pattern data
                PatternSequencerMessage.BANK.send(controller, toneIndex, bankIndex);
                PatternSequencerMessage.PATTERN.send(controller, toneIndex, patternIndex);

                //----------------------------------------------------------------
                // Load Pattern
                //----------------------------------------------------------------

                // add the list of tone patterns to the map
                PatternItem pattern = findPattern(map, patternName, toneIndex);
                if (!patterns.contains(pattern))
                    patterns.add(pattern);

                //----------------------------------------------------------------
                // Load Phrase
                //----------------------------------------------------------------

                // save pattern properties
                PhraseItem phraseItem = createPhrase(pattern, toneIndex, patches.get(toneIndex));

                phrases.add(phraseItem);

                pattern.addPhraseItem(phraseItem);
            }
        }

        descriptor.setPatternItems(patterns);
        descriptor.setPhraseItems(phrases);
        descriptor.setPatchItems(patches);
    }

    private PatchItem createPatch(String name, int toneIndex, MachineType machineType) {
        PatchItem item = new PatchItem(name);
        item.loadData(controller, toneIndex, machineType);
        return item;
    }

    private PatternItem findPattern(Map<String, PatternItem> map, String patternName, int toneIndex) {
        // add the list of tone patterns to the map
        PatternItem pattern = map.get(patternName);
        if (pattern == null) {
            int length = (int)PatternSequencerMessage.NUM_MEASURES.query(controller, toneIndex);
            int tempo = (int)OutputPanelMessage.BPM.query(controller);

            pattern = new PatternItem(patternName, length, tempo);

            map.put(patternName, pattern);
        }
        return pattern;
    }

    private PhraseItem createPhrase(PatternItem pattern, int toneIndex, PatchItem patch) {
        // return the pattern data as a string
        String data = PatternSequencerMessage.QUERY_NOTE_DATA.queryString(controller, toneIndex);

        PhraseItem phraseItem = new PhraseItem(data, pattern.getLength(), patch);

        return phraseItem;
    }

    private void addDescriptor(MemoryDescriptor descriptor) {
        descriptor.setIndex(map.size());
        map.put(descriptor.getIndex(), descriptor);
    }

}
