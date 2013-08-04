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

package com.teotigraphix.caustk.core.components.modular;

import com.teotigraphix.caustk.controller.ICaustkController;

public class StereoLPF extends ModularComponentBase {

    //----------------------------------
    // slope
    //----------------------------------

    private int slope;

    public int getSlope() {
        return slope;
    }

    int getSlope(boolean restore) {
        return (int)getValue("slope");
    }

    /**
     * Representing 24dB(0) or 12dB(1).
     * 
     * @param value (0,1)
     */
    public void setSlope(int value) {
        if (value == slope)
            return;
        slope = value;
        if (value < 0 || value > 1)
            newRangeException("slope", "0,1", value);
        setValue("slope", value);
    }

    //----------------------------------
    // cutoff
    //----------------------------------

    private float cutoff;

    public float getCutoff() {
        return cutoff;
    }

    float getCutoff(boolean restore) {
        return getValue("cutoff");
    }

    /**
     * @param value (0..1)
     */
    public void setCutoff(float value) {
        if (value == cutoff)
            return;
        cutoff = value;
        if (value < 0f || value > 1f)
            newRangeException("cutoff", "0..1", value);
        setValue("cutoff", value);
    }

    //----------------------------------
    // resonance
    //----------------------------------

    private float resonance;

    public float getResonance() {
        return resonance;
    }

    float getResonance(boolean restore) {
        return getValue("resonance");
    }

    /**
     * @param value (0..1)
     */
    public void setResonance(float value) {
        if (value == resonance)
            return;
        resonance = value;
        if (value < 0f || value > 1f)
            newRangeException("resonance", "0..1", value);
        setValue("resonance", value);
    }

    //----------------------------------
    // cutoffModulation
    //----------------------------------

    private float cutoffModulation;

    public float getCutoffModulation() {
        return cutoffModulation;
    }

    float getCutoffModulation(boolean restore) {
        return getValue("cut_mod");
    }

    /**
     * @param value (0..1)
     */
    public void setCutoffModulation(float value) {
        if (value == cutoffModulation)
            return;
        cutoffModulation = value;
        if (value < 0f || value > 1f)
            newRangeException("cut_mod", "0..1", value);
        setValue("cut_mod", value);
    }

    //----------------------------------
    // resonanceModulation
    //----------------------------------

    private float resonanceModulation;

    public float getResonanceModulation() {
        return resonanceModulation;
    }

    float getResonanceModulation(boolean restore) {
        return getValue("res_mod");
    }

    /**
     * @param value (0..1)
     */
    public void setResonanceModulation(float value) {
        if (value == resonanceModulation)
            return;
        resonanceModulation = value;
        if (value < 0f || value > 1f)
            newRangeException("res_mod", "0..1", value);
        setValue("res_mod", value);
    }

    public StereoLPF() {
        //ComponentType.StereoLPF;
    }

    public StereoLPF(ICaustkController controller, int bay) {
        super(controller, bay);
    }

    @Override
    protected int getNumBays() {
        return 1;
    }

    public enum StereoLPFJack implements IModularJack {

        InInputLeft(0),

        InInputRight(1),

        InCutoff(2),

        InResonance(3),

        OutLeft(0),

        OutRight(1);

        private int value;

        @Override
        public final int getValue() {
            return value;
        }

        StereoLPFJack(int value) {
            this.value = value;
        }
    }
}
