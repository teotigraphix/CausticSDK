
package com.teotigraphix.caustk.core.components.modular;

import com.teotigraphix.caustk.controller.ICaustkController;

public class Crossfader extends ModularComponentBase {

    //----------------------------------
    // gainA
    //----------------------------------

    private float gainA;

    public float getGainA() {
        return gainA;
    }

    float getGainA(boolean restore) {
        return getValue("gain_a");
    }

    /**
     * @param value (0..1)
     */
    public void setGainA(float value) {
        if (value == gainA)
            return;
        gainA = value;
        if (value < 0f || value > 1f)
            newRangeException("gain_a", "0..1", value);
        setValue("gain_a", value);
    }

    //----------------------------------
    // gainB
    //----------------------------------

    private float gainB;

    public float getGainB() {
        return gainB;
    }

    float getGainB(boolean restore) {
        return getValue("gain_b");
    }

    /**
     * @param value (0..1)
     */
    public void setGainB(float value) {
        if (value == gainB)
            return;
        gainB = value;
        if (value < 0f || value > 1f)
            newRangeException("gain_b", "0..1", value);
        setValue("gain_b", value);
    }

    //----------------------------------
    // gainOut
    //----------------------------------

    private float gainOut;

    public float getGainOut() {
        return gainOut;
    }

    float getGainOut(boolean restore) {
        return getValue("gain_out");
    }

    /**
     * @param value (0..1)
     */
    public void setGainOut(float value) {
        if (value == gainOut)
            return;
        gainOut = value;
        if (value < 0f || value > 1f)
            newRangeException("gain_out", "0..1", value);
        setValue("gain_out", value);
    }
    
    public Crossfader() {
    }

    public Crossfader(ICaustkController controller, int bay) {
        super(controller, bay);
    }

    @Override
    protected int getNumBays() {
        return 1;
    }

    public enum CrossfaderJack implements IModularJack {

        InA(0),

        InB(1),

        InFade(2),

        OutOutput(0);

        private int value;

        public final int getValue() {
            return value;
        }

        CrossfaderJack(int value) {
            this.value = value;
        }
    }
}
