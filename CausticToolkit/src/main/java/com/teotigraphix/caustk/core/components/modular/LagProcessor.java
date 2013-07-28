
package com.teotigraphix.caustk.core.components.modular;

import com.teotigraphix.caustk.controller.ICaustkController;

public class LagProcessor extends ModularComponentBase {

    //----------------------------------
    // rateA
    //----------------------------------

    private float rateA;

    public float getRateA() {
        return rateA;
    }

    float getRateA(boolean restore) {
        return getValue("rate_a");
    }

    /**
     * @param value (0..1)
     */
    public void setRateA(float value) {
        if (value == rateA)
            return;
        rateA = value;
        if (value < 0f || value > 1f)
            newRangeException("rate_a", "0..1", value);
        setValue("rate_a", value);
    }

    //----------------------------------
    // rateB
    //----------------------------------

    private float rateB;

    public float getRateB() {
        return rateB;
    }

    float getRateB(boolean restore) {
        return getValue("rate_b");
    }

    /**
     * @param value (0..1)
     */
    public void setRateB(float value) {
        if (value == rateB)
            return;
        rateB = value;
        if (value < 0f || value > 1f)
            newRangeException("rate_b", "0..1", value);
        setValue("rate_b", value);
    }

    public LagProcessor() {
    }

    public LagProcessor(ICaustkController controller, int bay) {
        super(controller, bay);
    }

    @Override
    protected int getNumBays() {
        return 1;
    }

    public enum LagProcessorJack implements IModularJack {

        InA(0),

        InB(1),

        OutA(0),

        OutB(1);

        private int value;

        public final int getValue() {
            return value;
        }

        LagProcessorJack(int value) {
            this.value = value;
        }
    }
}
