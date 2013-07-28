
package com.teotigraphix.caustk.core.components.modular;

import com.teotigraphix.caustk.controller.ICaustkController;

public class PanModule extends ModularComponentBase {

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
    // outAGain
    //----------------------------------

    private float outAGain;

    public float getOutAGain() {
        return outAGain;
    }

    float getOutAGain(boolean restore) {
        return getValue("outa_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setOutAGain(float value) {
        if (value == outAGain)
            return;
        outAGain = value;
        if (value < 0f || value > 1f)
            newRangeException("outa_gain", "0..1", value);
        setValue("outa_gain", value);
    }

    //----------------------------------
    // outBGain
    //----------------------------------

    private float outBGain;

    public float getOutBGain() {
        return outBGain;
    }

    float getOutBGain(boolean restore) {
        return getValue("outb_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setOutBGain(float value) {
        if (value == outBGain)
            return;
        outBGain = value;
        if (value < 0f || value > 1f)
            newRangeException("outb_gain", "0..1", value);
        setValue("outb_gain", value);
    }

    public PanModule() {
    }

    public PanModule(ICaustkController controller, int bay) {
        super(controller, bay);
    }

    @Override
    protected int getNumBays() {
        return 1;
    }

    public enum PanModuleJack implements IModularJack {

        InInput(0),

        InPan(1),

        OutA(0),

        OutB(1);

        private int value;

        public final int getValue() {
            return value;
        }

        PanModuleJack(int value) {
            this.value = value;
        }
    }
}
