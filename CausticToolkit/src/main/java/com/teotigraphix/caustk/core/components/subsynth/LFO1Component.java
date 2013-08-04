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

package com.teotigraphix.caustk.core.components.subsynth;

import com.teotigraphix.caustk.core.components.LFOComponentBase;
import com.teotigraphix.caustk.core.components.subsynth.Osc2Component.WaveForm;
import com.teotigraphix.caustk.core.osc.SubSynthLFOMessage;

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
        return LFOTarget.toType(SubSynthLFOMessage.LFO1_TARGET.query(getEngine(), getToneIndex()));
    }

    public void setTarget(LFOTarget value) {
        if (value == target)
            return;
        target = value;
        SubSynthLFOMessage.LFO1_TARGET.send(getEngine(), getToneIndex(), target.getValue());
    }

    //----------------------------------
    // waveForm
    //----------------------------------

    private WaveForm waveForm = WaveForm.SINE;

    public WaveForm getWaveform() {
        return waveForm;
    }

    WaveForm getWaveform(boolean restore) {
        return WaveForm.toType(SubSynthLFOMessage.LFO1_WAVEFORM.query(getEngine(), getToneIndex()));
    }

    public void setWaveForm(WaveForm value) {
        if (value == waveForm)
            return;
        waveForm = value;
        SubSynthLFOMessage.LFO1_WAVEFORM.send(getEngine(), getToneIndex(), waveForm.getValue());
    }

    public LFO1Component() {
        depthMessage = SubSynthLFOMessage.LFO1_DEPTH;
        rateMessage = SubSynthLFOMessage.LFO1_RATE;
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

        LFOTarget(int value) {
            mValue = value;
        }

        /**
         * Returns the int value of the lfo.
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
