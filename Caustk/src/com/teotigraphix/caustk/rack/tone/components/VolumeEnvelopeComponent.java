
package com.teotigraphix.caustk.rack.tone.components;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.VolumeMessage;
import com.teotigraphix.caustk.rack.tone.RackToneComponent;

public class VolumeEnvelopeComponent extends RackToneComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float attack = 0.0f;

    @Tag(101)
    private float decay = 0.0f;

    @Tag(102)
    private float sustain = 1.0f;

    @Tag(103)
    private float release = 0.0f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // attack
    //----------------------------------

    public float getAttack() {
        return attack;
    }

    float getAttack(boolean restore) {
        return VolumeMessage.VOLUME_ATTACK.query(getEngine(), getToneIndex());
    }

    public void setAttack(float value) {
        if (value == attack)
            return;
        if (value < 0 || value > 3.0625f)
            throw newRangeException(VolumeMessage.VOLUME_ATTACK.toString(), "0..3.0625", value);
        attack = value;
        VolumeMessage.VOLUME_ATTACK.send(getEngine(), getToneIndex(), attack);
    }

    //----------------------------------
    // decay
    //----------------------------------

    public float getDecay() {
        return decay;
    }

    float getDecay(boolean restore) {
        return VolumeMessage.VOLUME_DECAY.query(getEngine(), getToneIndex());
    }

    public void setDecay(float value) {
        if (value == decay)
            return;
        if (value < 0 || value > 3.0625f)
            throw newRangeException(VolumeMessage.VOLUME_DECAY.toString(), "0..3.0625", value);
        decay = value;
        VolumeMessage.VOLUME_DECAY.send(getEngine(), getToneIndex(), decay);
    }

    //----------------------------------
    // sustain
    //----------------------------------

    public float getSustain() {
        return sustain;
    }

    float getSustain(boolean restore) {
        return VolumeMessage.VOLUME_SUSTAIN.query(getEngine(), getToneIndex());
    }

    public void setSustain(float value) {
        if (value == sustain)
            return;
        if (value < 0 || value > 1.0f)
            throw newRangeException(VolumeMessage.VOLUME_SUSTAIN.toString(), "0..1.0", value);
        sustain = value;
        VolumeMessage.VOLUME_SUSTAIN.send(getEngine(), getToneIndex(), sustain);
    }

    //----------------------------------
    // release
    //----------------------------------

    public float getRelease() {
        return release;
    }

    float getRelease(boolean restore) {
        return VolumeMessage.VOLUME_RELEASE.query(getEngine(), getToneIndex());
    }

    public void setRelease(float value) {
        if (value == release)
            return;
        if (value < 0 || value > 3.0625f)
            throw newRangeException(VolumeMessage.VOLUME_RELEASE.toString(), "0..3.0625", value);
        release = value;
        VolumeMessage.VOLUME_RELEASE.send(getEngine(), getToneIndex(), release);
    }

    public VolumeEnvelopeComponent() {
    }

    @Override
    public void restore() {
        setAttack(getAttack(true));
        setDecay(getDecay(true));
        setRelease(getRelease(true));
        setSustain(getSustain(true));
    }

}
