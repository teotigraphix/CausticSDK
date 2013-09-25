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

public class Osc2Component extends ToneComponent {

    private static final long serialVersionUID = 716026572761466394L;

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
        return (int)SubSynthMessage.OSC2_CENTS.query(getEngine(), getToneIndex());
    }

    public void setCents(int value) {
        if (value == cents)
            return;
        if (value < -50 || value > 50)
            throw newRangeException(SubSynthMessage.OSC2_CENTS.toString(), "-50..50", value);
        cents = value;
        SubSynthMessage.OSC2_CENTS.send(getEngine(), getToneIndex(), cents);
    }

    //----------------------------------
    // octave
    //----------------------------------

    private int octave = 0;

    public int getOctave() {
        return octave;
    }

    int getOctave(boolean restore) {
        return (int)SubSynthMessage.OSC2_OCTAVE.query(getEngine(), getToneIndex());
    }

    public void setOctave(int value) {
        if (value == octave)
            return;
        if (value < -3 || value > 3)
            throw newRangeException(SubSynthMessage.OSC2_OCTAVE.toString(), "-3..3", value);
        octave = value;
        SubSynthMessage.OSC2_OCTAVE.send(getEngine(), getToneIndex(), octave);
    }

    //----------------------------------
    // phase
    //----------------------------------

    private float phase = 0f;

    public float getPhase() {
        return phase;
    }

    float getPhase(boolean restore) {
        return SubSynthMessage.OSC2_PHASE.query(getEngine(), getToneIndex());
    }

    public void setPhase(float value) {
        if (value == phase)
            return;
        if (value < -0.5f || value > 0.5f)
            throw newRangeException(SubSynthMessage.OSC2_PHASE.toString(), "-0.5..0.5", value);
        phase = value;
        SubSynthMessage.OSC2_PHASE.send(getEngine(), getToneIndex(), phase);
    }

    //----------------------------------
    // semis
    //----------------------------------

    private int semis = 0;

    public int getSemis() {
        return semis;
    }

    int getSemis(boolean restore) {
        return (int)SubSynthMessage.OSC2_SEMIS.query(getEngine(), getToneIndex());
    }

    public void setSemis(int value) {
        if (value == semis)
            return;
        if (value < -12 || value > 12)
            throw newRangeException(SubSynthMessage.OSC2_SEMIS.toString(), "-12..12", value);
        semis = value;
        SubSynthMessage.OSC2_SEMIS.send(getEngine(), getToneIndex(), semis);
    }

    //----------------------------------
    // centsMode
    //----------------------------------

    private CentsMode centsMode = CentsMode.CENTS;

    public CentsMode getCentsMode() {
        return centsMode;
    }

    CentsMode getCentsMode(boolean restore) {
        return CentsMode.toType((int)SubSynthMessage.OSC2_CENTS_MODE.query(getEngine(),
                getToneIndex()));
    }

    public void setCentsMode(CentsMode value) {
        if (value == centsMode)
            return;
        centsMode = value;
        SubSynthMessage.OSC2_CENTS_MODE.send(getEngine(), getToneIndex(), centsMode.getValue());
    }

    //----------------------------------
    // waveForm
    //----------------------------------

    private Osc2WaveForm waveForm = Osc2WaveForm.NONE;

    public Osc2WaveForm getWaveform() {
        return waveForm;
    }

    Osc2WaveForm getWaveform(boolean restore) {
        return Osc2WaveForm
                .toType(SubSynthMessage.OSC2_WAVEFORM.query(getEngine(), getToneIndex()));
    }

    public void setWaveform(Osc2WaveForm value) {
        if (value == waveForm)
            return;
        waveForm = value;
        SubSynthMessage.OSC2_WAVEFORM.send(getEngine(), getToneIndex(), waveForm.getValue());
    }

    public Osc2Component() {
    }

    @Override
    public void restore() {
        setCents(getCents(true));
        setCentsMode(getCentsMode(true));
        setOctave(getOctave(true));
        setPhase(getPhase(true));
        setSemis(getSemis(true));
        setWaveform(getWaveform(true));
    }

    public enum CentsMode {
        CENTS(0),

        UNISON(1);

        private final int value;

        /**
         * Returns in integer value for the {@link Osc2WaveForm}.
         */
        public int getValue() {
            return value;
        }

        CentsMode(int value) {
            this.value = value;
        }

        public static CentsMode toType(Integer type) {
            for (CentsMode result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }
    }

    public enum Osc2WaveForm {

        NONE(0),

        SINE(1),

        TRIANGLE(2),

        SAW(3),

        SAW_HQ(4),

        SQUARE(5),

        SQUARE_HQ(6),

        NOISE(7),

        CUSTOM1(8),

        CUSTOM2(9);

        private final int value;

        /**
         * Returns in integer value for the {@link Osc2WaveForm}.
         */
        public int getValue() {
            return value;
        }

        Osc2WaveForm(int value) {
            this.value = value;
        }

        /**
         * Returns a Waveform based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static Osc2WaveForm toType(Integer type) {
            for (Osc2WaveForm result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see #toType(Integer)
         */
        public static Osc2WaveForm toType(Float type) {
            return toType(type.intValue());
        }
    }
}
