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

public class FMPair extends ModularComponentBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private int octave1;

    @Tag(101)
    private int octave2;

    @Tag(102)
    private int semis2;

    @Tag(103)
    private float fm;

    @Tag(104)
    private float feedback;

    @Tag(105)
    private float fmModulation;

    @Tag(106)
    private float outGain;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // octave1
    //----------------------------------

    public float getOctave1() {
        return octave1;
    }

    int getOctave1(boolean restore) {
        return (int)getValue("octave_1");
    }

    /**
     * @param value (-4..4)
     */
    public void setOctave1(int value) {
        if (value == octave1)
            return;
        octave1 = value;
        if (value < -4 || value > 4)
            newRangeException("octave_1", "-4..4", value);
        setValue("octave_1", value);
    }

    //----------------------------------
    // octave2
    //----------------------------------

    public float getOctave2() {
        return octave1;
    }

    int getOctave2(boolean restore) {
        return (int)getValue("octave_2");
    }

    /**
     * @param value (-4..4)
     */
    public void setOctave2(int value) {
        if (value == octave2)
            return;
        octave2 = value;
        if (value < -4 || value > 4)
            newRangeException("octave_2", "-4..4", value);
        setValue("octave_2", value);
    }

    //----------------------------------
    // semis2
    //----------------------------------

    public float getSemis2() {
        return octave1;
    }

    int getSemis2(boolean restore) {
        return (int)getValue("semis_2");
    }

    /**
     * @param value (-6..6)
     */
    public void setSemis2(int value) {
        if (value == semis2)
            return;
        semis2 = value;
        if (value < -6 || value > 6)
            newRangeException("semis_2", "-6..6", value);
        setValue("semis_2", value);
    }

    //----------------------------------
    // fm
    //----------------------------------

    public float getFM() {
        return fm;
    }

    float getFM(boolean restore) {
        return getValue("fm");
    }

    /**
     * @param value (0..1)
     */
    public void setFM(float value) {
        if (value == fm)
            return;
        fm = value;
        if (value < 0f || value > 1f)
            newRangeException("fm", "0..1", value);
        setValue("fm", value);
    }

    //----------------------------------
    // feedback
    //----------------------------------

    public float getFeedback() {
        return feedback;
    }

    float getFeedback(boolean restore) {
        return getValue("feedback");
    }

    /**
     * @param value (0..1)
     */
    public void setFeedback(float value) {
        if (value == feedback)
            return;
        feedback = value;
        if (value < 0f || value > 1f)
            newRangeException("feedback", "0..1", value);
        setValue("feedback", value);
    }

    //----------------------------------
    // fmModulation
    //----------------------------------

    public float getFMModulation() {
        return fmModulation;
    }

    float getFMModulation(boolean restore) {
        return getValue("feedback");
    }

    /**
     * @param value (0..1)
     */
    public void setFMModulation(float value) {
        if (value == fmModulation)
            return;
        fmModulation = value;
        if (value < 0f || value > 1f)
            newRangeException("fm_mod", "0..1", value);
        setValue("fm_mod", value);
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

    public FMPair() {
    }

    public FMPair(MachineNode machineNode, int bay) {
        super(machineNode, bay);
        setLabel("FMPair");
    }

    @Override
    protected int getNumBays() {
        return 2;
    }

    @Override
    protected void restoreComponents() {
        setFeedback(getFeedback(true));
        setFM(getFM(true));
        setFMModulation(getFMModulation(true));
        setOctave1(getOctave1(true));
        setOctave2(getOctave2(true));
        setOutGain(getOutGain(true));
        setSemis2(getSemis2(true));
    }

    public enum FMPairJack implements IModularJack {

        InNote(0),

        InModulation(1),

        InFM(2),

        InFeedback(3),

        OutOutput(0);

        private int value;

        @Override
        public final int getValue() {
            return value;
        }

        FMPairJack(int value) {
            this.value = value;
        }
    }
}
