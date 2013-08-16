
package com.teotigraphix.caustk.tone.components;

import com.teotigraphix.caustk.core.osc.VolumeMessage;
import com.teotigraphix.caustk.tone.ToneComponent;

public class VolumeComponent extends ToneComponent {

    //----------------------------------
    // out
    //----------------------------------

    protected float out;

    public float getOut() {
        return out;
    }

    protected float getOut(boolean restore) {
        return VolumeMessage.VOLUME_OUT.query(getEngine(), getToneIndex());
    }

    // XXX Check range for all SYNTHS
    public void setOut(float value) {
        if (value == out)
            return;
//        if (value < 0f || value > 2f)
//            throw newRangeException("out", "0..2", value);
        out = value;
        VolumeMessage.VOLUME_OUT.send(getEngine(), getToneIndex(), out);
    }

    public VolumeComponent() {
    }

    @Override
    public void restore() {
        setOut(getOut(true));
    }

}
