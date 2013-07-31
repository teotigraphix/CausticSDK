
package com.teotigraphix.caustk.library;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerBase;
import com.teotigraphix.caustk.controller.SubControllerModel;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.PatternUtils;
import com.teotigraphix.caustk.core.components.PatternSequencerComponent.Resolution;
import com.teotigraphix.caustk.core.components.SynthComponent;
import com.teotigraphix.caustk.core.osc.OutputPanelMessage;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.ToneType;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/*



*/

public class LibraryManager extends SubControllerBase implements ILibraryManager {

    @Override
    protected Class<? extends SubControllerModel> getModelType() {
        return LibraryManagerModel.class;
    }

    LibraryManagerModel getModel() {
        return (LibraryManagerModel)getInternalModel();
    }

    //--------------------------------------------------------------------------
    // API
    //--------------------------------------------------------------------------

    //----------------------------------
    // selectedLibrary
    //----------------------------------

    private File librariesDirectory;

    @Override
    public Library getSelectedLibrary() {
        return getModel().getSelectedLibrary();
    }

    @Override
    public void setSelectedLibrary(Library value) {
        getModel().setSelectedLibrary(value);
        getController().getDispatcher().trigger(new OnLibraryManagerSelectedLibraryChange());
    }

    public LibraryManager(ICaustkController controller) {
        super(controller);

        File root = controller.getConfiguration().getApplicationRoot();
        if (!root.exists())
            throw new RuntimeException("Application root not specified");

        librariesDirectory = new File(root, "libraries");
        if (!librariesDirectory.exists())
            librariesDirectory.mkdirs();
    }

    @Override
    protected void loadState(Project project) {
        super.loadState(project);

        load();
        String id = getController().getProjectManager().getSessionPreferences()
                .getString("selectedLibrary");
        if (id != null) {
            setSelectedLibrary(getModel().getLibraries().get(UUID.fromString(id)));
        }
    }

    @Override
    protected void saveState(Project project) {
        super.saveState(project);

        // if the project has selected a library, save it
        if (getSelectedLibrary() != null) {
            getController().getProjectManager().getSessionPreferences()
                    .put("selectedLibrary", getSelectedLibrary().getId());
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

        if (!librariesDirectory.exists())
            return;

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
    public Library createLibrary(String name) throws IOException {
        File newDirectory = new File(librariesDirectory, name);
        //if (newDirectory.exists())
        //    throw new CausticException("Library already exists " + newDirectory.getAbsolutePath());
        newDirectory.mkdir();

        // create a default scene for every new Library
        LibraryScene defaultScene = null;
        try {
            defaultScene = createDefaultScene();
        } catch (CausticException e) {
            e.printStackTrace();
        }

        Library library = new Library();
        library.setId(UUID.randomUUID());
        library.setMetadataInfo(new MetadataInfo());
        library.setDirectory(newDirectory);
        library.mkdirs();

        getModel().getLibraries().put(library.getId(), library);

        library.addScene(defaultScene);

        saveLibrary(library);

        return library;
    }

    private LibraryScene createDefaultScene() throws CausticException {
        getController().getSoundSource().clearAndReset();

        getController().getSoundSource().createTone("SubSynth", ToneType.SubSynth);
        getController().getSoundSource().createTone("PCMSynth1", ToneType.PCMSynth);
        getController().getSoundSource().createTone("PCMSynth2", ToneType.PCMSynth);
        getController().getSoundSource().createTone("Bassline1", ToneType.Bassline);
        getController().getSoundSource().createTone("Bassline2", ToneType.Bassline);
        getController().getSoundSource().createTone("Beatbox", ToneType.Beatbox);

        for (Tone tone : getController().getSoundSource().getTones()) {
            tone.restore();
        }

        getController().getSoundMixer().restore();

        LibraryScene libraryScene = new LibraryScene();
        libraryScene.setId(UUID.randomUUID());
        MetadataInfo metadataInfo = new MetadataInfo();
        metadataInfo.addTag("DefaultScene");
        libraryScene.setMetadataInfo(metadataInfo);
        libraryScene.setSoundSourceState(new SoundSourceState());
        libraryScene.setSoundMixerState(new SoundMixerState());
        libraryScene.setEffectMixerState(new EffectMixerState());

        getController().getSoundSource().clearAndReset();

        return libraryScene;
    }

    @Override
    public Library loadLibrary(String name) {
        File directory = new File(librariesDirectory, name);
        File file = new File(directory, "library.ctk");
        if (!file.exists())
        {
            System.out.println("XXX Lib not found");
            return null;
        }

        Library library = getController().getSerializeService().fromFile(file, Library.class);
        getModel().getLibraries().put(library.getId(), library);

        //        getController().getDispatcher().trigger(new OnLibraryManagerLoadComplete(library));

        return library;
    }

    @Override
    public void saveLibrary(Library library) throws IOException {
        String data = getController().getSerializeService().toString(library);
        File file = new File(library.getDirectory(), "library.ctk");
        FileUtils.writeStringToFile(file, data);
    }

    @Override
    public void delete() throws IOException {
        for (Library library : getModel().getLibraries().values()) {
            library.delete();
        }
        clear();
    }

    @Override
    public void clear() {
        resetModel();
    }

    @Override
    public void importSong(Library library, File causticFile) throws IOException, CausticException {
        // Load the song, this automatically resets the sound source
        getController().getSoundSource().loadSong(causticFile);

        loadLibraryScene(library, causticFile, getController().getSoundSource());
        loadLibraryPhrases(library, getController().getSoundSource());

        getController().getSoundSource().clearAndReset();

        getController().getDispatcher().trigger(new OnLibraryManagerImportComplete());
    }

    private void loadLibraryScene(Library library, File causticFile, ISoundSource soundSource)
            throws IOException {
        String name = causticFile.getName().replace(".caustic", "");
        LibraryScene scene = new LibraryScene();
        scene.setMetadataInfo(new MetadataInfo());

        scene.setId(UUID.randomUUID());
        library.addScene(scene);

        //--------------------------------------
        SoundSourceState soundSourceState = new SoundSourceState();

        for (int i = 0; i < 6; i++) {
            Tone tone = soundSource.getTone(i);
            LibraryPatch patch = null;

            if (tone != null) {

                patch = new LibraryPatch();
                patch.setToneType(tone.getToneType());
                patch.setMetadataInfo(new MetadataInfo());
                patch.setId(UUID.randomUUID());
                TagUtils.addDefaultTags(tone, patch);
                relocatePresetFile(tone, library, patch);
                library.addPatch(patch);

                tone.setDefaultPatchId(patch.getId());
                soundSourceState.addTone(i, tone.serialize());
            }
        }

        scene.setSoundSourceState(soundSourceState);

        SoundMixerState soundMixerState = new SoundMixerState();
        soundMixerState.setData(getController().getSoundMixer().serialize());
        scene.setSoundMixerState(soundMixerState);

        EffectMixerState effectMixerState = new EffectMixerState();
        scene.setEffectMixerState(effectMixerState);

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

    @Override
    public boolean isLibrary(File reletiveFile) {
        return new File(librariesDirectory, reletiveFile.getPath()).exists();
    }

    @Override
    public void deleteLibrary(File reletivePath) throws IOException {
        Library library = getModel().getLibrary(reletivePath);
        getModel().removeLibrary(library);
    }
}
