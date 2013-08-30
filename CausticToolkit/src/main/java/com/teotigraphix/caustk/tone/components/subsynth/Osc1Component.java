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

public class Osc1Component extends ToneComponent {

    //--------------------------------------------------------------------------
    //
    // ISubSynthOsc1 API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // bend
    //----------------------------------

    private float bend = 0.0f;

    public float getBend() {
        return bend;
    }

    float getBend(boolean restore) {
        return SubSynthMessage.OSC_BEND.query(getEngine(), getToneIndex());
    }

    public void setBend(float value) {
        if (value == bend)
            return;
        if (value < 0 || value > 1f)
            throw newRangeException(SubSynthMessage.OSC_BEND.toString(), "0..1", value);
        bend = value;
        SubSynthMessage.OSC_BEND.send(getEngine(), getToneIndex(), bend);
    }

    //----------------------------------
    // fm
    //----------------------------------

    private float fm = 0.0f;

    public float getFM() {
        return fm;
    }

    float getFM(boolean restore) {
        return SubSynthMessage.OSC1_FM.query(getEngine(), getToneIndex());
    }

    // XXX SunSYnth fix 
    public void setFM(float value) {
        if (value == fm)
            return;
        //if (value < 0 || value > 1f)
        //    throw newRangeException(SubSynthMessage.OSC1_FM.toString(), "0..1", value);
        fm = value;
        //SubSynthMessage.OSC1_FM.send(getEngine(), getToneIndex(), fm);
    }

    //----------------------------------
    // mix
    //----------------------------------

    private float mix = 0.5f;

    public float getMix() {
        return mix;
    }

    float getMix(boolean restore) {
        return SubSynthMessage.OSC_MIX.query(getEngine(), getToneIndex());
    }

    public void setMix(float value) {
        if (value == mix)
            return;
        if (value < 0 || value > 1.0f)
            throw newRangeException(SubSynthMessage.OSC_MIX.toString(), "0..1", value);
        mix = value;
        SubSynthMessage.OSC_MIX.send(getEngine(), getToneIndex(), mix);
    }

    //----------------------------------
    // waveform
    //----------------------------------

    private Osc1Waveform waveform = Osc1Waveform.SINE;

    public Osc1Waveform getWaveform() {
        return waveform;
    }

    Osc1Waveform getWaveform(boolean restore) {
        return Osc1Waveform
                .toType(SubSynthMessage.OSC1_WAVEFORM.query(getEngine(), getToneIndex()));
    }

    public void setWaveform(Osc1Waveform value) {
        // XXX OSC ERROR
        if (value == null)
            return;
        if (value == waveform)
            return;
        waveform = value;
        SubSynthMessage.OSC1_WAVEFORM.send(getEngine(), getToneIndex(), waveform.getValue());
    }

    public Osc1Component() {
    }

    @Override
    public void restore() {
        setBend(getBend(true));
        setFM(getFM(true));
        setMix(getMix(true));
        setWaveform(getWaveform(true));
    }

    /**
     * The {@link ISubSynthOsc1} waveforms.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum Osc1Waveform {

        /**
         * A sine wave (0).
         */
        SINE(0),

        /**
         * A saw tooth wave (1).
         */
        SAW(1),

        /**
         * A triangle wave (2).
         */
        TRIANGLE(2),

        /**
         * A square wave (3).
         */
        SQUARE(3),

        /**
         * A noise wave (4).
         */
        NOISE(4),

        /**
         * N/A. (5)
         */
        CUSTOM(5);

        private final int mValue;

        /**
         * Returns the integer value of the {@link Osc1Waveform}.
         */
        public int getValue() {
            return mValue;
        }

        Osc1Waveform(int value) {
            mValue = value;
        }

        /**
         * Returns a {@link Osc1Waveform} based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static Osc1Waveform toType(Integer type) {
            for (Osc1Waveform result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see #toType(Integer)
         */
        public static Osc1Waveform toType(Float type) {
            return toType(type.intValue());
        }
    }
}
