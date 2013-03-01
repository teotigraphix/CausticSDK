
package com.teotigraphix.caustic.internal.machine;

import java.io.File;
import java.util.List;

import com.teotigraphix.caustic.sequencer.IPhrase;
import com.teotigraphix.caustic.sequencer.ITrigger;
import com.teotigraphix.caustic.test.EmptyRackTestBase;

public class Test_MachineSetup extends EmptyRackTestBase {

    private static final String PRESET_SUBSYNTH = "subsynth/UNITTEST_SUBSYNTH1.subsynth";

    private static final String PRESET_PCMSYNTH = "pcmsynth/UNITTEST_PCMSYNTH1.pcmsynth";

    private static final String PRESET_BASSLINE = "bassline/UNITTEST1_BASSLINE.bassline";

    private static final String PRESET_BEATBOX = "beatbox/UNITTEST1_BEATBOX.beatbox";

    @Override
    protected void setupValues() {
        setupPresets();
        setupPatternSequencer();
        // test the pattern sequencer

    }

    private void setupPatternSequencer() {
        // TODO Unit Test if Rej fixes the 0 velocity Issue#56, add it here for the test
        // test the note_data
        subSynth1.getSequencer().addNote(60, 0f, 1f, 0.5f, 0);
        subSynth1.getSequencer().addNote(61, 1f, 2f, 0.75f, 0);
        subSynth1.getSequencer().addNote(62, 2f, 3f, 0.1f, 0);
        subSynth1.getSequencer().addNote(63, 3f, 4f, 0.25f, 1);
        // test the saving of pattern
        subSynth1.getSequencer().setBankPattern(3, 2); // D03
        assertEquals("D03", subSynth1.getSequencer().getPhrase().getId());
        subSynth1.getSequencer().getPhrase().setLength(8);
        subSynth1.getSequencer().addNote(60, 0f, 1f, 0.5f, 0);
    }

    private void setupPresets() {
        assertNull(subSynth1.getPresetName());
        assertNull(pcmSynth1.getPresetName());
        assertNull(bassline1.getPresetName());
        assertNull(beatbox.getPresetName());

        subSynth1.savePreset("UNITTEST_SUBSYNTH1");
        pcmSynth1.savePreset("UNITTEST_PCMSYNTH1");
        bassline1.savePreset("UNITTEST1_BASSLINE");
        beatbox.savePreset("UNITTEST1_BEATBOX");

        subSynth1.loadPreset(new File(mPresetsDirectory, PRESET_SUBSYNTH).getAbsolutePath());
        pcmSynth1.loadPreset(new File(mPresetsDirectory, PRESET_PCMSYNTH).getAbsolutePath());
        bassline1.loadPreset(new File(mPresetsDirectory, PRESET_BASSLINE).getAbsolutePath());
        beatbox.loadPreset(new File(mPresetsDirectory, PRESET_BEATBOX).getAbsolutePath());

        assertEquals("UNITTEST_SUBSYNTH1", subSynth1.getPresetName());
        assertEquals("UNITTEST_PCMSYNTH1", pcmSynth1.getPresetName());
        assertEquals("UNITTEST1_BASSLINE", bassline1.getPresetName());
        assertEquals("UNITTEST1_BEATBOX", beatbox.getPresetName());
    }

    @Override
    protected void assertValues() {
        assertPresets();
        assertPatternSequencer();
    }

    private void assertPresets() {
        assertEquals("UNITTEST_SUBSYNTH1", subSynth1.getPresetName());
        assertEquals("UNITTEST_PCMSYNTH1", pcmSynth1.getPresetName());
        assertEquals("UNITTEST1_BASSLINE", bassline1.getPresetName());
        // TODO (mschmalle) OSC FAIL BeatboxPreset 
        //assertEquals("UNITTEST1_BEATBOX", beatbox.getPresetName());
    }

    private void assertPatternSequencer() {
        List<IPhrase> phrases = subSynth1.getSequencer().getPhrases(0);
        assertEquals(1, phrases.size());
        IPhrase A01 = phrases.get(0);
        assertEquals("A01", A01.getId());
        assertEquals(4, A01.getTriggers().size());
        // test the triggers
        ITrigger[] triggers = A01.getTriggers().toArray(new ITrigger[A01.getTriggers().size()]);
        assertEquals(60, triggers[0].getPitch());
        assertEquals(61, triggers[1].getPitch());
        assertEquals(62, triggers[2].getPitch());
        assertEquals(63, triggers[3].getPitch());

        assertEquals(3, subSynth1.getSequencer().getSelectedBank());
        assertEquals(2, subSynth1.getSequencer().getSelectedPattern());
        assertEquals(8, subSynth1.getSequencer().getPhrase().getLength());
    }
}
