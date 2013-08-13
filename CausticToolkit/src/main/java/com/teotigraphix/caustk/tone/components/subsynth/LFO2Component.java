////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.tone.components.subsynth;

import com.teotigraphix.caustk.core.osc.SubSynthMessage;
import com.teotigraphix.caustk.tone.ToneComponent;

public class LFO2Component extends ToneComponent {

    //--------------------------------------------------------------------------
    // API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // rate
    //----------------------------------

    protected int rate = 1;

    public int getRate() {
        return rate;
    }

    int getRate(boolean restore) {
        return (int)SubSynthMessage.LFO1_RATE.query(getEngine(), getToneIndex());
    }

    public void setRate(int value) {
        if (value == rate)
            return;
        if (value < 0 || value > 12)
            throw newRangeException("lfo1_rate", "0..12", value);
        rate = value;
        SubSynthMessage.LFO1_RATE.send(getEngine(), getToneIndex(), rate);
    }

    //----------------------------------
    // depth
    //----------------------------------

    private float depth = 0.0f;

    public float getDepth() {
        return depth;
    }

    float getDepth(boolean restore) {
        return SubSynthMessage.LFO1_DEPTH.query(getEngine(), getToneIndex());
    }

    public void setDepth(float value) {
        if (value == depth)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("lfo1_depth", "0..1", value);
        depth = value;
        SubSynthMessage.LFO1_DEPTH.send(getEngine(), getToneIndex(), depth);
    }

    //----------------------------------
    // target
    //----------------------------------

    private LFO2Target target = LFO2Target.NONE;

    public LFO2Target getTarget() {
        return target;
    }

    LFO2Target getTarget(boolean restore) {
        return LFO2Target.toType(SubSynthMessage.LFO2_TARGET.query(getEngine(), getToneIndex()));
    }

    public void setTarget(LFO2Target value) {
        if (value == target)
            return;
        target = value;
        SubSynthMessage.LFO2_TARGET.send(getEngine(), getToneIndex(), target.getValue());
    }

    public LFO2Component() {
    }

    @Override
    public void restore() {
        setDepth(getDepth(true));
        setRate(getRate(true));
        setTarget(getTarget(true));
    }

    public enum LFO2Target {

        /**
         * No LFO.
         */
        NONE(0),

        /**
         * Primary oscillation.
         */
        OSC_PRIMARY(1),

        /**
         * Secondary oscillation.
         */
        OSC_SECONDARY(2),

        /**
         * Primary and secondary oscillation.
         */
        OSC_PRIMARY_SECONDARY(3),

        /**
         * Phase oscillation.
         */
        PHASE(4),

        /**
         * Cutoff oscillation.
         */
        CUTOFF(5),

        /**
         * Volume oscillation.
         */
        VOLUME(6),

        /**
         * Octave oscillation.
         */
        OCTAVE(7),

        /**
         * Semitone oscillation.
         */
        SEMIS(8);

        private final int mValue;

        LFO2Target(int value) {
            mValue = value;
        }

        /**
         * Returns the int value of the lof.
         */
        public int getValue() {
            return mValue;
        }

        /**
         * Returns a {@link LFO2Target} based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static LFO2Target toType(Integer type) {
            for (LFO2Target result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see LFO2Target#toType(Integer)
         */
        public static LFO2Target toType(Float type) {
            return toType(type.intValue());
        }
    }
}
