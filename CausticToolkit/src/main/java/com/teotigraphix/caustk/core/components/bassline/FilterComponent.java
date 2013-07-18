
package com.teotigraphix.caustk.core.components.bassline;

import com.teotigraphix.caustk.core.components.FilterComponentBase;
import com.teotigraphix.caustk.core.osc.FilterMessage;

public class FilterComponent extends FilterComponentBase {

    //--------------------------------------------------------------------------
    //
    // IBasslineFilter API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // decay
    //----------------------------------

    private float decay = 0f;

    public float getDecay() {
        return decay;
    }

    float getDecay(boolean restore) {
        return FilterMessage.FILTER_DECAY.query(getEngine(), getToneIndex());
    }

    public void setDecay(float value) {
        if (value == decay)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("filter_decay", "0..1", value);
        decay = value;
        FilterMessage.FILTER_DECAY.send(getEngine(), getToneIndex(), decay);
    }

    //----------------------------------
    // envMod
    //----------------------------------

    private float envMod = 0.99f;

    public float getEnvMod() {
        return envMod;
    }

    float getEnvMod(boolean restore) {
        return FilterMessage.FILTER_ENVMOD.query(getEngine(), getToneIndex());
    }

    public void setEnvMod(float value) {
        if (value == envMod)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("filter_envmod", "0..1", value);
        envMod = value;
        FilterMessage.FILTER_ENVMOD.send(getEngine(), getToneIndex(), envMod);
    }

    public FilterComponent() {
    }

    @Override
    public void restore() {
        super.restore();
        setDecay(getDecay(true));
        setEnvMod(getEnvMod(true));
    }
}
