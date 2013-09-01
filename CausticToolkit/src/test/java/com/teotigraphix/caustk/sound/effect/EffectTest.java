
package com.teotigraphix.caustk.sound.effect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.EffectRackMessage;
import com.teotigraphix.caustk.sound.IEffect;
import com.teotigraphix.caustk.sound.ISoundMixer;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.sound.effect.DistortionEffect.Program;
import com.teotigraphix.caustk.sound.mixer.SoundMixerChannel;
import com.teotigraphix.caustk.tone.SubSynthTone;

public class EffectTest extends CaustkTestBase {

    private ISoundMixer soundMixer;

    private ISoundSource soundSource;

    private SoundMixerChannel channel1;

    @Override
    protected void start() throws CausticException, IOException {
        soundSource = controller.getSoundSource();

        soundMixer = controller.getSoundMixer();

        soundSource.createTone("part1", SubSynthTone.class);
        channel1 = soundMixer.getChannel(0);
    }

    @Override
    protected void end() {

    }

    @Test
    public void test_autowah() throws CausticException {
        // OSC: /caustic/effects_rack/create 0 0 8

        AutowahEffect effect = (AutowahEffect)assertEffect(EffectType.AUTOWAH, 0);
        assertEquals(2.23f, effect.getCutoff(), 0f);
        assertEquals(1.0f, effect.getDepth(), 0f);
        assertEquals(0.5f, effect.getResonance(), 0f);
        assertEquals(0.4f, effect.getSpeed(), 0f);
        assertEquals(1.0f, effect.getWet(), 0f);

        assertEquals(2.23f, effect.getCutoff(true), 0f);
        assertEquals(1.0f, effect.getDepth(true), 0f);
        assertEquals(0.5f, effect.getResonance(true), 0f);
        assertEquals(0.4f, effect.getSpeed(true), 0f);
        assertEquals(1.0f, effect.getWet(true), 0f);

        effect.setCutoff(3.0f);
        effect.setDepth(0.75f);
        effect.setResonance(0.13f);
        effect.setSpeed(0.24f);
        effect.setWet(0.42f);

        assertEquals(3.0f, effect.getCutoff(true), 0f);
        assertEquals(0.75f, effect.getDepth(true), 0f);
        assertEquals(0.13f, effect.getResonance(true), 0f);
        assertEquals(0.24f, effect.getSpeed(true), 0f);
        assertEquals(0.42f, effect.getWet(true), 0f);
    }

    @Test
    public void test_bitcrusher() throws CausticException {
        // OSC: /caustic/effects_rack/create 0 0 8
        BitcrusherEffect effect = (BitcrusherEffect)assertEffect(EffectType.BITCRUSHER, 1);

        assertEquals(3, effect.getDepth());
        assertEquals(0f, effect.getJitter(), 0f);
        assertEquals(0.1f, effect.getRate(), 0f);
        assertEquals(1.0f, effect.getWet(), 0f);

        assertEquals(3, effect.getDepth(true), 0f);
        assertEquals(0f, effect.getJitter(true), 0f);
        assertEquals(0.1f, effect.getRate(true), 0.01f);
        assertEquals(1.0f, effect.getWet(true), 0f);

        effect.setDepth(14);
        effect.setJitter(0.42f);
        effect.setRate(0.45f);
        effect.setWet(0.8f);

        assertEquals(14, effect.getDepth());
        assertEquals(0.42f, effect.getJitter(), 0f);
        assertEquals(0.45f, effect.getRate(), 0f);
        assertEquals(0.8f, effect.getWet(), 0f);
    }

    @Test
    public void test_chorus() throws CausticException {
        // OSC: /caustic/effects_rack/create 0 0 8
        ChorusEffect effect = (ChorusEffect)assertEffect(EffectType.CHORUS, 0);

        assertEquals(0.25f, effect.getDepth(), 0f);
        assertEquals(0.4f, effect.getRate(), 0f);
        assertEquals(0.5f, effect.getWet(), 0f);

        assertEquals(0.25f, effect.getDepth(true), 0f);
        assertEquals(0.4f, effect.getRate(true), 0f);
        assertEquals(0.5f, effect.getWet(true), 0f);

        effect.setDepth(0.7f);
        effect.setRate(0.65f);
        effect.setWet(0.2f);

        assertEquals(0.7f, effect.getDepth(), 0f);
        assertEquals(0.65f, effect.getRate(), 0f);
        assertEquals(0.2f, effect.getWet(), 0f);
    }

    @Test
    public void test_compressor() throws CausticException {
        // OSC: /caustic/effects_rack/create 0 0 8
        CompressorEffect effect = (CompressorEffect)assertEffect(EffectType.COMPRESSOR, 1);

        assertEquals(0.01f, effect.getAttack(), 0f);
        assertEquals(1f, effect.getRatio(), 0f);
        assertEquals(0.05f, effect.getRelease(), 0f);
        assertEquals(-1, effect.getSidechain());
        assertEquals(0.1f, effect.getThreshold(), 0f);

        assertEquals(0.01f, effect.getAttack(true), 0f);
        assertEquals(1f, effect.getRatio(true), 0f);
        assertEquals(0.05f, effect.getRelease(true), 0f);
        assertEquals(-1, effect.getSidechain(true));
        assertEquals(0.1f, effect.getThreshold(true), 0.01f);

        effect.setAttack(0.025f);
        effect.setRatio(0.42f);
        effect.setRelease(0.015f);
        effect.setSidechain(4);
        effect.setThreshold(0.6f);

        assertEquals(0.025f, effect.getAttack(), 0f);
        assertEquals(0.42f, effect.getRatio(), 0f);
        assertEquals(0.015f, effect.getRelease(), 0f);
        assertEquals(4, effect.getSidechain(), 0f);
        assertEquals(0.6f, effect.getThreshold(), 0f);
    }

    @Test
    public void test_distortion() throws CausticException {
        // OSC: /caustic/effects_rack/create 0 0 8
        DistortionEffect effect = (DistortionEffect)assertEffect(EffectType.DISTORTION, 0);

        assertEquals(16.3f, effect.getAmount(), 0f);
        assertEquals(Program.OVERDRIVE, effect.getProgram());

        // XXX UNIT BROKEN assertEquals(2.23f, effect.getPostGain(true), 0f);
        // XXX UNIT BROKEN assertEquals(2.23f, effect.getPreGain(true), 0f);
        assertEquals(16.3f, effect.getAmount(true), 0f);
        assertEquals(Program.OVERDRIVE, effect.getProgram(true));

        effect.setPostGain(0.75f);
        effect.setPreGain(0.6f);
        effect.setAmount(18f);
        effect.setProgram(Program.FOLDBACK);

        // XXX UNIT BROKEN assertEquals(0.75f, effect.getPostGain(true), 0f);
        // XXX UNIT BROKEN assertEquals(0.6f, effect.getPreGain(true), 0f);
        assertEquals(18f, effect.getAmount(true), 0f);
        assertEquals(Program.FOLDBACK, effect.getProgram(true));
    }

    @Test
    public void test_flanger() throws CausticException {
        // OSC: /caustic/effects_rack/create 0 0 8
        FlangerEffect effect = (FlangerEffect)assertEffect(EffectType.FLANGER, 1);

        assertEquals(0.25f, effect.getDepth(), 0f);
        assertEquals(0.4f, effect.getFeedback(), 0f);
        assertEquals(0.4f, effect.getRate(), 0f);
        assertEquals(0.5f, effect.getWet(), 0f);

        assertEquals(0.25f, effect.getDepth(true), 0f);
        assertEquals(0.4f, effect.getFeedback(true), 0f);
        assertEquals(0.4f, effect.getRate(true), 0f);
        assertEquals(0.5f, effect.getWet(true), 0f);

        effect.setDepth(0.7f);
        effect.setFeedback(0.65f);
        effect.setRate(1.3f);
        effect.setWet(0.24f);

        assertEquals(0.7f, effect.getDepth(), 0f);
        assertEquals(0.65f, effect.getFeedback(), 0f);
        assertEquals(1.3f, effect.getRate(), 0f);
        assertEquals(0.24f, effect.getWet(), 0f);
    }

    @Test
    public void test_parametriceq() throws CausticException {
        // OSC: /caustic/effects_rack/create 0 0 8
        ParametricEQEffect effect = (ParametricEQEffect)assertEffect(EffectType.PARAMETRICEQ, 0);

        assertEquals(0.54f, effect.getFrequency(), 0f);
        assertEquals(0, effect.getGain());
        assertEquals(0.49999994f, effect.getWidth(), 0f);

        assertEquals(0.54f, effect.getFrequency(true), 0f);
        assertEquals(0, effect.getGain(true));
        assertEquals(0.49999994f, effect.getWidth(true), 0f);

        effect.setFrequency(0.42f);
        effect.setGain(8);
        effect.setWidth(0.75f);

        assertEquals(0.42f, effect.getFrequency(true), 0f);
        assertEquals(8, effect.getGain(true));
        assertEquals(0.75f, effect.getWidth(true), 0f);
    }

    @Test
    public void test_phaser() throws CausticException {
        // OSC: /caustic/effects_rack/create 0 0 8
        PhaserEffect effect = (PhaserEffect)assertEffect(EffectType.PHASER, 1);

        assertEquals(0.8f, effect.getDepth(), 0f);
        assertEquals(0.47f, effect.getFeedback(), 0f);
        assertEquals(10, effect.getRate());

        assertEquals(0.8f, effect.getDepth(true), 0.01f);
        assertEquals(0.47f, effect.getFeedback(true), 0.01f);
        assertEquals(10, effect.getRate(true));

        effect.setDepth(0.7f);
        effect.setFeedback(0.65f);
        effect.setRate(8);

        assertEquals(0.7f, effect.getDepth(), 0f);
        assertEquals(0.65f, effect.getFeedback(), 0f);
        assertEquals(8, effect.getRate());
    }

    private IEffect assertEffect(EffectType type, int slot) throws CausticException {
        IEffect effect = channel1.addEffect(type, slot);
        assertNotNull(effect);
        assertEquals(type, effect.getType());
        assertEquals(type.getValue(), (int)EffectRackMessage.TYPE.send(controller, 0, slot));
        assertEquals(slot, effect.getSlot());
        assertEquals(0, effect.getToneIndex());
        return effect;
    }
}
