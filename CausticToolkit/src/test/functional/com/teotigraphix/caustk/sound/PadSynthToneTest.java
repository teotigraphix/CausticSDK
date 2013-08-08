
package com.teotigraphix.caustk.sound;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.tone.PadSynthTone;

public class PadSynthToneTest extends CaustkTestBase {

    private ISoundSource soundSource;

    private PadSynthTone tone;

    @Override
    protected void start() throws CausticException {
        soundSource = controller.getSoundSource();
        tone = soundSource.createTone("part1", PadSynthTone.class);
    }

    @Override
    protected void end() {

    }
    
    @Test
    public void test_defaults() {
        tone.getToneType();
    }
}
