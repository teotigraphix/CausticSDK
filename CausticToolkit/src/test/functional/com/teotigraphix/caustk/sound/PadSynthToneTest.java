
package com.teotigraphix.caustk.sound;

import static org.junit.Assert.*;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.tone.PadSynthTone;
import com.teotigraphix.caustk.tone.padsynth.LFO1Component.Target;

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
        // tone.getHarmonics().setWidth(1, 0.42f);
        // tone.getHarmonics().setHarmonic(0, 0, 0.4f);
        tone.restore();

        //------------------------------
        // Harmonics
        //------------------------------

        assertEquals(0f, tone.getHarmonics().getHarmonic(0, 0), 0.01f);
        assertEquals(0f, tone.getHarmonics().getWidth(0), 0.01f);
        assertEquals(0f, tone.getHarmonics().getWidth(1), 0.01f);

        //------------------------------
        // LFO1
        //------------------------------

        assertEquals(Target.Off, tone.getLFO1().getTarget());
        assertEquals(6, tone.getLFO1().getRate());
        assertEquals(0f, tone.getLFO1().getDepth(), 0f);
        assertEquals(0f, tone.getLFO1().getPhase(), 0f);

        //------------------------------
        // LFO2
        //------------------------------

        assertEquals(Target.Off, tone.getLFO2().getTarget());
        assertEquals(6, tone.getLFO2().getRate());
        assertEquals(0f, tone.getLFO2().getDepth(), 0f);
        assertEquals(0f, tone.getLFO2().getPhase(), 0f);

        //------------------------------
        // Morph
        //------------------------------

        assertEquals(0f, tone.getMorph().getAttack(), 0f);
        assertEquals(0f, tone.getMorph().getBlend(), 0f);
        assertEquals(0f, tone.getMorph().getDecay(), 0f);
        assertEquals(1, tone.getMorph().getEnvelopeEnabled());
        assertEquals(0f, tone.getMorph().getRelease(), 0f);
        assertEquals(1.0f, tone.getMorph().getSustain(), 0f);

        //------------------------------
        // Volume
        //------------------------------

        assertEquals(0f, tone.getVolume().getAttack(), 0f);
        assertEquals(0f, tone.getVolume().getDecay(), 0f);
        assertEquals(1.0f, tone.getVolume().getGain1(), 0f);
        assertEquals(1.0f, tone.getVolume().getGain2(), 0f);
        assertEquals(1.0f, tone.getVolume().getOut(), 0f);
        assertEquals(0f, tone.getVolume().getRelease(), 0f);
        assertEquals(1.0f, tone.getVolume().getSustain(), 0f);
    }
}
