
package com.teotigraphix.caustk.sound.effect;

public class BitcrusherEffect extends EffectBase {

    @Override
    public EffectType getType() {
        return EffectType.BITCRUSHER;
    }

    //----------------------------------
    // depth
    //----------------------------------

    private int depth = 3;

    public int getDepth() {
        return depth;
    }

    int getDepth(boolean restore) {
        return (int)get(BitcrusherControl.Depth);
    }

    public void setDepth(int value) {
        if (value == depth)
            return;
        if (value < 1 || value > 16)
            throw newRangeException(BitcrusherControl.Depth, "1..16", value);
        depth = value;
        set(BitcrusherControl.Depth, depth);
    }

    //----------------------------------
    // jitter
    //----------------------------------

    private float jitter = 0f;

    public float getJitter() {
        return jitter;
    }

    float getJitter(boolean restore) {
        return get(BitcrusherControl.Jitter);
    }

    public void setJitter(float value) {
        if (value == jitter)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(BitcrusherControl.Jitter, "0..1", value);
        jitter = value;
        set(BitcrusherControl.Jitter, jitter);
    }

    //----------------------------------
    // rate
    //----------------------------------

    private float rate = 0.1f;

    public float getRate() {
        return rate;
    }

    float getRate(boolean restore) {
        return get(BitcrusherControl.Rate);
    }

    public void setRate(float value) {
        if (value == rate)
            return;
        if (value < 0.01f || value > 0.5f)
            throw newRangeException(BitcrusherControl.Rate, "0.01..0.5", value);
        rate = value;
        set(BitcrusherControl.Rate, rate);
    }

    //----------------------------------
    // wet
    //----------------------------------

    private float wet = 1f;

    public float getWet() {
        return wet;
    }

    float getWet(boolean restore) {
        return get(BitcrusherControl.Wet);
    }

    public void setWet(float value) {
        if (value == wet)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(BitcrusherControl.Wet, "0..1", value);
        wet = value;
        set(BitcrusherControl.Wet, wet);
    }

    //--------------------------------------------------------------------------
    // API :: Properties
    //--------------------------------------------------------------------------

    public BitcrusherEffect(int slot, int toneIndex) {
        super(slot, toneIndex);
    }

    @Override
    public void restore() {
        setDepth(getDepth(true));
        setJitter(getJitter(true));
        setRate(getRate(true));
        setWet(getWet(true));
    }

    public enum BitcrusherControl implements IEffectControl {

        /**
         * 1..16
         */
        Depth("cutoff"),

        /**
         * 0..1
         */
        Jitter("cutoff"),

        /**
         * 0.01..0.5
         */
        Rate("resonance"),

        /**
         * 0..1
         */
        Wet("wet");

        private String control;

        public String getControl() {
            return control;
        }

        private BitcrusherControl(String control) {
            this.control = control;
        }
    }

}
