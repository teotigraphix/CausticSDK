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

package com.teotigraphix.caustk.tone.subsynth;

import com.teotigraphix.caustk.core.osc.SubSynthOscMessage;
import com.teotigraphix.caustk.tone.ToneComponent;

public class Osc2Component extends ToneComponent {

    //--------------------------------------------------------------------------
    // API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // cents
    //----------------------------------

    private int cents = 0;

    public int getCents() {
        return cents;
    }

    int getCents(boolean restore) {
        return (int)SubSynthOscMessage.OSC2_CENTS.query(getEngine(), getToneIndex());
    }

    public void setCents(int value) {
        if (value == cents)
            return;
        if (value < -50 || value > 50)
            throw newRangeException(SubSynthOscMessage.OSC2_CENTS.toString(), "-50..50", value);
        cents = value;
        SubSynthOscMessage.OSC2_CENTS.send(getEngine(), getToneIndex(), cents);
    }

    //----------------------------------
    // octave
    //----------------------------------

    private int octave = 0;

    public int getOctave() {
        return octave;
    }

    int getOctave(boolean restore) {
        return (int)SubSynthOscMessage.OSC2_OCTAVE.query(getEngine(), getToneIndex());
    }

    public void setOctave(int value) {
        if (value == octave)
            return;
        if (value < -3 || value > 3)
            throw newRangeException(SubSynthOscMessage.OSC2_OCTAVE.toString(), "-3..3", value);
        octave = value;
        SubSynthOscMessage.OSC2_OCTAVE.send(getEngine(), getToneIndex(), octave);
    }

    //----------------------------------
    // phase
    //----------------------------------

    private float phase = 0f;

    public float getPhase() {
        return phase;
    }

    float getPhase(boolean restore) {
        return SubSynthOscMessage.OSC2_PHASE.query(getEngine(), getToneIndex());
    }

    public void setPhase(float value) {
        if (value == phase)
            return;
        if (value < -0.5f || value > 0.5f)
            throw newRangeException(SubSynthOscMessage.OSC2_PHASE.toString(), "-0.5..0.5", value);
        phase = value;
        SubSynthOscMessage.OSC2_PHASE.send(getEngine(), getToneIndex(), phase);
    }

    //----------------------------------
    // semis
    //----------------------------------

    private int semis = 0;

    public int getSemis() {
        return semis;
    }

    int getSemis(boolean restore) {
        return (int)SubSynthOscMessage.OSC2_SEMIS.query(getEngine(), getToneIndex());
    }

    public void setSemis(int value) {
        if (value == semis)
            return;
        if (value < -12 || value > 12)
            throw newRangeException(SubSynthOscMessage.OSC2_SEMIS.toString(), "-12..12", value);
        semis = value;
        SubSynthOscMessage.OSC2_SEMIS.send(getEngine(), getToneIndex(), semis);
    }

    //----------------------------------
    // waveForm
    //----------------------------------

    private WaveForm waveForm = WaveForm.NONE;

    public WaveForm getWaveform() {
        return waveForm;
    }

    WaveForm getWaveform(boolean restore) {
        return WaveForm.toType(SubSynthOscMessage.OSC2_WAVEFORM.query(getEngine(), getToneIndex()));
    }

    public void setWaveform(WaveForm value) {
        // XXX OSC NULL
        if (value == null)
            return;
        if (value == waveForm)
            return;
        waveForm = value;
        SubSynthOscMessage.OSC2_WAVEFORM.send(getEngine(), getToneIndex(), waveForm.getValue());
    }

    public Osc2Component() {
    }

    @Override
    public void restore() {
        setCents(getCents(true));
        setOctave(getOctave(true));
        setPhase(getPhase(true));
        setSemis(getSemis(true));
        setWaveform(getWaveform(true));
    }

    public enum WaveForm {

        /**
         * Inactive oscillator.
         */
        NONE(0),

        /**
         * A sine wave (1).
         */
        SINE(1),

        /**
         * A saw wave (2).
         */
        SAW(2),

        /**
         * A triangle wave (3).
         */
        TRIANGLE(3),

        /**
         * A square wave (4).
         */
        SQUARE(4),

        /**
         * A noise wave (5).
         */
        NOISE(5),

        /**
         * A custom wave (6) <strong>N/A</strong>.
         */
        CUSTOM(6);

        private final int mValue;

        /**
         * Returns in integer value for the {@link WaveForm}.
         */
        public int getValue() {
            return mValue;
        }

        WaveForm(int value) {
            mValue = value;
        }

        /**
         * Returns a Waveform based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static WaveForm toType(Integer type) {
            for (WaveForm result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see #toType(Integer)
         */
        public static WaveForm toType(Float type) {
            return toType(type.intValue());
        }
    }
}
