
package com.teotigraphix.caustk.project;

import java.io.File;

import com.teotigraphix.caustic.internal.sequencer.PatternSequencerUtils;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.sequencer.IPatternSequencer2;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.tone.Tone;

public class TrackUtils {

    /**
     * @param controller
     * @param track
     * @param libraryPatch
     */
    public static void assignPatch(ICaustkController controller, Track track,
            LibraryPatch libraryPatch) {
        // get the current Library
        Library library = controller.getLibraryManager().getSelectedLibrary();
        // get the absolute preset File for the LibraryPatch
        File file = library.getPresetFile(libraryPatch.getPresetFile());
        // get the Machine for the Tone
        IMachine machine = getMachine(controller, track);
        // load the Machine witht eh preset File
        machine.loadPreset(file.getAbsolutePath());
        // restore the Machine's values
        machine.restore();
    }

    public static void assignNotes(ICaustkController controller, Track track,
            TrackPhrase trackPhrase) {
        IPatternSequencer2 sequencer = getPatternSequencer(controller, track);

        int lastBank = sequencer.getSelectedBank();
        int lastPattern = sequencer.getSelectedIndex();
        sequencer.setSelectedPattern(trackPhrase.getBankIndex(), trackPhrase.getPatternIndex());

        sequencer.setLength(trackPhrase.getNumMeasures());

        PatternSequencerUtils.applyNoteData(sequencer, trackPhrase.getNoteData());

        sequencer.setSelectedPattern(lastBank, lastPattern);
    }

    public static Tone getTone(ICaustkController controller, Track track) {
        Tone tone = controller.getSoundSource().getTone(track.getIndex());
        return tone;
    }

    public static IMachine getMachine(ICaustkController controller, Track track) {
        return getTone(controller, track).getMachine();
    }

    public static IPatternSequencer2 getPatternSequencer(ICaustkController controller, Track track) {
        return getMachine(controller, track).getPatternSequencer();
    }
}
