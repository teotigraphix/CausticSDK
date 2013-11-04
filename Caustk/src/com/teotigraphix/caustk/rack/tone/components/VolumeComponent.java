
package com.teotigraphix.caustk.rack.tone.components;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.VolumeMessage;
import com.teotigraphix.caustk.rack.tone.BeatboxTone;
import com.teotigraphix.caustk.rack.tone.RackToneComponent;

public class VolumeComponent extends RackToneComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    protected float out;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // out
    //----------------------------------

    public float getOut() {
        return out;
    }

    protected float getOut(boolean restore) {
        return VolumeMessage.VOLUME_OUT.query(getEngine(), getToneIndex());
    }

    public void setOut(float value) {
        if (value == out)
            return;
        if (getTone() instanceof BeatboxTone) {
            if (value < 0f || value > 4f)
                throw newRangeException("beatbox out", "0..4", value);
        }
        if (value < 0f || value > 2f)
            throw newRangeException("out", "0..2", value);
        out = value;
        VolumeMessage.VOLUME_OUT.send(getEngine(), getToneIndex(), out);
    }

    public VolumeComponent() {
    }

    @Override
    protected void restoreComponents() {
        setOut(getOut(true));
    }

}
