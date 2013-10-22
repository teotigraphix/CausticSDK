
package com.teotigraphix.caustk.rack.tone.components.padsynth;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.PadSynthMessage;

public class VolumeComponent extends com.teotigraphix.caustk.rack.tone.components.VolumeComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    protected float gain1;

    @Tag(101)
    protected float gain2;

    @Tag(102)
    protected float attack;

    @Tag(103)
    protected float decay;

    @Tag(104)
    protected float sustain;

    @Tag(105)
    protected float release;

    @Tag(106)
    protected float out;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // gain1
    //----------------------------------

    public float getGain1() {
        return gain1;
    }

    float getGain1(boolean restore) {
        return PadSynthMessage.GAIN1.query(getEngine(), getToneIndex());
    }

    public void setGain1(float value) {
        if (value == gain1)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("gain1", "0..1", value);
        gain1 = value;
        PadSynthMessage.GAIN1.send(getEngine(), getToneIndex(), gain1);
    }

    //----------------------------------
    // gain2
    //----------------------------------

    public float getGain2() {
        return gain2;
    }

    float getGain2(boolean restore) {
        return PadSynthMessage.GAIN2.query(getEngine(), getToneIndex());
    }

    public void setGain2(float value) {
        if (value == gain2)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("gain2", "0..1", value);
        gain2 = value;
        PadSynthMessage.GAIN2.send(getEngine(), getToneIndex(), gain2);
    }

    //----------------------------------
    // attack
    //----------------------------------

    public float getAttack() {
        return attack;
    }

    float getAttack(boolean restore) {
        return PadSynthMessage.VOLUME_ATTACK.query(getEngine(), getToneIndex());
    }

    public void setAttack(float value) {
        if (value == attack)
            return;
        if (value < 0f || value > 3f)
            throw newRangeException("volume_attack", "0..3", value);
        attack = value;
        PadSynthMessage.VOLUME_ATTACK.send(getEngine(), getToneIndex(), attack);
    }

    //----------------------------------
    // decay
    //----------------------------------

    public float getDecay() {
        return decay;
    }

    float getDecay(boolean restore) {
        return PadSynthMessage.VOLUME_DECAY.query(getEngine(), getToneIndex());
    }

    public void setDecay(float value) {
        if (value == decay)
            return;
        if (value < 0f || value > 3f)
            throw newRangeException("volume_decay", "0..3", value);
        decay = value;
        PadSynthMessage.VOLUME_DECAY.send(getEngine(), getToneIndex(), decay);
    }

    //----------------------------------
    // sustain
    //----------------------------------

    public float getSustain() {
        return sustain;
    }

    float getSustain(boolean restore) {
        return PadSynthMessage.VOLUME_SUSTAIN.query(getEngine(), getToneIndex());
    }

    public void setSustain(float value) {
        if (value == sustain)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("volume_sustain", "0..1", value);
        sustain = value;
        PadSynthMessage.VOLUME_SUSTAIN.send(getEngine(), getToneIndex(), sustain);
    }

    //----------------------------------
    // release
    //----------------------------------

    public float getRelease() {
        return release;
    }

    float getRelease(boolean restore) {
        return PadSynthMessage.VOLUME_RELEASE.query(getEngine(), getToneIndex());
    }

    public void setRelease(float value) {
        if (value == release)
            return;
        if (value < 0f || value > 3f)
            throw newRangeException("volume_release", "0..3", value);
        release = value;
        PadSynthMessage.VOLUME_RELEASE.send(getEngine(), getToneIndex(), release);
    }

    //----------------------------------
    // out
    //----------------------------------

    @Override
    public float getOut() {
        return out;
    }

    @Override
    protected float getOut(boolean restore) {
        return PadSynthMessage.VOLUME_OUT.query(getEngine(), getToneIndex());
    }

    @Override
    public void setOut(float value) {
        if (value == out)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException("out", "0..2", value);
        out = value;
        PadSynthMessage.VOLUME_OUT.send(getEngine(), getToneIndex(), out);
    }

    public VolumeComponent() {
    }

    @Override
    public void restore() {
        setAttack(getAttack(true));
        setDecay(getDecay(true));
        setGain1(getGain1(true));
        setGain2(getGain2(true));
        setOut(getOut(true));
        setRelease(getRelease(true));
        setSustain(getSustain(true));
    }

}
