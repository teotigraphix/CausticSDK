
package com.teotigraphix.caustk.core.components;

import com.teotigraphix.caustic.osc.VolumeMessage;
import com.teotigraphix.caustk.tone.ToneType;

/**
 * @see ToneType#Bassline
 */
public class VolumeComponent extends ToneComponent {

    //--------------------------------------------------------------------------
    // API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // out
    //----------------------------------

    private float out = 1.0f;

    public float getOut() {
        return out;
    }

    float getOut(boolean restore) {
        return VolumeMessage.VOLUME_OUT.query(getEngine(), getToneIndex());
    }

    public void setOut(float value) {
        if (value == out)
            return;
        if (getTone().getToneType().equals(ToneType.PCMSynth)) {
            if (value < 0 || value > 8.0f)
                throw newRangeException(VolumeMessage.VOLUME_OUT.toString(), "0..8.0", value);
        } else {
            if (value < 0 || value > 2.0f)
                throw newRangeException(VolumeMessage.VOLUME_OUT.toString(), "0..2.0", value);
        }
        out = value;
        VolumeMessage.VOLUME_OUT.send(getEngine(), getToneIndex(), value);
    }

    public VolumeComponent() {
    }

    @Override
    public void restore() {
        setOut(getOut(true));
    }

}
