
package com.teotigraphix.caustic.internal.effect;

import com.teotigraphix.caustic.effect.IAutowahEffect;
import com.teotigraphix.caustic.effect.IDistortionEffect.Program;
import com.teotigraphix.caustic.effect.IEffect;
import com.teotigraphix.caustic.effect.IEffect.EffectType;
import com.teotigraphix.caustic.osc.EffectRackMessage;
import com.teotigraphix.caustic.test.EmptyRackTestBase;

public class Test_EffectsRackSetup extends EmptyRackTestBase {

    private static final int NUM_MACHINES_WITH_EFFECTS = 4;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected void setupValues() {
        assertEquals(0, mEffectsRack.getEffects().size());

        DistortionEffect distortion = (DistortionEffect)mEffectsRack.putEffect(subSynth1, 0,
                EffectType.DISTORTION);
        CompressorEffect compressor = (CompressorEffect)mEffectsRack.putEffect(subSynth1, 1,
                EffectType.COMPRESSOR);

        BitcrusherEffect bitcrusher = (BitcrusherEffect)mEffectsRack.putEffect(pcmSynth1, 0,
                EffectType.BITCRUSHER);
        FlangerEffect flanger = (FlangerEffect)mEffectsRack.putEffect(pcmSynth1, 1,
                EffectType.FLANGER);

        PhaserEffect phaser = (PhaserEffect)mEffectsRack.putEffect(pcmSynth2, 0, EffectType.PHASER);
        ChorusEffect chorus = (ChorusEffect)mEffectsRack.putEffect(pcmSynth2, 1, EffectType.CHORUS);

        AutowahEffect autowah = (AutowahEffect)mEffectsRack.putEffect(bassline1, 0,
                EffectType.AUTOWAH);
        ParametricEQEffect parametric = (ParametricEQEffect)mEffectsRack.putEffect(bassline1, 1,
                EffectType.PARAMETRICEQ);

        ParametricEQEffect removeEffect = (ParametricEQEffect)mEffectsRack.putEffect(beatbox, 0,
                EffectType.PARAMETRICEQ);
        assertNotNull(removeEffect);

        assertEquals(EffectType.DISTORTION.getValue(),
                (int)EffectRackMessage.TYPE.send(mEngine, 0, 0));
        assertEquals(EffectType.COMPRESSOR.getValue(),
                (int)EffectRackMessage.TYPE.send(mEngine, 0, 1));
        assertEquals(EffectType.BITCRUSHER.getValue(),
                (int)EffectRackMessage.TYPE.send(mEngine, 1, 0));
        assertEquals(EffectType.FLANGER.getValue(), (int)EffectRackMessage.TYPE.send(mEngine, 1, 1));
        assertEquals(EffectType.PHASER.getValue(), (int)EffectRackMessage.TYPE.send(mEngine, 2, 0));
        assertEquals(EffectType.CHORUS.getValue(), (int)EffectRackMessage.TYPE.send(mEngine, 2, 1));
        assertEquals(EffectType.AUTOWAH.getValue(), (int)EffectRackMessage.TYPE.send(mEngine, 3, 0));
        assertEquals(EffectType.PARAMETRICEQ.getValue(),
                (int)EffectRackMessage.TYPE.send(mEngine, 3, 1));

        setupDistortionValues(distortion);
        setupCompressorValues(compressor);
        setupBitcrusherValues(bitcrusher);
        setupFlangerValues(flanger);
        setupPhaserValues(phaser);
        setupChorusValues(chorus);
        setupAutoWahValues(autowah);
        setupParametricEQValues(parametric);

        setupBeatboxRemove();
    }

    @Override
    protected void assertValues() {
        // assert that the saved file now contains effects for machines
        assertEquals(NUM_MACHINES_WITH_EFFECTS, mEffectsRack.getEffects().size());

        DistortionEffect distortion = (DistortionEffect)mEffectsRack.getEffect(subSynth1, 0);
        CompressorEffect compressor = (CompressorEffect)mEffectsRack.getEffect(subSynth1, 1);

        BitcrusherEffect bitcrusher = (BitcrusherEffect)mEffectsRack.getEffect(pcmSynth1, 0);
        FlangerEffect flanger = (FlangerEffect)mEffectsRack.getEffect(pcmSynth1, 1);

        PhaserEffect phaser = (PhaserEffect)mEffectsRack.getEffect(pcmSynth2, 0);
        ChorusEffect chorus = (ChorusEffect)mEffectsRack.getEffect(pcmSynth2, 1);

        AutowahEffect autowah = (AutowahEffect)mEffectsRack.getEffect(bassline1, 0);
        ParametricEQEffect parametric = (ParametricEQEffect)mEffectsRack.getEffect(bassline1, 1);

        assertDistortionValues(distortion);
        assertCompressorValues(compressor);
        assertBitcrusherValues(bitcrusher);
        assertFlangerValues(flanger);
        assertPhaserValues(phaser);
        assertChorusValues(chorus);
        assertAutoWahValues(autowah);
        assertParametricEQValues(parametric);

        assertBeatboxRemove();
    }

    private void setupBeatboxRemove() {
        assertTrue(mEffectsRack.hasEffectsFor(beatbox));
        ParametricEQEffect parametric = (ParametricEQEffect)mEffectsRack.getEffect(beatbox, 0);
        assertNotNull(parametric);
        IEffect effect = mEffectsRack.removeEffect(beatbox, 0);
        assertSame(parametric, effect);
        assertFalse(mEffectsRack.hasEffectsFor(beatbox));
    }

    private void assertBeatboxRemove() {
        // should not exist after save since it was removed
        ParametricEQEffect parametric = (ParametricEQEffect)mEffectsRack.getEffect(beatbox, 0);
        assertNull(parametric);
    }

    //----------------------------------
    // DistortionEffect
    //----------------------------------

    private void setupDistortionValues(DistortionEffect effect) {
        assertEquals(16.3f, effect.getAmount());
        assertEquals(Program.OVERDRIVE, effect.getProgram());

        // XXX UNIT BROKEN assertEquals(2.23f, effect.getPostGain(true));
        // XXX UNIT BROKEN assertEquals(2.23f, effect.getPreGain(true));
        assertEquals(16.3f, effect.getAmount(true));
        assertEquals(Program.OVERDRIVE, effect.getProgram(true));

        effect.setPostGain(0.75f);
        effect.setPreGain(0.6f);
        effect.setAmount(18f);
        effect.setProgram(Program.FOLDBACK);
    }

    private void assertDistortionValues(DistortionEffect effect) {
        // XXX UNIT BROKEN assertEquals(0.75f, effect.getPostGain(true));
        // XXX UNIT BROKEN assertEquals(0.6f, effect.getPreGain(true));
        assertEquals(18f, effect.getAmount(true));
        assertEquals(Program.FOLDBACK, effect.getProgram(true));
    }

    //----------------------------------
    // CompressorEffect
    //----------------------------------

    private void setupCompressorValues(CompressorEffect compressor) {
        assertEquals(0.01f, compressor.getAttack());
        assertEquals(1f, compressor.getRatio());
        assertEquals(0.05f, compressor.getRelease());
        assertEquals(-1, compressor.getSidechain());
        assertEquals(0.1f, compressor.getThreshold());

        assertEquals(0.01f, compressor.getAttack(true));
        assertEquals(1f, compressor.getRatio(true));
        assertEquals(0.05f, compressor.getRelease(true));
        assertEquals(-1, compressor.getSidechain(true));
        assertEquals(0.1f, compressor.getThreshold(true));

        compressor.setAttack(0.025f);
        compressor.setRatio(0.42f);
        compressor.setRelease(0.015f);
        compressor.setSidechain(4);
        compressor.setThreshold(0.6f);
    }

    private void assertCompressorValues(CompressorEffect compressor) {
        assertEquals(0.025f, compressor.getAttack());
        assertEquals(0.42f, compressor.getRatio());
        assertEquals(0.015f, compressor.getRelease());
        assertEquals(4, compressor.getSidechain());
        assertEquals(0.6f, compressor.getThreshold());
    }

    //----------------------------------
    // BitcrusherEffect
    //----------------------------------

    private void setupBitcrusherValues(BitcrusherEffect bitcrusher) {
        assertEquals(3, bitcrusher.getDepth());
        assertEquals(0f, bitcrusher.getJitter());
        assertEquals(0.1f, bitcrusher.getRate());
        assertEquals(1.0f, bitcrusher.getWet());

        assertEquals(3, bitcrusher.getDepth(true));
        assertEquals(0f, bitcrusher.getJitter(true));
        assertEquals(0.1f, bitcrusher.getRate(true));
        assertEquals(1.0f, bitcrusher.getWet(true));

        bitcrusher.setDepth(14);
        bitcrusher.setJitter(0.42f);
        bitcrusher.setRate(0.45f);
        bitcrusher.setWet(0.8f);
    }

    private void assertBitcrusherValues(BitcrusherEffect bitcrusher) {
        assertEquals(14, bitcrusher.getDepth());
        assertEquals(0.42f, bitcrusher.getJitter());
        assertEquals(0.45f, bitcrusher.getRate());
        assertEquals(0.8f, bitcrusher.getWet());
    }

    //----------------------------------
    // FlangerEffect
    //----------------------------------

    private void setupFlangerValues(FlangerEffect flanger) {
        assertEquals(0.9f, flanger.getDepth());
        assertEquals(0.4f, flanger.getFeedback());
        assertEquals(0.4f, flanger.getRate());
        assertEquals(0.5f, flanger.getWet());

        assertEquals(0.9f, flanger.getDepth(true));
        assertEquals(0.4f, flanger.getFeedback(true));
        assertEquals(0.4f, flanger.getRate(true));
        assertEquals(0.5f, flanger.getWet(true));

        flanger.setDepth(0.7f);
        flanger.setFeedback(0.65f);
        flanger.setRate(1.3f);
        flanger.setWet(0.24f);
    }

    private void assertFlangerValues(FlangerEffect flanger) {
        assertEquals(0.7f, flanger.getDepth());
        assertEquals(0.65f, flanger.getFeedback());
        assertEquals(1.3f, flanger.getRate());
        assertEquals(0.24f, flanger.getWet());
    }

    //----------------------------------
    // PhaserEffect
    //----------------------------------

    private void setupPhaserValues(PhaserEffect phaser) {
        assertEquals(0.8f, phaser.getDepth());
        assertEquals(0.9f, phaser.getFeedback());
        assertEquals(10, phaser.getRate());

        assertEquals(0.8f, phaser.getDepth(true));
        assertEquals(0.9f, phaser.getFeedback(true));
        assertEquals(10, phaser.getRate(true));

        phaser.setDepth(0.7f);
        phaser.setFeedback(0.65f);
        phaser.setRate(8);
    }

    private void assertPhaserValues(PhaserEffect phaser) {
        assertEquals(0.7f, phaser.getDepth());
        assertEquals(0.65f, phaser.getFeedback());
        assertEquals(8, phaser.getRate());
    }

    //----------------------------------
    // ChorusEffect
    //----------------------------------

    private void setupChorusValues(ChorusEffect chorus) {
        assertEquals(0.3f, chorus.getDepth());
        assertEquals(0.4f, chorus.getRate());
        assertEquals(0.5f, chorus.getWet());

        assertEquals(0.3f, chorus.getDepth(true));
        assertEquals(0.4f, chorus.getRate(true));
        assertEquals(0.5f, chorus.getWet(true));

        chorus.setDepth(0.7f);
        chorus.setRate(0.65f);
        chorus.setWet(0.2f);
    }

    private void assertChorusValues(ChorusEffect chorus) {
        // XXX UNIT BROKEN assertEquals(0.7f, chorus.getDepth());
        assertEquals(0.65f, chorus.getRate());
        assertEquals(0.2f, chorus.getWet());
    }

    //----------------------------------
    // ParametricEQEffect
    //----------------------------------

    private void setupParametricEQValues(ParametricEQEffect parametric) {
        assertEquals(0.54f, parametric.getFrequency());
        assertEquals(0, parametric.getGain());
        assertEquals(0.49999994f, parametric.getWidth());

        assertEquals(0.54f, parametric.getFrequency(true));
        assertEquals(0, parametric.getGain(true));
        assertEquals(0.49999994f, parametric.getWidth(true));

        parametric.setFrequency(0.42f);
        parametric.setGain(8);
        parametric.setWidth(0.75f);
    }

    private void assertParametricEQValues(ParametricEQEffect parametric) {
        assertEquals(0.42f, parametric.getFrequency(true));
        assertEquals(8, parametric.getGain(true));
        assertEquals(0.75f, parametric.getWidth(true));
    }

    //----------------------------------
    // AutowahEffect
    //----------------------------------

    private void setupAutoWahValues(AutowahEffect effect) {
        assertEquals(2.23f, effect.getCutoff());
        assertEquals(1.0f, effect.getDepth());
        assertEquals(0.5f, effect.getResonance());
        assertEquals(0.4f, effect.getSpeed());
        assertEquals(1.0f, effect.getWet());

        assertEquals(2.23f, effect.getCutoff(true));
        assertEquals(1.0f, effect.getDepth(true));
        assertEquals(0.5f, effect.getResonance(true));
        assertEquals(0.4f, effect.getSpeed(true));
        assertEquals(1.0f, effect.getWet(true));

        effect.setCutoff(3.0f);
        effect.setDepth(0.75f);
        effect.setResonance(0.13f);
        effect.setSpeed(0.24f);
        effect.setWet(0.42f);
    }

    private void assertAutoWahValues(IAutowahEffect effect) {
        assertEquals(3.0f, effect.getCutoff());
        assertEquals(0.75f, effect.getDepth());
        assertEquals(0.13f, effect.getResonance());
        assertEquals(0.24f, effect.getSpeed());
        assertEquals(0.42f, effect.getWet());
    }
}
