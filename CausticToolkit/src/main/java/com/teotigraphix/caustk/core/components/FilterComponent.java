
package com.teotigraphix.caustk.core.components;

import com.teotigraphix.caustic.osc.FilterMessage;

public class FilterComponent extends ToneComponent {

    //--------------------------------------------------------------------------
    // API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // cutoff
    //----------------------------------

    protected float cutoff = 1.0f;

    public float getCutoff() {
        return cutoff;
    }

    float getCutoff(boolean restore) {
        return FilterMessage.FILTER_CUTOFF.query(getEngine(), getToneIndex());
    }

    public void setCutoff(float value) {
        if (value == cutoff)
            return;
        if (value < 0 || value > 1f)
            throw newRangeException(FilterMessage.FILTER_CUTOFF.toString(), "0..1", value);
        cutoff = value;
        FilterMessage.FILTER_CUTOFF.send(getEngine(), getToneIndex(), cutoff);
    }

    //----------------------------------
    // resonance
    //----------------------------------

    protected float resonance = 0f;

    public float getResonance() {
        return resonance;
    }

    float getResonance(boolean restore) {
        return FilterMessage.FILTER_RESONANCE.query(getEngine(), getToneIndex());
    }

    public void setResonance(float value) {
        if (value == resonance)
            return;
        if (value < 0 || value > 1f)
            throw newRangeException(FilterMessage.FILTER_RESONANCE.toString(), "0..1", value);
        resonance = value;
        FilterMessage.FILTER_RESONANCE.send(getEngine(), getToneIndex(), resonance);
    }

    public FilterComponent() {
    }

    @Override
    public void restore() {
        setCutoff(getCutoff(true));
        setResonance(getResonance(true));
    }
}
