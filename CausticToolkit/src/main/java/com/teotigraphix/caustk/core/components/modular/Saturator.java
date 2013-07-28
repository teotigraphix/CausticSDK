
package com.teotigraphix.caustk.core.components.modular;

import com.teotigraphix.caustk.controller.ICaustkController;

public class Saturator extends ModularComponentBase {

    //----------------------------------
    // amount
    //----------------------------------

    private float amount;

    public float getAmount() {
        return amount;
    }

    float getAmount(boolean restore) {
        return getValue("amount");
    }

    /**
     * @param value (0..1)
     */
    public void setAmount(float value) {
        if (value == amount)
            return;
        amount = value;
        if (value < 0f || value > 1f)
            newRangeException("amount", "0..1", value);
        setValue("amount", value);
    }

    //----------------------------------
    // inGain
    //----------------------------------

    private float inGain;

    public float getInGain() {
        return inGain;
    }

    float getInGain(boolean restore) {
        return getValue("in_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setInGain(float value) {
        if (value == inGain)
            return;
        inGain = value;
        if (value < 0f || value > 1f)
            newRangeException("in_gain", "0..1", value);
        setValue("in_gain", value);
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

    public Saturator() {
    }

    public Saturator(ICaustkController controller, int bay) {
        super(controller, bay);
    }

    @Override
    protected int getNumBays() {
        return 1;
    }

    public enum SaturatorJack implements IModularJack {

        InInput(0),

        OutOutput(0);

        private int value;

        public final int getValue() {
            return value;
        }

        SaturatorJack(int value) {
            this.value = value;
        }
    }
}
