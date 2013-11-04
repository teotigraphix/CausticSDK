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

package com.teotigraphix.caustk.rack.tone.components.bassline;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.BasslineMessage;
import com.teotigraphix.caustk.rack.tone.RackToneComponent;

public class LFO1Component extends RackToneComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    protected int rate = 1;

    @Tag(101)
    private float depth = 0.0f;

    @Tag(102)
    private float phase = 0f;

    @Tag(103)
    private LFOTarget target = LFOTarget.OFF;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // rate
    //----------------------------------

    public int getRate() {
        return rate;
    }

    int getRate(boolean restore) {
        return (int)BasslineMessage.LFO_RATE.query(getEngine(), getToneIndex());
    }

    public void setRate(int value) {
        if (value == rate)
            return;
        if (value < 0 || value > 12)
            throw newRangeException("lfo1_rate", "0..12", value);
        rate = value;
        BasslineMessage.LFO_RATE.send(getEngine(), getToneIndex(), rate);
    }

    //----------------------------------
    // depth
    //----------------------------------

    public float getDepth() {
        return depth;
    }

    float getDepth(boolean restore) {
        return BasslineMessage.LFO_DEPTH.query(getEngine(), getToneIndex());
    }

    public void setDepth(float value) {
        if (value == depth)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("lfo1_depth", "0..1", value);
        depth = value;
        BasslineMessage.LFO_DEPTH.send(getEngine(), getToneIndex(), depth);
    }

    //----------------------------------
    // phase
    //----------------------------------

    public float getPhase() {
        return phase;
    }

    float getPhase(boolean restore) {
        return BasslineMessage.LFO_PHASE.query(getEngine(), getToneIndex());
    }

    public void setPhase(float value) {
        if (value == phase)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(BasslineMessage.LFO_PHASE.toString(), "0..1", value);
        phase = value;
        BasslineMessage.LFO_PHASE.send(getEngine(), getToneIndex(), phase);
    }

    //----------------------------------
    // target
    //----------------------------------

    public LFOTarget getTarget() {
        return target;
    }

    LFOTarget getTarget(boolean restore) {
        return LFOTarget.toType(BasslineMessage.LFO_TARGET.query(getEngine(), getToneIndex()));
    }

    public void setTarget(LFOTarget value) {
        if (value == target)
            return;
        target = value;
        BasslineMessage.LFO_TARGET.send(getEngine(), getToneIndex(), target.getValue());
    }

    public LFO1Component() {
        rate = 0;
    }

    @Override
    protected void updateComponents() {
        BasslineMessage.LFO_DEPTH.send(getEngine(), getToneIndex(), depth);
        BasslineMessage.LFO_RATE.send(getEngine(), getToneIndex(), rate);
        BasslineMessage.LFO_PHASE.send(getEngine(), getToneIndex(), phase);
        BasslineMessage.LFO_TARGET.send(getEngine(), getToneIndex(), target.getValue());
    }

    @Override
    public void restoreComponents() {
        setDepth(getDepth(true));
        setRate(getRate(true));
        setPhase(getPhase(true));
        setTarget(getTarget(true));
    }

    /**
     * The {@link IBasslineLFO1#getTarget()} LFO value.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum LFOTarget {

        /**
         * No lfo on the bassline oscillator.
         */
        OFF(0),

        /**
         * Pulse Width Modulation.
         */
        PWM(1),

        /**
         * Voltage Control Frequency.
         */
        VCF(2),

        /**
         * Volume Controled Frequency.
         */
        VCA(3);

        private final int value;

        LFOTarget(int type) {
            value = type;
        }

        /**
         * Returns the int value for the lfo.
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns a {@link LFOTarget} based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static LFOTarget toType(Integer type) {
            for (LFOTarget result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see LFOTarget#toType(Integer)
         */
        public static LFOTarget toType(Float type) {
            return toType(type.intValue());
        }
    }
}
