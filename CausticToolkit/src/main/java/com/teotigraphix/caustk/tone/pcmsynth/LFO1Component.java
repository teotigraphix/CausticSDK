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

package com.teotigraphix.caustk.tone.pcmsynth;

import com.teotigraphix.caustk.core.components.LFOComponentBase;
import com.teotigraphix.caustk.core.osc.PCMSynthLFOMessage;
import com.teotigraphix.caustk.tone.subsynth.Osc2Component.WaveForm;

public class LFO1Component extends LFOComponentBase {

    //--------------------------------------------------------------------------
    // API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // target
    //----------------------------------

    private LFOTarget target = LFOTarget.NONE;

    public LFOTarget getTarget() {
        return target;
    }

    LFOTarget getTarget(boolean restore) {
        return LFOTarget.toType(PCMSynthLFOMessage.LFO_TARGET.query(getEngine(), getToneIndex()));
    }

    public void setTarget(LFOTarget value) {
        if (value == target)
            return;
        target = value;
        PCMSynthLFOMessage.LFO_TARGET.send(getEngine(), getToneIndex(), target.getValue());
    }

    //----------------------------------
    // waveform
    //----------------------------------

    private WaveForm waveform = WaveForm.SINE;

    public WaveForm getWaveform() {
        return waveform;
    }

    WaveForm getWaveform(boolean restore) {
        return WaveForm.toType(PCMSynthLFOMessage.LFO_WAVEFORM.query(getEngine(), getToneIndex()));
    }

    public void setWaveForm(WaveForm value) {
        if (value == waveform)
            return;
        waveform = value;
        PCMSynthLFOMessage.LFO_WAVEFORM.send(getEngine(), getToneIndex(), waveform.getValue());
    }

    public LFO1Component() {
        depthMessage = PCMSynthLFOMessage.LFO_DEPTH;
        rateMessage = PCMSynthLFOMessage.LFO_RATE;
    }

    @Override
    public void restore() {
        super.restore();
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
