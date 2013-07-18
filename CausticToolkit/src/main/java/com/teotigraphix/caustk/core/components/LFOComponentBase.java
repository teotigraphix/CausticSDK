
package com.teotigraphix.caustk.core.components;

import com.teotigraphix.caustic.osc.CausticMessage;

public abstract class LFOComponentBase extends ToneComponent {

    protected transient CausticMessage depthMessage;

    protected transient CausticMessage rateMessage;

    //--------------------------------------------------------------------------
    //
    // ILFOComponent API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // rate
    //----------------------------------

    protected int rate = 1;

    public int getRate() {
        return rate;
    }

    int getRate(boolean restore) {
        return (int)rateMessage.query(getEngine(), getToneIndex());
    }

    public void setRate(int value) {
        if (value == rate)
            return;
        if (value < 0 || value > 12)
            throw newRangeException("lfo1_rate", "0..12", value);
        rate = value;
        rateMessage.send(getEngine(), getToneIndex(), rate);
    }

    //----------------------------------
    // depth
    //----------------------------------

    private float depth = 0.0f;

    public float getDepth() {
        return depth;
    }

    float getDepth(boolean restore) {
        return depthMessage.query(getEngine(), getToneIndex());
    }

    public void setDepth(float value) {
        if (value == depth)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("lfo1_depth", "0..1", value);
        depth = value;
        depthMessage.send(getEngine(), getToneIndex(), depth);
    }

    public LFOComponentBase() {
    }

    @Override
    public void restore() {
        setDepth(getDepth(true));
        setRate(getRate(true));
    }

}
