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


public class DelayModule extends ModularComponentBase {

    private static final long serialVersionUID = 2564452846220434596L;

    //----------------------------------
    // rate
    //----------------------------------

    private int rate;

    public int getRate() {
        return rate;
    }

    int getRate(boolean restore) {
        return (int)getValue("rate");
    }

    /**
     * @param value (0..12)
     */
    public void setRate(int value) {
        if (value == rate)
            return;
        rate = value;
        if (value < 0 || value > 12)
            newRangeException("rate", "0..12", value);
        setValue("rate", value);
    }

    //----------------------------------
    // inGain
    //----------------------------------

    private float inGain;

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

    public DelayModule() {
    }

    public DelayModule(int bay) {
        super(bay);
    }

    @Override
    protected int getNumBays() {
        return 1;
    }

    public enum DelayModuleJack implements IModularJack {

        InInput(0),

        OutOutput(0);

        private int value;

        @Override
        public final int getValue() {
            return value;
        }

        DelayModuleJack(int value) {
            this.value = value;
        }
    }
}
