
package com.teotigraphix.caustk.library;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.desktop.RuntimeUtils;
import com.teotigraphix.caustic.internal.rack.Rack;
import com.teotigraphix.caustic.internal.utils.PatternUtils;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.OutputPanelMessage;
import com.teotigraphix.caustic.osc.PatternSequencerMessage;
import com.teotigraphix.caustic.osc.RackMessage;
import com.teotigraphix.caustic.sequencer.IStepPhrase.Resolution;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.library.vo.EffectRackInfo;
import com.teotigraphix.caustk.library.vo.MixerPanelInfo;
import com.teotigraphix.caustk.library.vo.RackInfo;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class LibraryManager implements ILibraryManager {

    private XStream xStream;

    private LibraryRegistry registry;

    private ICaustkController controller;

    private File librariesDirectory;

    @Override
    public File getLibrariesDirectory() {
        return librariesDirectory;
    }

    @Override
    public int getLibraryCount() {
        return registry.getLibraries().size();
    }

    @Override
    public Collection<Library> getLibraries() {
        return registry.getLibraries();
    }

    public LibraryManager(ICaustkController controller) {
        this.controller = controller;

        File root = controller.getConfiguration().getApplicationRoot();
        if (!root.exists())
            throw new RuntimeException("Application root not specified");

        librariesDirectory = new File(root, "libraries");
        if (!librariesDirectory.exists())
            librariesDirectory.mkdirs();

        registry = new LibraryRegistry(librariesDirectory);

        xStream = new XStream(new JettisonMappedXmlDriver());
        xStream.setMode(XStream.NO_REFERENCES);
    }

    /**
     * Saves the {@link Library}s to disk using the libraries directory
     * location.
     * 
     * @throws IOException
     */
    @Override
    public void save() throws IOException {
        for (Library library : registry.getLibraries()) {
            saveLibrary(library);
        }
    }

    /**
     * Loads the entire <code>libraries</code> directory into the manager.
     * <p>
     * Each sub directory located within the <code>libraries</code> directory
     * will be created as a {@link Library} instance.
     */
    @Override
    public void load() {
        Collection<File> dirs = FileUtils.listFilesAndDirs(librariesDirectory, new IOFileFilter() {
            @Override
            public boolean accept(File arg0, String arg1) {
                return false;
            }

            @Override
            public boolean accept(File arg0) {
                return false;
            }
        }, new IOFileFilter() {
            @Override
            public boolean accept(File file, String name) {
                if (file.getParentFile().getName().equals("libraries"))
                    return true;
                return false;
            }

            @Override
            public boolean accept(File file) {
                if (file.getParentFile().getName().equals("libraries"))
                    return true;
                return false;
            }
        });

        for (File directory : dirs) {
            if (directory.equals(librariesDirectory))
                continue;

            loadLibrary(directory.getName());
        }
    }

    @Override
    public void importSong(Library library, File causticFile) throws IOException {
        //----------------------------------------------------------------------
        // clear the core rack
        RackMessage.BLANKRACK.send(controller);

        Rack rack = new Rack(controller.getFactory(), true);
        rack.setEngine(controller);

        // load the file into the rack.
        try {
            rack.loadSong(causticFile.getAbsolutePath());
        } catch (CausticException e) {
            e.printStackTrace();
        }

        // restore the rack
        //rack.restore();
        rack.getOutputPanel().restore();
        rack.getMixerPanel().restore();
        rack.getEffectsRack().restore();

        loadLibraryScene(library, causticFile, rack);
        loadLibraryPhrases(library, rack);
        loadLibraryPatches(library, rack);

        // clear the core rack
        RackMessage.BLANKRACK.send(controller);
    }

    @Override
    public Library createLibrary(String name) throws IOException {
        File newDirectory = new File(librariesDirectory, name);
        //if (newDirectory.exists())
        //    throw new CausticException("Library already exists " + newDirectory.getAbsolutePath());
        newDirectory.mkdir();

        Library library = new Library();
        library.setId(UUID.randomUUID());
        library.setDirectory(newDirectory);
        library.mkdirs();

        registry.addLibrary(library);

        return library;
    }

    private void loadLibraryPatches(Library library, Rack rack) throws IOException {
        for (int i = 0; i < 6; i++) {
            IMachine machine = rack.getMachine(i);
            if (machine != null) {
                LibraryPatch patch = new LibraryPatch();
                patch.setId(UUID.randomUUID());
                relocatePresetFile(machine, library, patch);
                library.addPatch(patch);
            }
        }
    }

    private void loadLibraryScene(Library library, File causticFile, Rack rack) {
        LibraryScene libraryScene = new LibraryScene();

        // 8ff156ff36376d2517cef39cdd43876a-PULSAR.caustic
        //String id = LibrarySerializerUtils.toMD5String(causticFile, causticFile.getName());
        libraryScene.setId(UUID.randomUUID());
        library.addScene(libraryScene);

        RackInfo rackInfo = LibrarySerializerUtils.createRackInfo(rack);
        libraryScene.setRackInfo(rackInfo);

        MixerPanelInfo mixerPanelInfo = LibrarySerializerUtils.createMixerPanelInfo(rack);
        libraryScene.setMixerInfo(mixerPanelInfo);

        EffectRackInfo effectRackInfo = LibrarySerializerUtils.createEffectRackInfo(rack);
        libraryScene.setEffectRackInfo(effectRackInfo);
    }

    private void loadLibraryPhrases(Library library, Rack rack) {
        for (int i = 0; i < 6; i++) {
            IMachine machine = rack.getMachine(i);
            if (machine != null) {

                String result = PatternSequencerMessage.QUERY_PATTERNS_WITH_DATA.queryString(
                        controller, i);

                if (result == null)
                    continue;

                for (String patternName : result.split(" ")) {
                    int bankIndex = PatternUtils.toBank(patternName);
                    int patternIndex = PatternUtils.toPattern(patternName);

                    // set the current bank and pattern of the machine to query
                    // the string pattern data
                    PatternSequencerMessage.BANK.send(controller, i, bankIndex);
                    PatternSequencerMessage.PATTERN.send(controller, i, patternIndex);

                    //----------------------------------------------------------------
                    // Load Pattern
                    //----------------------------------------------------------------

                    // load one phrase per pattern; load ALL patterns
                    // as caustic machine patterns
                    int length = (int)PatternSequencerMessage.NUM_MEASURES.query(controller, i);
                    float tempo = OutputPanelMessage.BPM.query(controller);
                    String noteData = PatternSequencerMessage.QUERY_NOTE_DATA.queryString(
                            controller, i);

                    LibraryPhrase phrase = new LibraryPhrase();
                    phrase.setId(UUID.randomUUID());
                    phrase.setLength(length);
                    phrase.setTempo(tempo);
                    phrase.setMachineType(machine.getType());
                    phrase.setNoteData(noteData);
                    phrase.setResolution(calculateResolution(noteData));
                    library.addPhrase(phrase);
                }
            }
        }
    }

    private Resolution calculateResolution(String data) {
        // TODO This is totally inefficient, needs to be lazy loaded
        // push the notes into the machines sequencer
        float smallestGate = 1f;
        String[] notes = data.split("\\|");
        for (String noteData : notes) {
            String[] split = noteData.split(" ");

            float start = Float.parseFloat(split[0]);
            float end = Float.parseFloat(split[3]);
            float gate = end - start;
            smallestGate = Math.min(smallestGate, gate);
        }

        Resolution result = Resolution.SIXTEENTH;
        if (smallestGate <= Resolution.SIXTYFOURTH.getValue() * 4)
            result = Resolution.SIXTYFOURTH;
        else if (smallestGate <= Resolution.THIRTYSECOND.getValue() * 4)
            result = Resolution.THIRTYSECOND;
        else if (smallestGate <= Resolution.SIXTEENTH.getValue() * 4)
            result = Resolution.SIXTEENTH;

        return result;
    }

    protected void relocatePresetFile(IMachine machine, Library library, LibraryPatch patch)
            throws IOException {
        String id = patch.getId().toString();
        machine.savePreset(id);
        File presetFile = RuntimeUtils.getCausticPresetsFile(machine.getType().toString(), id);
        if (!presetFile.exists())
            throw new IOException("Preset file does not exist");

        File presetsDirectory = library.getPresetsDirectory();
        File destFile = new File(presetsDirectory, presetFile.getName());
        FileUtils.copyFile(presetFile, destFile);
        FileUtils.deleteQuietly(presetFile);
    }

    @Override
    public void saveLibrary(Library library) throws IOException {
        String data = xStream.toXML(library);
        File file = new File(library.getDirectory(), "library.ctk");
        FileUtils.writeStringToFile(file, data);
    }

    @Override
    public Library loadLibrary(String name) {
        File directory = new File(librariesDirectory, name);
        File file = new File(directory, "library.ctk");

        xStream = new XStream(new JettisonMappedXmlDriver());
        xStream.setMode(XStream.NO_REFERENCES);

        Library library = (Library)xStream.fromXML(file);
        registry.addLibrary(library);
        return library;
    }

    @Override
    public void clear() {
        registry = new LibraryRegistry(librariesDirectory);
    }

    @Override
    public void delete() throws IOException {
        for (Library library : registry.getLibraries()) {
            library.delete();
        }
        clear();
    }

}

/*


<?xml version="1.0" encoding="UTF-8"?>
<mixer>
    <master eq_bass="0.19565237" eq_high="0.100000024" eq_mid="0.0" id="-1" index="-1" volume="1.1478264"/>
    <channels>
        <channel delay_send="0.504348" eq_bass="-0.008696437" eq_high="0.06956482" eq_mid="1.1920929E-7" id="0" index="0" mute="0" pan="0.0" reverb_send="0.05869567" solo="0" stereo_width="0.99609375" volume="1.2782611"/>
        <channel delay_send="0.5000001" eq_bass="-1.0" eq_high="0.02666676" eq_mid="0.034783125" id="1" index="1" mute="0" pan="0.0" reverb_send="0.0" solo="0" stereo_width="0.0" volume="1.3652176"/>
        <channel delay_send="0.0" eq_bass="1.0" eq_high="-0.008695722" eq_mid="0.0" id="2" index="2" mute="0" pan="0.0" reverb_send="0.0" solo="0" stereo_width="0.0" volume="1.2782608"/>
        <channel delay_send="0.52173936" eq_bass="-1.0" eq_high="-2.9802322E-7" eq_mid="-0.026086628" id="3" index="3" mute="0" pan="0.18260896" reverb_send="0.07173912" solo="0" stereo_width="0.0" volume="1.4383385"/>
        <channel delay_send="0.0" eq_bass="-0.017391145" eq_high="0.38260782" eq_mid="0.0" id="4" index="4" mute="0" pan="0.0" reverb_send="0.07391316" solo="0" stereo_width="0.53269273" volume="1.3913045"/>
        <channel delay_send="0.0" eq_bass="1.0" eq_high="5.9604645E-7" eq_mid="-0.008694708" id="5" index="5" mute="0" pan="0.0" reverb_send="0.0" solo="0" stereo_width="0.99609375" volume="0.99999934"/>
    </channels>
    <delay feedback="0.7565215" stereo="1" time="5"/>
    <reverb damping="0.0" room="0.9" stereo="1"/>
</mixer>


<?xml version="1.0" encoding="UTF-8"?>
<rack>
    <machine active="1" id="LOWLEAD" index="0" type="subsynth"/>
    <machine active="1" id="HIGHLEAD" index="1" type="subsynth"/>
    <machine active="1" id="BASS" index="2" type="subsynth"/>
    <machine active="1" id="MELODY" index="3" type="bassline"/>
    <machine active="1" id="STRINGS" index="4" type="pcmsynth"/>
    <machine active="1" id="DRUMSIES" index="5" type="beatbox"/>
</rack>


<?xml version="1.0" encoding="UTF-8"?>
<effects>
    <channel id="LOWLEAD" index="0">
        <effect channel="0" depth="0.95" feedback="0.25" rate="0.43333432" type="5" wet="1.0"/>
    </channel>
    <channel id="HIGHLEAD" index="1"/>
    <channel id="BASS" index="2">
        <effect attack="0.011313666" channel="2" ratio="1.0" release="0.098769695" sidechain="5" threshold="0.19565232" type="3"/>
    </channel>
    <channel id="MELODY" index="3">
        <effect channel="3" depth="1" jitter="1.0" rate="0.31891286" type="4" wet="1.0"/>
        <effect attack="0.005227146" channel="3" ratio="0.7956521" release="0.105373904" sidechain="5" threshold="0.58260775" type="3"/>
    </channel>
    <channel id="STRINGS" index="4">
        <effect channel="4" depth="0.95" feedback="0.25" rate="0.05869563" type="5" wet="1.0"/>
        <effect attack="0.066963136" channel="4" ratio="1.0" release="0.03214792" sidechain="5" threshold="0.0" type="3"/>
    </channel>
    <channel id="DRUMSIES" index="5"/>
</effects>


*/

