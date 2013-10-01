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

package com.teotigraphix.caustk.tone.components.modular;

import com.teotigraphix.caustk.controller.ICaustkController;

public class Oscillator extends ModularComponentBase {

    private static final long serialVersionUID = -4868841470853676163L;

    //----------------------------------
    // waveform
    //----------------------------------

    private float waveform;

    public float getWaveform() {
        return waveform;
    }

    float getWaveform(boolean restore) {
        return getValue("waveform");
    }

    /**
     * @param value (0..1) 0 is Sine, 0.333 is Triangle, 0.666 is Saw and 1.0 is
     *            Square
     */
    public void setWaveform(float value) {
        if (value == waveform)
            return;
        waveform = value;
        if (value < 0f || value > 1f)
            newRangeException("waveform", "0..1", value);
        setValue("waveform", value);
    }

    //----------------------------------
    // octave
    //----------------------------------

    private int octave;

    public int getOctave() {
        return octave;
    }

    int getOctave(boolean restore) {
        return (int)getValue("octave");
    }

    /**
     * @param value (-4..4)
     */
    public void setOctave(int value) {
        if (value == octave)
            return;
        octave = value;
        if (value < -4 || value > 4)
            newRangeException("octave", "-4..4", value);
        setValue("octave", value);
    }

    //----------------------------------
    // semis
    //----------------------------------

    private int semis;

    public int getSemis() {
        return semis;
    }

    int getSemis(boolean restore) {
        return (int)getValue("semis");
    }

    /**
     * @param value (-6..6)
     */
    public void setSemis(int value) {
        if (value == semis)
            return;
        semis = value;
        if (value < -6 || value > 6)
            newRangeException("semis", "-6..6", value);
        setValue("semis", value);
    }

    //----------------------------------
    // cents
    //----------------------------------

    private int cents;

    public int getCents() {
        return cents;
    }

    int getCents(boolean restore) {
        return (int)getValue("cents");
    }

    /**
     * @param value (-50..50)
     */
    public void setCents(int value) {
        if (value == cents)
            return;
        cents = value;
        if (value < -50 || value > 50)
            newRangeException("cents", "-50..50", value);
        setValue("cents", value);
    }

    //----------------------------------
    // fm
    //----------------------------------

    private float fm;

    public float getFM() {
        return fm;
    }

    float getFM(boolean restore) {
        return getValue("fm_mod");
    }

    /**
     * @param value (0..1)
     */
    public void setFM(float value) {
        if (value == fm)
            return;
        fm = value;
        if (value < 0f || value > 1f)
            newRangeException("fm_mod", "0..1", value);
        setValue("fm_mod", value);
    }

    //----------------------------------
    // am
    //----------------------------------

    private float am;

    public float getAM() {
        return am;
    }

    float getAM(boolean restore) {
        return getValue("am_mod");
    }

    /**
     * @param value (0..1)
     */
    public void setAM(float value) {
        if (value == am)
            return;
        am = value;
        if (value < 0f || value > 1f)
            newRangeException("am_mod", "0..1", value);
        setValue("am_mod", value);
    }

    //----------------------------------
    // pitch
    //----------------------------------

    private float pitch;

    public float getPitch() {
        return pitch;
    }

    float getPitch(boolean restore) {
        return getValue("pitch_mod");
    }

    /**
     * @param value (0..1)
     */
    public void setPitch(float value) {
        if (value == pitch)
            return;
        pitch = value;
        if (value < 0f || value > 1f)
            newRangeException("pitch_mod", "0..1", value);
        setValue("pitch_mod", value);
    }

    //----------------------------------
    // outGain
    //----------------------------------

    private float outGain;

    public float getOutGain() {
        return outGain;
    }

    float getOutGain(boolean restore) {
        return getValue("out_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setOutGain(float value) {
        if (value == outGain)
            return;
        outGain = value;
        if (value < 0f || value > 1f)
            newRangeException("out_gain", "0..1", value);
        setValue("out_gain", value);
    }

    public Oscillator() {
    }

    public Oscillator(ICaustkController controller, int bay) {
        super(controller, bay);
    }

    @Override
    protected int getNumBays() {
        return 2;
    }

    public enum OscillatorJack implements IModularJack {

        InNote(0),

        InModulation(1),

        InFM(2),

        InAM(3),

        InPitch(4),

        Out(0);

        private int value;

        @Override
        public final int getValue() {
            return value;
        }

        OscillatorJack(int value) {
            this.value = value;
        }
    }

    @Override
    public void restore() {
        super.restore();
        setAM(getAM(true));
        setCents(getCents(true));
        setFM(getFM(true));
        setOctave(getOctave(true));
        setOutGain(getOutGain(true));
        setPitch(getPitch(true));
        setSemis(getSemis(true));
        setWaveform(getWaveform(true));
    }
}
