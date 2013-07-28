
package com.teotigraphix.caustk.core.components.modular;

import com.teotigraphix.caustk.controller.ICaustkController;

public class SVFilter extends ModularComponentBase {

    //----------------------------------
    // cutoff
    //----------------------------------

    private float cutoff;

    public float getCutoff() {
        return cutoff;
    }

    float getCutoff(boolean restore) {
        return getValue("cutoff");
    }

    /**
     * @param value (0..1)
     */
    public void setCutoff(float value) {
        if (value == cutoff)
            return;
        cutoff = value;
        if (value < 0f || value > 1f)
            newRangeException("cutoff", "0..1", value);
        setValue("cutoff", value);
    }

    //----------------------------------
    // resonance
    //----------------------------------

    private float resonance;

    public float getResonance() {
        return resonance;
    }

    float getResonance(boolean restore) {
        return getValue("resonance");
    }

    /**
     * @param value (0..1)
     */
    public void setResonance(float value) {
        if (value == resonance)
            return;
        resonance = value;
        if (value < 0f || value > 1f)
            newRangeException("resonance", "0..1", value);
        setValue("resonance", value);
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

    public SVFilter() {
    }

    public SVFilter(ICaustkController controller, int bay) {
        super(controller, bay);
    }

    @Override
    protected int getNumBays() {
        return 1;
    }

    public enum SVFilterJack implements IModularJack {

        InInput(0),

        InCutoff(1),

        InResonance(2),

        OutLP(0),

        OutHP(1),

        OutBP(2);

        private int value;

        public final int getValue() {
            return value;
        }

        SVFilterJack(int value) {
            this.value = value;
        }
    }
}
