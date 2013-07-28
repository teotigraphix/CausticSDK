
package com.teotigraphix.caustk.core.components.modular;

import com.teotigraphix.caustk.controller.ICaustkController;

public class NoiseGenerator extends ModularComponentBase {

    //----------------------------------
    // lfGain
    //----------------------------------

    private float lfGain;

    public float getLFGain() {
        return lfGain;
    }

    float getLFGain(boolean restore) {
        return getValue("lf_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setLFGain(float value) {
        if (value == lfGain)
            return;
        lfGain = value;
        if (value < 0f || value > 1f)
            newRangeException("lf_gain", "0..1", value);
        setValue("lf_gain", value);
    }

    //----------------------------------
    // pinkGain
    //----------------------------------

    private float pinkGain;

    public float getPinkGain() {
        return pinkGain;
    }

    float getPinkGain(boolean restore) {
        return getValue("pink_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setPinkGain(float value) {
        if (value == pinkGain)
            return;
        pinkGain = value;
        if (value < 0f || value > 1f)
            newRangeException("pink_gain", "0..1", value);
        setValue("pink_gain", value);
    }

    //----------------------------------
    // whiteGain
    //----------------------------------

    private float whiteGain;

    public float getWhiteGain() {
        return whiteGain;
    }

    float getWhiteGain(boolean restore) {
        return getValue("white_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setWhiteGain(float value) {
        if (value == whiteGain)
            return;
        whiteGain = value;
        if (value < 0f || value > 1f)
            newRangeException("white_gain", "0..1", value);
        setValue("white_gain", value);
    }

    public NoiseGenerator() {
    }

    public NoiseGenerator(ICaustkController controller, int bay) {
        super(controller, bay);
    }

    @Override
    protected int getNumBays() {
        return 1;
    }

    public enum NoiseGeneratorJack implements IModularJack {

        InModulation(0),

        OutLF(0),

        OutPink(1),

        OutWhite(2);

        private int value;

        public final int getValue() {
            return value;
        }

        NoiseGeneratorJack(int value) {
            this.value = value;
        }
    }
}
