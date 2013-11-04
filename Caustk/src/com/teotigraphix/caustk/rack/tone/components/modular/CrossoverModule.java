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

package com.teotigraphix.caustk.rack.tone.components.modular;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public class CrossoverModule extends ModularComponentBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float frequency;

    @Tag(101)
    private float inGain;

    @Tag(102)
    private float lowGain;

    @Tag(103)
    private float highGain;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // frequency
    //----------------------------------

    public float getFrequency() {
        return frequency;
    }

    float getFrequency(boolean restore) {
        return getValue("frequency");
    }

    /**
     * @param value (0..1)
     */
    public void setFrequency(float value) {
        if (value == frequency)
            return;
        frequency = value;
        if (value < 0f || value > 1f)
            newRangeException("frequency", "0..1", value);
        setValue("frequency", value);
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
    // lowGain
    //----------------------------------

    public float getLowGain() {
        return lowGain;
    }

    float getLowGain(boolean restore) {
        return getValue("low_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setLowGain(float value) {
        if (value == lowGain)
            return;
        lowGain = value;
        if (value < 0f || value > 1f)
            newRangeException("low_gain", "0..1", value);
        setValue("low_gain", value);
    }

    //----------------------------------
    // highGain
    //----------------------------------

    public float getHighGain() {
        return highGain;
    }

    float getHighGain(boolean restore) {
        return getValue("high_gain");
    }

    /**
     * @param value (0..1)
     */
    public void setHighGain(float value) {
        if (value == highGain)
            return;
        highGain = value;
        if (value < 0f || value > 1f)
            newRangeException("high_gain", "0..1", value);
        setValue("high_gain", value);
    }

    public CrossoverModule() {
    }

    public CrossoverModule(int bay) {
        super(bay);
    }

    @Override
    protected int getNumBays() {
        return 0;
    }

    @Override
    protected void updateComponents() {
        setValue("frequency", frequency);
        setValue("high_gain", highGain);
        setValue("in_gain", inGain);
        setValue("low_gain", lowGain);
    }

    @Override
    protected void restoreComponents() {
        setFrequency(getFrequency(true));
        setHighGain(getHighGain(true));
        setInGain(getInGain(true));
        setLowGain(getLowGain(true));

    }

    public enum CrossoverModuleJack implements IModularJack {

        InInput(0),

        OutLow(0),

        OutHigh(1);

        private int value;

        @Override
        public final int getValue() {
            return value;
        }

        CrossoverModuleJack(int value) {
            this.value = value;
        }
    }
}
