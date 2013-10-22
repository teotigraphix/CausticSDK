
package com.teotigraphix.caustk.rack.tone.components.padsynth;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.PadSynthMessage;
import com.teotigraphix.caustk.rack.tone.ToneComponent;

public class MorphComponent extends ToneComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float blend;

    @Tag(101)
    private int evelopeEnabled = 0;

    @Tag(102)
    private float attack;

    @Tag(103)
    private float decay;

    @Tag(104)
    private float sustain;

    @Tag(105)
    private float release;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // blend
    //----------------------------------

    public float getBlend() {
        return blend;
    }

    float getBlend(boolean restore) {
        return PadSynthMessage.MORPH.query(getEngine(), getToneIndex());
    }

    public void setBlend(float value) {
        if (value == blend)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("morph", "0..1", value);
        blend = value;
        PadSynthMessage.MORPH.send(getEngine(), getToneIndex(), blend);
    }

    //----------------------------------
    // evelopeEnabled
    //----------------------------------

    public int getEnvelopeEnabled() {
        return evelopeEnabled;
    }

    int getEnvelopeEnabled(boolean restore) {
        return (int)PadSynthMessage.MORPH_ENV.query(getEngine(), getToneIndex());
    }

    public void setEnvelopeEnabled(int value) {
        if (value == evelopeEnabled)
            return;
        if (value < 0 || value > 1)
            throw newRangeException("morph_env", "0,1", value);
        evelopeEnabled = value;
        PadSynthMessage.MORPH_ENV.send(getEngine(), getToneIndex(), evelopeEnabled);
    }

    //----------------------------------
    // attack
    //----------------------------------

    public float getAttack() {
        return attack;
    }

    float getAttack(boolean restore) {
        return PadSynthMessage.MORPH_ATTACK.query(getEngine(), getToneIndex());
    }

    public void setAttack(float value) {
        if (value == attack)
            return;
        if (value < 0f || value > 3f)
            throw newRangeException("morph_attack", "0..3", value);
        attack = value;
        PadSynthMessage.MORPH_ATTACK.send(getEngine(), getToneIndex(), attack);
    }

    //----------------------------------
    // decay
    //----------------------------------

    public float getDecay() {
        return decay;
    }

    float getDecay(boolean restore) {
        return PadSynthMessage.MORPH_DECAY.query(getEngine(), getToneIndex());
    }

    public void setDecay(float value) {
        if (value == decay)
            return;
        if (value < 0f || value > 3f)
            throw newRangeException("morph_decay", "0..3", value);
        decay = value;
        PadSynthMessage.MORPH_DECAY.send(getEngine(), getToneIndex(), decay);
    }

    //----------------------------------
    // sustain
    //----------------------------------

    public float getSustain() {
        return sustain;
    }

    float getSustain(boolean restore) {
        return PadSynthMessage.MORPH_SUSTAIN.query(getEngine(), getToneIndex());
    }

    public void setSustain(float value) {
        if (value == sustain)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("morph_sustain", "0..1", value);
        sustain = value;
        PadSynthMessage.MORPH_SUSTAIN.send(getEngine(), getToneIndex(), sustain);
    }

    //----------------------------------
    // release
    //----------------------------------

    public float getRelease() {
        return release;
    }

    float getRelease(boolean restore) {
        return PadSynthMessage.MORPH_RELEASE.query(getEngine(), getToneIndex());
    }

    public void setRelease(float value) {
        if (value == release)
            return;
        if (value < 0f || value > 3f)
            throw newRangeException("morph_release", "0..3", value);
        release = value;
        PadSynthMessage.MORPH_RELEASE.send(getEngine(), getToneIndex(), release);
    }

    public MorphComponent() {
    }

    @Override
    public void restore() {
        setAttack(getAttack(true));
        setBlend(getBlend(true));
        setDecay(getDecay(true));
        setEnvelopeEnabled(getEnvelopeEnabled(true));
        setRelease(getRelease(true));
        setSustain(getSustain(true));
    }

}
