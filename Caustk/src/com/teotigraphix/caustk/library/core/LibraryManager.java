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

package com.teotigraphix.caustk.library.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IControllerAware;
import com.teotigraphix.caustk.controller.core.ControllerComponent;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.OutputPanelMessage;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.library.ILibraryManager;
import com.teotigraphix.caustk.library.item.LibraryPatch;
import com.teotigraphix.caustk.library.item.LibraryPattern;
import com.teotigraphix.caustk.library.item.LibraryPattern.ToneSet;
import com.teotigraphix.caustk.library.item.LibraryPhrase;
import com.teotigraphix.caustk.library.item.LibraryScene;
import com.teotigraphix.caustk.library.item.SoundSourceDescriptor;
import com.teotigraphix.caustk.rack.sound.ISoundSource;
import com.teotigraphix.caustk.rack.tone.Tone;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent.Resolution;
import com.teotigraphix.caustk.rack.tone.components.SynthComponent;
import com.teotigraphix.caustk.utils.Compress;
import com.teotigraphix.caustk.utils.PatternUtils;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public class LibraryManager extends ControllerComponent implements ILibraryManager,
        IControllerAware {

    private static final String LIBRARY_CTKL = "library.ctkl";

    //--------------------------------------------------------------------------
    // API
    //--------------------------------------------------------------------------

    //----------------------------------
    // libraries
    //----------------------------------

    private transient Map<UUID, Library> libraries = new TreeMap<UUID, Library>();

    Map<UUID, Library> getLibraries() {
        return libraries;
    }

    //----------------------------------
    // selectedLibrary
    //----------------------------------

    private Library selectedLibrary;

    @Override
    public Library getSelectedLibrary() {
        return selectedLibrary;
    }

    @Override
    public void setSelectedLibrary(Library value) {
        selectedLibrary = value;
        getController().trigger(new OnLibraryManagerSelectedLibraryChange(value));
    }

    public LibraryManager() {
        super(); // creates dispatcher
    }

    @Override
    public void onAttach(ICaustkController controller) {
        setController(controller);
    }

    @Override
    public void onDetach() {
    }

    /**
     * Loads the entire <code>libraries</code> directory into the manager.
     * <p>
     * Each sub directory located within the <code>libraries</code> directory
     * will be created as a {@link Library} instance.
     */
    //@Override
    public void load() {

        //        File librariesDirectory = null;
        //
        //        if (!librariesDirectory.exists())
        //            return;
        //
        //        Collection<File> dirs = FileUtils.listFilesAndDirs(librariesDirectory, new IOFileFilter() {
        //            @Override
        //            public boolean accept(File arg0, String arg1) {
        //                return false;
        //            }
        //
        //            @Override
        //            public boolean accept(File arg0) {
        //                return false;
        //            }
        //        }, new IOFileFilter() {
        //            @Override
        //            public boolean accept(File file, String name) {
        //                if (file.getParentFile().getName().equals("libraries"))
        //                    return true;
        //                return false;
        //            }
        //
        //            @Override
        //            public boolean accept(File file) {
        //                if (file.getParentFile().getName().equals("libraries"))
        //                    return true;
        //                return false;
        //            }
        //        });
        //
        //        for (File directory : dirs) {
        //            if (directory.equals(librariesDirectory))
        //                continue;
        //
        //            //            loadLibrary(directory.getName());
        //        }
    }

    @Override
    public Library createLibrary() {
        Library library = new Library();
        library.wakeup(getController());
        library.setId(UUID.randomUUID());
        library.setMetadataInfo(new MetadataInfo());
        getLibraries().put(library.getId(), library);

        return library;
    }

    @Override
    public Library createLibrary(String relativePath) throws IOException {
        return createLibrary(new File(relativePath));
    }

    @Override
    public Library createLibrary(File directory) throws IOException {
        Library library = new Library();
        library.wakeup(getController());
        library.setId(UUID.randomUUID());
        library.setMetadataInfo(new MetadataInfo());
        // Relative directory IN the libraries folder
        library.setDirectory(new File("libraries", directory.getPath()));
        library.mkdirs();

        getLibraries().put(library.getId(), library);

        saveLibrary(library);

        return library;
    }

    @Override
    public Library loadLibrary(String relativePath) throws IOException {
        return loadLibrary(new File(relativePath));
    }

    @Override
    public Library loadLibrary(File directory) throws IOException {
        File absoluteLocation = getController().getProjectManager().getProject()
                .getAbsoluteResource(new File("libraries", directory.getPath()).getPath());

        getController().getLogger().log("LibraryManager", "Load library; " + absoluteLocation);
        if (!absoluteLocation.exists()) {
            getController().getLogger().err("LibraryManager", "Library not found; " + directory);
            return null;
        }

        File file = new File(absoluteLocation, LIBRARY_CTKL);
        Library library = getController().getSerializeService().fromFile(file, Library.class);
        getLibraries().put(library.getId(), library);

        //getController().getDispatcher().trigger(new OnLibraryManagerLoadComplete(library));

        return library;
    }

    @Override
    public void save() throws IOException {
        saveLibrary(getSelectedLibrary());
    }

    @Override
    public void saveLibrary(Library library) throws IOException {
        String data = getController().getSerializeService().toString(library);
        File absoluteLocation = getController().getProjectManager().getProject()
                .getAbsoluteResource(library.getDirectory().getPath());
        File file = new File(absoluteLocation, LIBRARY_CTKL);
        FileUtils.writeStringToFile(file, data);
    }

    @Override
    public void delete() throws IOException {
        for (Library library : getLibraries().values()) {
            library.delete();
        }
        clear();
    }

    @Override
    public void clear() {
    }

    @Override
    public void importSong(Library library, File causticFile) throws IOException, CausticException {
        final ISoundSource soundSource = getController().getRack().getSoundSource();
        // Load the song, this automatically resets the sound source
        getController().getRack().loadSong(causticFile);

        loadLibraryScene(library, causticFile, soundSource);
        loadLibraryPhrases(library, soundSource);

        getController().getRack().clearAndReset();

        getController().trigger(new OnLibraryManagerImportComplete());
    }

    @Override
    public void importPatterns(Library library, File causticFile) throws IOException,
            CausticException {
        // Load the song, this automatically resets the sound source
        //        getController().getSoundSource().loadSong(causticFile);
        //
        //        loadLibraryScene(library, causticFile, getController().getSoundSource());
        //        loadLibraryPhrases(library, getController().getSoundSource());
        //
        //        loadLibraryPatterns(library, getController().getSoundSource());
        //
        //        getController().getSoundSource().clearAndReset();
        //
        //        getController().trigger(new OnLibraryManagerImportComplete());
    }

    @SuppressWarnings("unused")
    private void loadLibraryPatterns(Library library, ISoundSource soundSource) {
        Map<String, List<LibraryPatch>> map = new TreeMap<String, List<LibraryPatch>>();

        // the numbers get merged together
        // PART1A, PART1B, PART1C etc

        // first sort the parts from the patch name
        for (LibraryPatch patch : library.getPatches()) {
            // System.out.println(patch.getName());
            String machineName = patch.getName();
            String post = machineName.substring(4);
            String index = post.substring(0, 1);

            List<LibraryPatch> list = map.get(index);
            if (list == null) {
                list = new ArrayList<LibraryPatch>();
                map.put(index, list);
            }

            list.add(patch);
        }

        // Create a PatternLibrary for EVERY defined phrase

        // how many parts [3]
        //int numParts = map.size();
        // all sets have to be the same size, test the first one to see how many
        // sets are contained IE A, B, C etc [2]
        int numPartSets = map.get("1").size();

        // Number of LibraryPatterns, the pattern holds the numParts
        int numPatterns = 64 * numPartSets;

        List<ToneDescriptor> descriptors = new ArrayList<ToneDescriptor>();

        for (Entry<String, List<LibraryPatch>> entry : map.entrySet()) {
            int index = Integer.parseInt(entry.getKey()) - 1; // parts are 1 based
            LibraryPatch firstPatch = entry.getValue().get(0);

            ToneDescriptor descriptor = new ToneDescriptor(index, "PART" + (index + 1),
                    firstPatch.getToneType());
            descriptor.setPatchId(firstPatch.getId());
            //for (LibraryPatch patch : entry.getValue()) {
            //    List<LibraryPhrase> phrases = library.findPhrasesByTag(patch.getName());
            //
            //}
            descriptors.add(descriptor);
        }

        ToneSet set = new ToneSet(descriptors);

        List<LibraryPattern> patterns = new ArrayList<LibraryPattern>();
        // create the LibraryPatterns
        for (int i = 0; i < numPatterns; i++) {
            LibraryPattern pattern = new LibraryPattern();
            pattern.setId(UUID.randomUUID());
            pattern.setIndex(i);
            pattern.setMetadataInfo(new MetadataInfo());
            pattern.setToneSet(set);
            patterns.add(pattern);

            for (ToneDescriptor descriptor : set.getDescriptors()) {
                int partIndex = descriptor.getIndex();
                String name = descriptor.getName();
                int patternBank = i / (64);
                name = name + alpha[patternBank];
                System.out.println("   " + name);
                List<LibraryPhrase> list = library.findPhrasesByTagStartsWith(name);
                LibraryPhrase phrase = getPhraseFor(list, i);
                pattern.putPhrase(partIndex, phrase);
            }

            // get the phrase id of the pattern
        }

        library.setPatterns(patterns);
    }

    private String[] alpha = {
            "A", "B", "C", "D"
    };

    private LibraryPhrase getPhraseFor(List<LibraryPhrase> phrases, int index) {
        int bank = PatternUtils.getBank(index);
        int pattern = PatternUtils.getPattern(index);

        // Find the Phrase that has the same bank and pattern
        for (LibraryPhrase phrase : phrases) {

            if (phrase.getBankIndex() == bank && phrase.getPatternIndex() == pattern)
                return phrase;
        }
        return null;
    }

    private void loadLibraryScene(Library library, File causticFile, ISoundSource soundSource)
            throws IOException {
        String name = causticFile.getName().replace(".caustic", "");
        LibraryScene scene = new LibraryScene();
        scene.setMetadataInfo(new MetadataInfo());

        scene.setId(UUID.randomUUID());
        library.addScene(scene);

        //--------------------------------------
        SoundSourceDescriptor soundSourceDescriptor = new SoundSourceDescriptor();

        for (int i = 0; i < 6; i++) {
            Tone tone = soundSource.getTone(i);
            LibraryPatch patch = null;

            if (tone != null) {

                patch = new LibraryPatch();
                patch.setName(tone.getName());
                patch.setToneType(tone.getToneType());
                patch.setMetadataInfo(new MetadataInfo());
                patch.setId(UUID.randomUUID());
                TagUtils.addDefaultTags(tone, patch);
                relocatePresetFile(tone, library, patch);
                library.addPatch(patch);

                tone.setDefaultPatchId(patch.getId());
                soundSourceDescriptor.addTone(tone, patch.getId());
            }
        }

        scene.setSoundSourceDescriptor(soundSourceDescriptor);

        //SoundMixerDescriptor soundMixerDescriptor;

        //        SoundMixerDescriptor soundMixerDescriptor = new SoundMixerDescriptor();
        //        soundMixerDescriptor.setMasterMixer(getController().getRack().getSoundMixer()
        //                .getMasterMixer());
        //scene.setSoundMixerDescriptor(soundMixerDescriptor);

        TagUtils.addDefaultTags(name, getController(), scene);
    }

    private void loadLibraryPhrases(Library library, ISoundSource soundSource) {
        for (int i = 0; i < 6; i++) {
            Tone tone = soundSource.getTone(i);
            if (tone != null) {

                String result = PatternSequencerMessage.QUERY_PATTERNS_WITH_DATA.queryString(
                        getController(), i);

                if (result == null)
                    continue;

                for (String patternName : result.split(" ")) {
                    int bankIndex = PatternUtils.toBank(patternName);
                    int patternIndex = PatternUtils.toPattern(patternName);

                    // set the current bank and pattern of the machine to query
                    // the string pattern data
                    PatternSequencerMessage.BANK.send(getController(), i, bankIndex);
                    PatternSequencerMessage.PATTERN.send(getController(), i, patternIndex);

                    //----------------------------------------------------------------
                    // Load Pattern
                    //----------------------------------------------------------------

                    // load one phrase per pattern; load ALL patterns
                    // as caustic machine patterns
                    int length = (int)PatternSequencerMessage.NUM_MEASURES
                            .query(getController(), i);
                    float tempo = OutputPanelMessage.BPM.query(getController());
                    String noteData = PatternSequencerMessage.QUERY_NOTE_DATA.queryString(
                            getController(), i);

                    LibraryPhrase phrase = new LibraryPhrase();
                    phrase.setBankIndex(bankIndex);
                    phrase.setPatternIndex(patternIndex);
                    phrase.setMachineName(tone.getName());
                    phrase.setMetadataInfo(new MetadataInfo());
                    phrase.setId(UUID.randomUUID());
                    phrase.setLength(length);
                    phrase.setTempo(tempo);
                    phrase.setToneType(tone.getToneType());
                    phrase.setNoteData(noteData);
                    phrase.setResolution(calculateResolution(noteData));
                    TagUtils.addDefaultTags(phrase);
                    library.addPhrase(phrase);
                }
            }
        }
    }

    protected void relocatePresetFile(Tone tone, Library library, LibraryPatch patch)
            throws IOException {
        String id = patch.getId().toString();
        tone.getComponent(SynthComponent.class).savePreset(id);
        File presetFile = RuntimeUtils.getCausticPresetsFile(tone.getToneType().getValue(), id);
        if (!presetFile.exists()) {
            throw new IOException("Preset file does not exist");
        }

        File presetsDirectory = library.getPresetsDirectory();
        File destFile = new File(presetsDirectory, presetFile.getName());
        FileUtils.copyFile(presetFile, destFile);
        FileUtils.deleteQuietly(presetFile);
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

    //    @Override
    //    public boolean isLibrary(File reletiveFile) {
    //        return new File(librariesDirectory, reletiveFile.getPath()).exists();
    //    }
    public Library getLibrary(File reletivePath) {
        for (Library library : libraries.values()) {
            if (library.getName().endsWith(reletivePath.getName()))
                return library;
        }
        return null;
    }

    public void removeLibrary(Library library) throws IOException {
        library.delete();
        libraries.remove(library.getId());
    }

    @Override
    public void deleteLibrary(File reletivePath) throws IOException {
        Library library = getLibrary(reletivePath);
        removeLibrary(library);
    }

    @Override
    public void exportLibrary(File file, Library library) throws IOException {
        Collection<File> files = FileUtils.listFiles(library.getDirectory(), null, true);
        Compress compress = new Compress(files, file.getAbsolutePath());
        compress.zip();
    }

    //    @Override
    //    public Library importLibrary(File file) throws IOException {
    //        // extract to libraries dir
    //        File location = new File(librariesDirectory, file.getName().replace(".ctkl", ""));
    //        Decompress decompress = new Decompress(file.getAbsolutePath(), location.getAbsolutePath());
    //        decompress.unzip();
    //        Library library = loadLibrary(location);
    //        return library;
    //    }

}
