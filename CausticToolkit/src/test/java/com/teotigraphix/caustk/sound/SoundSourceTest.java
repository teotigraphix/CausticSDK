
package com.teotigraphix.caustk.sound;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.BeatboxTone;
import com.teotigraphix.caustk.tone.PCMSynthTone;
import com.teotigraphix.caustk.tone.SubSynthTone;
import com.teotigraphix.caustk.tone.ToneType;
import com.teotigraphix.caustk.tone.ToneUtils;

public class SoundSourceTest {
    private ICaustkApplication application;

    private SoundSource soundSource;

    @Before
    public void setUp() throws Exception {
        application = CaustkApplicationUtils.createAndRun();
        soundSource = (SoundSource)application.getController().getSoundSource();
    }

    @After
    public void tearDown() throws Exception {
        application = null;
        soundSource = null;
    }

    @Test
    public void test_setup() throws CausticException {

    }

    @Test
    public void test_Beatbox() throws CausticException {
        BeatboxTone tone = (BeatboxTone)soundSource.createTone("tone1", ToneType.Beatbox);
        Assert.assertEquals(4, ToneUtils.getComponentCount(tone));
        Assert.assertNotNull(tone.getSynth());
        Assert.assertNotNull(tone.getPatternSequencer());
        Assert.assertNotNull(tone.getVolume());
        Assert.assertNotNull(tone.getSampler());
    }

    @Test
    public void test_PCMSynth() throws CausticException {
        PCMSynthTone tone = (PCMSynthTone)soundSource.createTone("tone1", ToneType.PCMSynth);
        Assert.assertEquals(7, ToneUtils.getComponentCount(tone));
        Assert.assertNotNull(tone.getSynth());
        Assert.assertNotNull(tone.getPatternSequencer());
        Assert.assertNotNull(tone.getVolume());
        Assert.assertNotNull(tone.getFilter());
        Assert.assertNotNull(tone.getLFO1());
        Assert.assertNotNull(tone.getSampler());
        Assert.assertNotNull(tone.getTuner());
    }

    @Test
    public void test_SubSynth() throws CausticException {
        SubSynthTone tone = (SubSynthTone)soundSource.createTone("tone1", ToneType.SubSynth);
        Assert.assertEquals(8, ToneUtils.getComponentCount(tone));
        Assert.assertNotNull(tone.getSynth());
        Assert.assertNotNull(tone.getPatternSequencer());
        Assert.assertNotNull(tone.getVolume());
        Assert.assertNotNull(tone.getFilter());
        Assert.assertNotNull(tone.getOsc1());
        Assert.assertNotNull(tone.getOsc2());
        Assert.assertNotNull(tone.getLFO1());
        Assert.assertNotNull(tone.getLFO2());
    }

    @Test
    public void test_Bassline() throws CausticException {
        BasslineTone tone = (BasslineTone)soundSource.createTone("tone1", ToneType.Bassline);
        Assert.assertEquals(7, ToneUtils.getComponentCount(tone));
        Assert.assertNotNull(tone.getSynth());
        Assert.assertNotNull(tone.getPatternSequencer());
        Assert.assertNotNull(tone.getVolume());
        Assert.assertNotNull(tone.getFilter());
        Assert.assertNotNull(tone.getOsc1());
        Assert.assertNotNull(tone.getLFO1());
        Assert.assertNotNull(tone.getDistortion());
    }

}
