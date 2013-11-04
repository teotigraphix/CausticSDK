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

public class LagProcessor extends ModularComponentBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float rateA;

    @Tag(101)
    private float rateB;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // rateA
    //----------------------------------

    public float getRateA() {
        return rateA;
    }

    float getRateA(boolean restore) {
        return getValue("rate_a");
    }

    /**
     * @param value (0..1)
     */
    public void setRateA(float value) {
        if (value == rateA)
            return;
        rateA = value;
        if (value < 0f || value > 1f)
            newRangeException("rate_a", "0..1", value);
        setValue("rate_a", value);
    }

    //----------------------------------
    // rateB
    //----------------------------------

    public float getRateB() {
        return rateB;
    }

    float getRateB(boolean restore) {
        return getValue("rate_b");
    }

    /**
     * @param value (0..1)
     */
    public void setRateB(float value) {
        if (value == rateB)
            return;
        rateB = value;
        if (value < 0f || value > 1f)
            newRangeException("rate_b", "0..1", value);
        setValue("rate_b", value);
    }

    public LagProcessor() {
    }

    public LagProcessor(int bay) {
        super(bay);
    }

    @Override
    protected int getNumBays() {
        return 1;
    }

    @Override
    protected void restoreComponents() {
        setRateA(getRateA(true));
        setRateB(getRateB(true));
    }

    public enum LagProcessorJack implements IModularJack {

        InA(0),

        InB(1),

        OutA(0),

        OutB(1);

        private int value;

        @Override
        public final int getValue() {
            return value;
        }

        LagProcessorJack(int value) {
            this.value = value;
        }
    }
}
