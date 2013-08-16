
package com.teotigraphix.caustk.sound;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.EffectRackMessage;
import com.teotigraphix.caustk.sound.effect.AutowahEffect;
import com.teotigraphix.caustk.sound.effect.BitcrusherEffect;
import com.teotigraphix.caustk.sound.effect.ChorusEffect;
import com.teotigraphix.caustk.sound.effect.CompressorEffect;
import com.teotigraphix.caustk.sound.effect.DistortionEffect;
import com.teotigraphix.caustk.sound.effect.EffectType;
import com.teotigraphix.caustk.sound.effect.FlangerEffect;
import com.teotigraphix.caustk.sound.effect.ParametricEQEffect;
import com.teotigraphix.caustk.sound.effect.PhaserEffect;
import com.teotigraphix.caustk.tone.SubSynthTone;

@SuppressWarnings("unused")
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

    }

    @Test
    public void test_bitcrusher() throws CausticException {
        // OSC: /caustic/effects_rack/create 0 0 8
        BitcrusherEffect effect = (BitcrusherEffect)assertEffect(EffectType.BITCRUSHER, 1);

    }

    @Test
    public void test_chorus() throws CausticException {
        // OSC: /caustic/effects_rack/create 0 0 8
        ChorusEffect effect = (ChorusEffect)assertEffect(EffectType.CHORUS, 0);

    }

    @Test
    public void test_compressor() throws CausticException {
        // OSC: /caustic/effects_rack/create 0 0 8
        CompressorEffect effect = (CompressorEffect)assertEffect(EffectType.COMPRESSOR, 1);

    }

    @Test
    public void test_distortion() throws CausticException {
        // OSC: /caustic/effects_rack/create 0 0 8
        DistortionEffect effect = (DistortionEffect)assertEffect(EffectType.DISTORTION, 0);

    }

    @Test
    public void test_flanger() throws CausticException {
        // OSC: /caustic/effects_rack/create 0 0 8
        FlangerEffect effect = (FlangerEffect)assertEffect(EffectType.FLANGER, 1);

    }

    @Test
    public void test_parametriceq() throws CausticException {
        // OSC: /caustic/effects_rack/create 0 0 8
        ParametricEQEffect effect = (ParametricEQEffect)assertEffect(EffectType.PARAMETRICEQ, 0);

    }

    @Test
    public void test_phaser() throws CausticException {
        // OSC: /caustic/effects_rack/create 0 0 8
        PhaserEffect effect = (PhaserEffect)assertEffect(EffectType.PHASER, 1);

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
