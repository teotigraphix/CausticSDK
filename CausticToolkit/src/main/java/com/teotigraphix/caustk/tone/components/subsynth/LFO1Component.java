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
import com.teotigraphix.caustk.tone.components.subsynth.Osc2Component.Osc2WaveForm;

public class LFO1Component extends ToneComponent { //LFOComponentBase {

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
        //        if (value < 0 || value > 12)
        //            throw newRangeException("lfo1_rate", "0..12", value);
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

    private LFO1Target target = LFO1Target.NONE;

    public LFO1Target getTarget() {
        return target;
    }

    LFO1Target getTarget(boolean restore) {
        return LFO1Target.toType(SubSynthMessage.LFO1_TARGET.query(getEngine(), getToneIndex()));
    }

    public void setTarget(LFO1Target value) {
        if (value == target)
            return;
        target = value;
        SubSynthMessage.LFO1_TARGET.send(getEngine(), getToneIndex(), target.getValue());
    }

    //----------------------------------
    // waveForm
    //----------------------------------

    private Osc2WaveForm waveForm = Osc2WaveForm.SINE;

    public Osc2WaveForm getWaveform() {
        return waveForm;
    }

    Osc2WaveForm getWaveform(boolean restore) {
        return Osc2WaveForm
                .toType(SubSynthMessage.LFO1_WAVEFORM.query(getEngine(), getToneIndex()));
    }

    public void setWaveForm(Osc2WaveForm value) {
        if (value == waveForm)
            return;
        waveForm = value;
        SubSynthMessage.LFO1_WAVEFORM.send(getEngine(), getToneIndex(), waveForm.getValue());
    }

    public LFO1Component() {
    }

    @Override
    public void restore() {
        setDepth(getDepth(true));
        setRate(getRate(true));
        setTarget(getTarget(true));
        setWaveForm(getWaveform(true));
    }

    public enum LFO1Target {

        NONE(0),

        OSC1(1),

        OSC2(2),

        OSC1PLUS2(3),

        PHASE(4),

        CUTOFF(5),

        VOLUME(6),

        OCTAVE(7),

        SEMIS(8),

        OSC1MOD(9);

        private final int value;

        LFO1Target(int value) {
            this.value = value;
        }

        /**
         * Returns the int value of the lfo.
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns a {@link LFO1Target} based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static LFO1Target toType(Integer type) {
            for (LFO1Target result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see LFO1Target#toType(Integer)
         */
        public static LFO1Target toType(Float type) {
            return toType(type.intValue());
        }
    }
}
