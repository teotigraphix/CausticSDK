
package com.teotigraphix.caustk.rack.tone.components.padsynth;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.PadSynthMessage;
import com.teotigraphix.caustk.rack.tone.ToneComponent;

public class LFO1Component extends ToneComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    protected Target target = Target.Off;

    @Tag(101)
    protected int rate = 6;

    @Tag(102)
    protected float depth;

    @Tag(103)
    protected float phase;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // target
    //----------------------------------

    public Target getTarget() {
        return target;
    }

    Target getTarget(boolean restore) {
        return Target.fromInt(PadSynthMessage.LFO1_TARGET.query(getEngine(), getToneIndex()));
    }

    public void setTarget(Target value) {
        if (value == target)
            return;
        target = value;
        PadSynthMessage.LFO1_TARGET.send(getEngine(), getToneIndex(), target.getValue());
    }

    //----------------------------------
    // rate
    //----------------------------------

    public int getRate() {
        return rate;
    }

    int getRate(boolean restore) {
        return (int)PadSynthMessage.LFO1_RATE.query(getEngine(), getToneIndex());
    }

    public void setRate(int value) {
        if (value == rate)
            return;
        if (value < 0 || value > 12)
            throw newRangeException("lfo1_rate", "0..12", value);
        rate = value;
        PadSynthMessage.LFO1_RATE.send(getEngine(), getToneIndex(), rate);
    }

    //----------------------------------
    // depth
    //----------------------------------

    public float getDepth() {
        return depth;
    }

    float getDepth(boolean restore) {
        return PadSynthMessage.LFO1_DEPTH.query(getEngine(), getToneIndex());
    }

    public void setDepth(float value) {
        if (value == depth)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("lfo1_depth", "0..1", value);
        depth = value;
        PadSynthMessage.LFO1_DEPTH.send(getEngine(), getToneIndex(), depth);
    }

    //----------------------------------
    // phase
    //----------------------------------

    public float getPhase() {
        return phase;
    }

    float getPhase(boolean restore) {
        return PadSynthMessage.LFO1_PHASE.query(getEngine(), getToneIndex());
    }

    public void setPhase(float value) {
        if (value == phase)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("lfo1_phase", "0..1", value);
        phase = value;
        PadSynthMessage.LFO1_PHASE.send(getEngine(), getToneIndex(), phase);
    }

    public LFO1Component() {
    }

    @Override
    public void restore() {
        setDepth(getDepth(true));
        setPhase(getPhase(true));
        setRate(getRate(true));
        setTarget(getTarget(true));
    }

    public enum Target {
        Off(0), Morph(1), Pitch(2), Volume(3);

        private int value;

        public int getValue() {
            return value;
        }

        Target(int value) {
            this.value = value;
        }

        public static Target fromInt(float value) {
            for (Target target : values()) {
                if (target.getValue() == value)
                    return target;
            }
            return null;
        }
    }
}
