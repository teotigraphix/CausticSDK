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

package com.teotigraphix.caustk.tone.components.pcmsynth;

import com.teotigraphix.caustk.core.osc.PCMSynthMessage;
import com.teotigraphix.caustk.tone.ToneComponent;
import com.teotigraphix.caustk.tone.components.subsynth.Osc2Component.Osc2WaveForm;

public class LFO1Component extends ToneComponent {

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
        return (int)PCMSynthMessage.LFO_RATE.query(getEngine(), getToneIndex());
    }

    public void setRate(int value) {
        if (value == rate)
            return;
        if (value < 0 || value > 12)
            throw newRangeException("lfo1_rate", "0..12", value);
        rate = value;
        PCMSynthMessage.LFO_RATE.send(getEngine(), getToneIndex(), rate);
    }

    //----------------------------------
    // depth
    //----------------------------------

    private float depth = 0.0f;

    public float getDepth() {
        return depth;
    }

    float getDepth(boolean restore) {
        return PCMSynthMessage.LFO_DEPTH.query(getEngine(), getToneIndex());
    }

    public void setDepth(float value) {
        if (value == depth)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("lfo1_depth", "0..1", value);
        depth = value;
        PCMSynthMessage.LFO_DEPTH.send(getEngine(), getToneIndex(), depth);
    }

    //----------------------------------
    // target
    //----------------------------------

    private LFOTarget target = LFOTarget.NONE;

    public LFOTarget getTarget() {
        return target;
    }

    LFOTarget getTarget(boolean restore) {
        return LFOTarget.toType(PCMSynthMessage.LFO_TARGET.query(getEngine(), getToneIndex()));
    }

    public void setTarget(LFOTarget value) {
        if (value == target)
            return;
        target = value;
        PCMSynthMessage.LFO_TARGET.send(getEngine(), getToneIndex(), target.getValue());
    }

    //----------------------------------
    // waveform
    //----------------------------------

    private Osc2WaveForm waveform = Osc2WaveForm.SINE;

    public Osc2WaveForm getWaveform() {
        return waveform;
    }

    Osc2WaveForm getWaveform(boolean restore) {
        return Osc2WaveForm.toType(PCMSynthMessage.LFO_WAVEFORM.query(getEngine(), getToneIndex()));
    }

    public void setWaveForm(Osc2WaveForm value) {
        if (value == waveform)
            return;
        waveform = value;
        PCMSynthMessage.LFO_WAVEFORM.send(getEngine(), getToneIndex(), waveform.getValue());
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

    public enum LFOTarget {

        /**
         * No LFO.
         */
        NONE(0),

        /**
         * Pitch oscillation.
         */
        PITCH(1),

        /**
         * Cutoff frequency oscillation.
         */
        CUTOFF(2),

        /**
         * Volume amplitude oscillation.
         */
        VOLUME(3);

        private final int mValue;

        LFOTarget(int value) {
            mValue = value;
        }

        /**
         * Returns int the value of the FLO.
         */
        public int getValue() {
            return mValue;
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
