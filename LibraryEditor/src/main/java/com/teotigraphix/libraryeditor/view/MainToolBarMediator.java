
package com.teotigraphix.libraryeditor.view;

import java.io.File;
import java.io.IOException;

import javafx.scene.control.Dialogs;
import javafx.scene.control.Dialogs.DialogResponse;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import com.google.inject.Inject;
import com.teotigraphix.caustic.utils.FileUtil;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.core.MediatorBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.BeatboxTone;
import com.teotigraphix.caustk.tone.PCMSynthTone;
import com.teotigraphix.caustk.tone.SubSynthTone;
import com.teotigraphix.caustk.tone.SynthTone;
import com.teotigraphix.caustk.tone.ToneType;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.libraryeditor.model.LibraryModel;

public class MainToolBarMediator extends MediatorBase {

    @Inject
    LibraryModel libraryModel;

    public MainToolBarMediator() {
    }

    @Inject
    public MainToolBarMediator(ICaustkApplicationProvider provider) {
        super(provider);
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();
    }

    public void createLibrary() {
        // applicationModel.getStage(
        String libraryName = Dialogs.showInputDialog(null, "Name", "Create a new Library",
                "Create library", "UntitledLibrary");
        if (libraryName == null)
            return; // canceled

        File libraryFile = new File(libraryName);
        if (getController().getLibraryManager().isLibrary(libraryFile)) {
            DialogResponse response = Dialogs.showConfirmDialog(null, "Library " + libraryName
                    + " exists, overwrite?");
            if (response == DialogResponse.NO || response == DialogResponse.CANCEL)
                return;

            try {
                getController().getLibraryManager().deleteLibrary(libraryFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Library library = null;
        try {
            library = getController().getLibraryManager().createLibrary(libraryName);
            getController().getLibraryManager().setSelectedLibrary(library);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void importFile() {
        System.out.println("onSaveButtonAction");
        FileChooser chooser = FileUtil.createDefaultFileChooser(RuntimeUtils
                .getCausticSongsDirectory().getAbsolutePath(), "Caustic song file", "*.caustic");
        File causticFile = chooser.showOpenDialog(null);

        // if no current library open error dialog
        Library library = getController().getLibraryManager().getSelectedLibrary();
        try {
            getController().getLibraryManager().importSong(library, causticFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CausticException e) {
            e.printStackTrace();
        }

    }

    public void saveLibrary() {
        Library library = getController().getLibraryManager().getSelectedLibrary();
        try {
            getController().getLibraryManager().saveLibrary(library);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLibrary() {
        DirectoryChooser chooser = FileUtil.createDefaultDirectoryChooser(null);
        File libDirectory = chooser.showDialog(null);
        Library library = getController().getLibraryManager().loadLibrary(libDirectory.getName());
        getController().getLibraryManager().setSelectedLibrary(library);
    }

    private SubSynthTone subSynthTone;

    private BeatboxTone beatboxTone;

    private BasslineTone basslineSynthTone;

    private PCMSynthTone pcmSynthTone;

    private void createAudioSystem() throws CausticException {
        ISoundSource soundSource = getController().getSoundSource();

        // XXX TEMP
        soundSource.clearAndReset();

        subSynthTone = (SubSynthTone)soundSource.createTone("subsynth", ToneType.SubSynth);
        pcmSynthTone = (PCMSynthTone)soundSource.createTone("pcmsyth", ToneType.PCMSynth);
        basslineSynthTone = (BasslineTone)soundSource.createTone("bassline", ToneType.Bassline);
        beatboxTone = (BeatboxTone)soundSource.createTone("beatbox", ToneType.Beatbox);
    }

    public void previewItem() {
        if (subSynthTone == null) {
            try {
                createAudioSystem();
            } catch (CausticException e) {
                e.printStackTrace();
            }
        }

        // Preview the selected phrase
        //if (libraryModel.getSelectedKind() != ItemKind.PHRASE)
        //   return;

        ISoundSource soundSource = getController().getSoundSource();

        LibraryPhrase libraryPhrase = libraryModel.getLibraryPhrase();
        LibraryPatch libraryPatch = libraryModel.getLibraryPatch();

        ToneType toneType = libraryPhrase.getToneType();

        SynthTone tone = (SynthTone)soundSource.getToneByName(toneType.getValue());
        if (libraryPatch != null) {
            File file = libraryPatch.getPresetFile();
            file = getController().getLibraryManager().getSelectedLibrary().getPresetFile(file);
            tone.getSynth().loadPreset(file.getAbsolutePath());
        }

        // clear A01
        tone.getPatternSequencer().clear();

        // set the note data at A01
        tone.getPatternSequencer().setSelectedPattern(0, 0);

        // set the length
        int numMeasures = libraryPhrase.getLength();
        tone.getPatternSequencer().setLength(numMeasures);
        tone.getPatternSequencer().initializeData(libraryPhrase.getNoteData());

        // add to song

        // play
        getController().getSystemSequencer().play(SequencerMode.PATTERN);
    }

}
