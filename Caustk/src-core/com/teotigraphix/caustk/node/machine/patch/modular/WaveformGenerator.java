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

package com.teotigraphix.caustk.node.machine.patch.modular;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.node.machine.MachineNode;

public class WaveformGenerator extends ModularComponentBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float waveform;

    @Tag(101)
    private int octave;

    @Tag(102)
    private int semis;

    @Tag(103)
    private int cents;

    @Tag(104)
    private float fm;

    @Tag(105)
    private float am;

    @Tag(106)
    private float pitch;

    @Tag(107)
    private float outGain;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // waveform
    //----------------------------------

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

    public WaveformGenerator() {
    }

    public WaveformGenerator(MachineNode machineNode, int bay) {
        super(machineNode, bay);
        setLabel("WaveformGenerator");
    }

    @Override
    protected int getNumBays() {
        return 2;
    }

    public enum WaveformGeneratorJack implements IModularJack {

        InNote(0),

        InModulation(1),

        // XXX WaveformGenerator InSync

        InFM(2),

        InAM(3),

        InPitch(4),

        Out(0);

        private int value;

        @Override
        public final int getValue() {
            return value;
        }

        WaveformGeneratorJack(int value) {
            this.value = value;
        }
    }

    @Override
    protected void restoreComponents() {
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
