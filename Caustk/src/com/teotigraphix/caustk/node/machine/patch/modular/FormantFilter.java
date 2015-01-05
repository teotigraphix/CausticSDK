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

public class FormantFilter extends ModularComponentBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private int formant1;

    @Tag(101)
    private float morph;

    @Tag(102)
    private int formant2;

    @Tag(103)
    private float gain;

    @Tag(104)
    private float inGain;

    @Tag(105)
    private float morphModulation;

    @Tag(106)
    private float gainModulation;

    @Tag(107)
    private float outGain;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // formant1
    //----------------------------------

    public int getFormant1() {
        return formant1;
    }

    int getFormant1(boolean restore) {
        return (int)getValue("formant_1");
    }

    /**
     * Representing AY(0), AH(1), OW(2), OO(3), ER(4), EE(5), and IH(6).
     * 
     * @param value (0..6)
     */
    public void setFormant1(int value) {
        if (value == formant1)
            return;
        formant1 = value;
        if (value < 0 || value > 1)
            newRangeException("formant_1", "0..6", value);
        setValue("formant_1", value);
    }

    //----------------------------------
    // morph
    //----------------------------------

    public float getMorph() {
        return morph;
    }

    float getMorph(boolean restore) {
        return getValue("morph");
    }

    /**
     * @param value (0..1)
     */
    public void setMorph(float value) {
        if (value == morph)
            return;
        morph = value;
        if (value < 0f || value > 1f)
            newRangeException("morph", "0..1", value);
        setValue("morph", value);
    }

    //----------------------------------
    // formant2
    //----------------------------------

    public int getFormant2() {
        return formant2;
    }

    int getFormant2(boolean restore) {
        return (int)getValue("formant_2");
    }

    /**
     * Representing AY(0), AH(1), OW(2), OO(3), ER(4), EE(5), and IH(6).
     * 
     * @param value (0..6)
     */
    public void setFormant2(int value) {
        if (value == formant2)
            return;
        formant2 = value;
        if (value < 0 || value > 1)
            newRangeException("formant_2", "0..6", value);
        setValue("formant_2", value);
    }

    //----------------------------------
    // gain
    //----------------------------------

    public float getGain() {
        return gain;
    }

    float getGain(boolean restore) {
        return getValue("gain");
    }

    /**
     * @param value (0..1)
     */
    public void setGain(float value) {
        if (value == gain)
            return;
        gain = value;
        if (value < 0f || value > 1f)
            newRangeException("gain", "0..1", value);
        setValue("gain", value);
    }

    //----------------------------------
    // inGain
    //----------------------------------

    public float getInGain() {
        return inGain;
    }

    float getInGain(boolean restore) {
        return getValue("in_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setInGain(float value) {
        if (value == inGain)
            return;
        inGain = value;
        if (value < 0f || value > 1f)
            newRangeException("in_gain", "0..1", value);
        setValue("in_gain", value);
    }

    //----------------------------------
    // morphModulation
    //----------------------------------

    public float getMorphModulation() {
        return morphModulation;
    }

    float getMorphModulation(boolean restore) {
        return getValue("morph_mod");
    }

    /**
     * @param value (0..1)
     */
    public void setMorphModulation(float value) {
        if (value == morphModulation)
            return;
        morphModulation = value;
        if (value < 0f || value > 1f)
            newRangeException("morph_mod", "0..1", value);
        setValue("morph_mod", value);
    }

    //----------------------------------
    // gainModulation
    //----------------------------------

    public float getGainModulation() {
        return gainModulation;
    }

    float getGainModulation(boolean restore) {
        return getValue("gain_mod");
    }

    /**
     * @param value (0..1)
     */
    public void setGainModulation(float value) {
        if (value == gainModulation)
            return;
        gainModulation = value;
        if (value < 0f || value > 1f)
            newRangeException("gain_mod", "0..1", value);
        setValue("gain_mod", value);
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

    public FormantFilter() {
    }

    public FormantFilter(MachineNode machineNode, int bay) {
        super(machineNode, bay);
        setLabel("FormantFilter");
    }

    @Override
    protected int getNumBays() {
        return 2;
    }

    @Override
    protected void restoreComponents() {
        setFormant1(getFormant1(true));
        setFormant2(getFormant2(true));
        setGain(getGain(true));
        setGainModulation(getGainModulation(true));
        setInGain(getInGain(true));
        setMorph(getMorph(true));
        setMorphModulation(getMorphModulation(true));
        setOutGain(getOutGain(true));
    }

    public enum FormantFilterJack implements IModularJack {

        InInput(0),

        InMorph(1),

        InGain(2),

        OutOutput(0);

        private int value;

        @Override
        public final int getValue() {
            return value;
        }

        FormantFilterJack(int value) {
            this.value = value;
        }
    }
}
