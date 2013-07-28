
package com.teotigraphix.caustk.core.components.modular;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.components.modular.DADSREnvelope.EnvelopeSlope;

public class DecayEnvelope extends ModularComponentBase {

    //----------------------------------
    // decay
    //----------------------------------

    private float decay;

    public float getDecay() {
        return decay;
    }

    float getDecay(boolean restore) {
        return getValue("decay");
    }

    /**
     * @param value (0..2)
     */
    public void setDecay(float value) {
        if (value == decay)
            return;
        decay = value;
        if (value < 0f || value > 2f)
            newRangeException("decay", "0..2", value);
        setValue("decay", value);
    }

    //----------------------------------
    // decaySlope
    //----------------------------------

    private EnvelopeSlope decaySlope;

    public EnvelopeSlope getDecaySlope() {
        return decaySlope;
    }

    EnvelopeSlope getDecaySlope(boolean restore) {
        return EnvelopeSlope.fromFloat(getValue("decay_slope"));
    }

    /**
     * @param value (0,1,2)
     */
    public void setDecaySlope(EnvelopeSlope value) {
        if (value == decaySlope)
            return;
        decaySlope = value;
        setValue("decay_slope", value.getValue());
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

    public DecayEnvelope() {
    }

    public DecayEnvelope(ICaustkController controller, int bay) {
        super(controller, bay);
    }

    @Override
    protected int getNumBays() {
        return 1;
    }

    @Override
    public void restore() {
        super.restore();
        setDecay(getDecay(true));
        setDecaySlope(getDecaySlope(true));
        setOutGain(getOutGain(true));
    }

    public enum DecayEnvelopeJack implements IModularJack {

        Out(0),

        InModulation(0);

        private int value;

        public final int getValue() {
            return value;
        }

        DecayEnvelopeJack(int value) {
            this.value = value;
        }
    }
}
