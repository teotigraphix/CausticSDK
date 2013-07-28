
package com.teotigraphix.caustk.core.components.modular;

import com.teotigraphix.caustk.controller.ICaustkController;

public class SampleAndHold extends ModularComponentBase {

    //----------------------------------
    // rate
    //----------------------------------

    private int rate;

    public int getRate() {
        return rate;
    }

    int getRate(boolean restore) {
        return (int)getValue("rate");
    }

    /**
     * @param value (0..12)
     */
    public void setRate(int value) {
        if (value == rate)
            return;
        rate = value;
        if (value < 0 || value > 12)
            newRangeException("rate", "0..12", value);
        setValue("rate", value);
    }

    //----------------------------------
    // outGain
    //----------------------------------

    private float outGain;

    public float getOutGain() {
        return outGain;
    }

    float getOutGain(boolean restore) {
        return getValue("out_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setOutGain(float value) {
        if (value == outGain)
            return;
        outGain = value;
        if (value < 0f || value > 1f)
            newRangeException("out_gain", "0..1", value);
        setValue("out_gain", value);
    }

    public SampleAndHold() {
    }

    public SampleAndHold(ICaustkController controller, int bay) {
        super(controller, bay);
    }

    @Override
    protected int getNumBays() {
        return 1;
    }

    public enum SampleAndHoldJack implements IModularJack {

        InInput(0),

        InGate(1),

        OutOutput(0);

        private int value;

        public final int getValue() {
            return value;
        }

        SampleAndHoldJack(int value) {
            this.value = value;
        }
    }

}
