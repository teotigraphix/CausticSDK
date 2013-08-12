
package com.teotigraphix.caustk.core.components.organ;

import com.teotigraphix.caustk.core.components.ToneComponent;
import com.teotigraphix.caustk.core.osc.OrganMessage;

public class VolumeComponent extends ToneComponent {

    //----------------------------------
    // out
    //----------------------------------

    protected float out;

    public float getOut() {
        return out;
    }

    float getOut(boolean restore) {
        return OrganMessage.VOLUME_OUT.query(getEngine(), getToneIndex());
    }

    public void setOut(float value) {
        if (value == out)
            return;
        if (value < 0f || value > 1.5f)
            throw newRangeException("out", "0..1.5", value);
        out = value;
        OrganMessage.VOLUME_OUT.send(getEngine(), getToneIndex(), out);
    }

    public VolumeComponent() {
    }

    @Override
    public void restore() {
        setOut(getOut(true));
    }

}
