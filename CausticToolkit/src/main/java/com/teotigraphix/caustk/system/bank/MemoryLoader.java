////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.system.bank;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.internal.effect.EffectsRack;
import com.teotigraphix.caustic.internal.machine.Beatbox;
import com.teotigraphix.caustic.internal.machine.Machine;
import com.teotigraphix.caustic.internal.machine.PCMSynth;
import com.teotigraphix.caustic.internal.mixer.MixerDelay;
import com.teotigraphix.caustic.internal.mixer.MixerPanel;
import com.teotigraphix.caustic.internal.mixer.MixerReverb;
import com.teotigraphix.caustic.internal.utils.PatternUtils;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.mixer.MixerEffectType;
import com.teotigraphix.caustic.osc.OutputPanelMessage;
import com.teotigraphix.caustic.osc.PatternSequencerMessage;
import com.teotigraphix.caustic.osc.RackMessage;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.SubSynthTone;
import com.teotigraphix.caustk.tone.ToneDescriptor;
import com.teotigraphix.caustk.tone.ToneType;

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

    private OnPatchCopy onPatchCopyListener;

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
     * @throws IOException
     */
    public void load(MemoryDescriptor descriptor, List<ToneDescriptor> tones) throws IOException {
        // if the file is not null, this descriptor has not been created
        // the patterns, phrases and patches are not in the collections, load it
        if (descriptor instanceof CausticFileMemoryDescriptor) {
            loadDescriptor((CausticFileMemoryDescriptor)descriptor, tones);
        }

        addDescriptor(descriptor);
    }

    private void loadDescriptor(CausticFileMemoryDescriptor descriptor, List<ToneDescriptor> tones)
            throws IOException {
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
                    tones.add(new ToneDescriptor(i, name, ToneType.fromString(type)));
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
            ToneType toneType = ToneType.fromString(type);

            if (toneDescriptor.getToneType() != toneType)
                throw new RuntimeException("Tone ToneType not equal to loaded tone");

            //------------------------------------------------------------------
            // Load Patch
            //------------------------------------------------------------------

            // create the Patch for the Tone preset
            PatchItem item = createPatch(name, toneIndex, toneType);
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

                PatternItem pattern = null;

                //----------------------------------------------------------------
                // Load Pattern
                //----------------------------------------------------------------

                if (descriptor.fullLoad) {
                    // load one phrase per pattern; load ALL patterns
                    // as caustic machine patterns
                    int length = (int)PatternSequencerMessage.NUM_MEASURES.query(controller,
                            toneIndex);
                    int tempo = (int)OutputPanelMessage.BPM.query(controller);

                    pattern = new PatternItem(patternName, length, tempo);
                    pattern.setIndex(toneIndex);
                    patterns.add(pattern);

                } else {

                    // add the list of tone patterns to the map
                    pattern = findPattern(map, patternName, toneIndex);
                    if (!patterns.contains(pattern))
                        patterns.add(pattern);

                }

                //----------------------------------------------------------------
                // Load Phrase
                //----------------------------------------------------------------

                // save pattern properties
                PhraseItem phraseItem = createPhrase(pattern, toneIndex, patches.get(toneIndex));
                phrases.add(phraseItem);
                pattern.addPhraseItem(phraseItem);
            }
        }

        descriptor.setToneDescriptors(tones);
        descriptor.setPatternItems(patterns);
        descriptor.setPhraseItems(phrases);
        descriptor.setPatchItems(patches);
    }

    private PatchItem createPatch(String name, int toneIndex, ToneType machineType)
            throws IOException {
        String id = UUID.randomUUID().toString();
        PatchItem item = new PatchItem(name, id);
        item.setIndex(toneIndex);
        IMemento memento = copyData(toneIndex, machineType, id, name);
        item.setMemento(memento);
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
        phraseItem.setIndex(toneIndex);

        return phraseItem;
    }

    private void addDescriptor(MemoryDescriptor descriptor) {
        descriptor.setIndex(map.size());
        map.put(descriptor.getIndex(), descriptor);
    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    private Machine createMachine(int machineIndex, MachineType machineType, String id, String name) {
        Machine machine = null;
        //        switch (machineType) {
        //            case BASSLINE:
        //                machine = new Bassline(id);
        //                machine.setType(MachineType.BASSLINE);
        //                break;
        //            case BEATBOX:
        //                machine = new Beatbox(id);
        //                machine.setType(MachineType.BEATBOX);
        //                break;
        //            case PCMSYNTH:
        //                machine = new PCMSynth(id);
        //                machine.setType(MachineType.PCMSYNTH);
        //                break;
        //            case SUBSYNTH:
        //                machine = new SubSynth(id);
        //                machine.setType(MachineType.SUBSYNTH);
        //                break;
        //        }
        //        machine.setName(name);
        //        machine.setFactory(controller.getFactory());
        //        machine.setEngine(controller);
        //        machine.setIndex(machineIndex);
        return machine;
    }

    public IMemento copyData(int machineIndex, ToneType machineType, String id, String name)
            throws IOException {
        throw new RuntimeException("FIX SINCE YOU MESSED IT UP");
        //        Machine machine = createMachine(machineIndex, machineType, id, name);
        //
        //        IMemento memento = XMLMemento.createWriteRoot("patch");
        //
        //        switch (machineType) {
        //            case BASSLINE:
        //                copyBassline(controller, (Bassline)machine, memento);
        //                break;
        //            case BEATBOX:
        //                copyBeatbox(controller, (Beatbox)machine, memento);
        //                break;
        //            case PCMSYNTH:
        //                copyPCMSynth(controller, (PCMSynth)machine, memento);
        //                break;
        //            case SUBSYNTH:
        //                copySubSynth(controller, (SubSynth)machine, memento);
        //                break;
        //        }
        //
        //        copyMixerChannel(controller, machine, memento);
        //        copyEffectChannel(controller, machine, memento);
        //
        //        machine.copy(memento);
        //
        //        if (onPatchCopyListener != null)
        //            onPatchCopyListener.onPatchCopy(machine);
        //
        //        machine.setEngine(null);
        //
        //        return memento;
    }

    void copyBassline(ICaustkController controller, BasslineTone tone, IMemento memento) {
        //machine.getSequencer().restore();
        tone.getOsc1().restore();
        tone.getVolume().restore();
        tone.getFilter().restore();
        tone.getLFO1().restore();
        tone.getDistortion().restore();
    }

    void copySubSynth(ICaustkController controller, SubSynthTone tone, IMemento memento) {
        //machine.getSequencer().restore();
        tone.getFilter().restore();
        tone.getLFO1().restore();
        tone.getLFO2().restore();
        tone.getOsc1().restore();
        tone.getOsc2().restore();
        tone.getVolume().restore();
    }

    void copyPCMSynth(ICaustkController controller, PCMSynth machine, IMemento memento) {
        //machine.getSequencer().restore();
        machine.getFilter().restore();
        machine.getLFO1().restore();
        machine.getPitch().restore();
        machine.getSampler().restore();
        machine.getVolume().restore();
    }

    void copyBeatbox(ICaustkController controller, Beatbox machine, IMemento memento) {
        //machine.getSequencer().restore();
        machine.getSampler().restore();
        machine.getVolume().restore();
    }

    private void copyMixerChannel(ICaustkController controller, Machine machine, IMemento memento) {
        MixerPanel panel = (MixerPanel)controller.getFactory().createMixerPanel();
        panel.setEngine(controller);
        panel.addMachine(machine);

        MixerDelay delay = (MixerDelay)controller.getFactory().createMixerEffect(panel,
                MixerEffectType.DELAY);
        MixerReverb reverb = (MixerReverb)controller.getFactory().createMixerEffect(panel,
                MixerEffectType.REVERB);

        delay.setDevice(panel);
        reverb.setDevice(panel);

        panel.setDelay(delay);
        panel.setReverb(reverb);

        panel.restore();
        panel.copyChannel(machine, memento);
    }

    private void copyEffectChannel(ICaustkController controller, Machine machine, IMemento memento) {
        EffectsRack effectsRack = (EffectsRack)controller.getFactory().createEffectRack();
        effectsRack.setEngine(controller);
        effectsRack.addMachine(machine);
        effectsRack.restore();
        effectsRack.copyChannel(machine, memento);
    }

    public void setOnPatchCopyListener(OnPatchCopy l) {
        onPatchCopyListener = l;
    }

    public interface OnPatchCopy {
        void onPatchCopy(IMachine machine);
    }
}
