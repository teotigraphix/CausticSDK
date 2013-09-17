
package com.teotigraphix.caustk.tone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sound.source.SoundSource;

public class SoundSerializationTest extends CaustkTestBase {

    // XXX Test createScene()

    private SoundSource soundSource;

    @Override
    protected void start() {
        soundSource = (SoundSource)controller.getSoundSource();
    }

    @Override
    protected void end() {
        soundSource = null;
    }

    @Test
    public void test_serialize_PadSynth() throws CausticException, IOException {
        @SuppressWarnings("unused")
        PadSynthTone tone = (PadSynthTone)soundSource.createTone(10, "part1", ToneType.PadSynth);
    }

    @Test
    public void test_serialize_SubSynth() throws CausticException, IOException {
        Tone tone = soundSource.createTone(10, "part1", ToneType.SubSynth);
        tone.setMuted(true);
        tone.setEnabled(true);
        tone.setSelected(true);

        String data = controller.getSerializeService().toPrettyString(tone);

        SubSynthTone subsynth = soundSource.createTone(data);

        // check the index is 1, not what was saved, 'tone' is already at 0 index
        assertEquals(1, subsynth.getIndex());
        assertEquals(tone.getId(), subsynth.getId());
        assertEquals("part1", subsynth.getName());
        // check all components were created
        assertEquals(tone.getComponentCount(), subsynth.getComponentCount());
        assertTrue(subsynth.isMuted());
        assertTrue(subsynth.isEnabled());
        assertTrue(subsynth.isSelected());
    }

}
