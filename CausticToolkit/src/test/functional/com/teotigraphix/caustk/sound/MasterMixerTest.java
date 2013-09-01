
package com.teotigraphix.caustk.sound;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.sound.mixer.MasterMixer;

public class MasterMixerTest {

    private ICaustkApplication application;

    private ICaustkController controller;

    private ISoundSource soundSource;

    private ISoundMixer soundMixer;

    private MasterMixer masterMixer;

    @Before
    public void setUp() {
        application = CaustkApplicationUtils.createAndRun();
        controller = application.getController();
        soundSource = controller.getSoundSource();
        soundMixer = controller.getSoundMixer();
        masterMixer = soundMixer.getMasterMixer();

        start();
    }

    protected void start() {
    }

    @After
    public void tearDown() {
        end();
    }

    protected void end() {
    }

    @Test
    public void test_defaults() {
        soundSource.clearAndReset();
        masterMixer.restore();

        Assert.assertEquals(1f, masterMixer.getVolume(), 0f);

        //------------------------------
        // EQ
        //------------------------------
        Assert.assertFalse(masterMixer.getEqualizer().isBypass());
        Assert.assertEquals(1.100000023841858f, masterMixer.getEqualizer().getBass(), 0f);
        Assert.assertEquals(0.5f, masterMixer.getEqualizer().getBassMidFreq(), 0f);
        Assert.assertEquals(1.100000023841858f, masterMixer.getEqualizer().getHigh(), 0f);
        Assert.assertEquals(1.0f, masterMixer.getEqualizer().getMid(), 0f);
        Assert.assertEquals(0.5f, masterMixer.getEqualizer().getMidHighFreq(), 0f);

        //------------------------------
        // Limiter
        //------------------------------
        Assert.assertFalse(masterMixer.getLimiter().isBypass());
        Assert.assertEquals(0.01899999938905239f, masterMixer.getLimiter().getAttack(), 0f);
        // XXX was 0.5 
        Assert.assertEquals(1.0f, masterMixer.getLimiter().getPost(), 0f);
        // XXX was 2.0
        Assert.assertEquals(1.0f, masterMixer.getLimiter().getPre(), 0f);
        Assert.assertEquals(0.25f, masterMixer.getLimiter().getRelease(), 0f);

        //------------------------------
        // Delay
        //------------------------------
        Assert.assertFalse(masterMixer.getDelay().isBypass());
        Assert.assertEquals(1f, masterMixer.getDelay().getDamping(), 0f);
        Assert.assertEquals(0.5f, masterMixer.getDelay().getFeedback(), 0f);
        Assert.assertEquals(0f, masterMixer.getDelay().getFeedbackFirst(), 0f);
        // XXX ?
        Assert.assertEquals(2, masterMixer.getDelay().getLoop());
        Assert.assertEquals(0.0f, masterMixer.getDelay().getPan(0), 0f);
        Assert.assertEquals(0.0f, masterMixer.getDelay().getPan(1), 0f);
        Assert.assertEquals(0.0f, masterMixer.getDelay().getPan(2), 0f);
        Assert.assertEquals(0.0f, masterMixer.getDelay().getPan(3), 0f);
        Assert.assertEquals(0.0f, masterMixer.getDelay().getPan(4), 0f);
        Assert.assertEquals(2, masterMixer.getDelay().getSteps());
        Assert.assertEquals(1, masterMixer.getDelay().getSync());
        Assert.assertEquals(8, masterMixer.getDelay().getTime());
        Assert.assertEquals(0.5f, masterMixer.getDelay().getWet(), 1f);

        //------------------------------
        // Reverb
        //------------------------------
        Assert.assertFalse(masterMixer.getReverb().isBypass());
        Assert.assertEquals(0.7f, masterMixer.getReverb().getDiffuse(), 0.1f);
        Assert.assertEquals(0, masterMixer.getReverb().getDitherEchoes());
        Assert.assertEquals(0.25f, masterMixer.getReverb().getERDecay(), 0f);
        Assert.assertEquals(1.0f, masterMixer.getReverb().getERGain(), 0f);
        Assert.assertEquals(0.156f, masterMixer.getReverb().getHFDamping(), 0.01f);
        Assert.assertEquals(0.02500000037252903f, masterMixer.getReverb().getPreDelay(), 0f);
        Assert.assertEquals(0.7549999952316284f, masterMixer.getReverb().getRoomSize(), 0f);
        Assert.assertEquals(0.5f, masterMixer.getReverb().getStereoDelay(), 0f);
        Assert.assertEquals(0.25f, masterMixer.getReverb().getStereoSpread(), 0f);
        Assert.assertEquals(0.25f, masterMixer.getReverb().getWet(), 0f);
    }
}
