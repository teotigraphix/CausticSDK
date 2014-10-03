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

public class Crossfader extends ModularComponentBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float gainA;

    @Tag(101)
    private float gainB;

    @Tag(102)
    private float gainOut;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // gainA
    //----------------------------------

    public float getGainA() {
        return gainA;
    }

    float getGainA(boolean restore) {
        return getValue("gain_a");
    }

    /**
     * @param value (0..1)
     */
    public void setGainA(float value) {
        if (value == gainA)
            return;
        gainA = value;
        if (value < 0f || value > 1f)
            newRangeException("gain_a", "0..1", value);
        setValue("gain_a", value);
    }

    //----------------------------------
    // gainB
    //----------------------------------

    public float getGainB() {
        return gainB;
    }

    float getGainB(boolean restore) {
        return getValue("gain_b");
    }

    /**
     * @param value (0..1)
     */
    public void setGainB(float value) {
        if (value == gainB)
            return;
        gainB = value;
        if (value < 0f || value > 1f)
            newRangeException("gain_b", "0..1", value);
        setValue("gain_b", value);
    }

    //----------------------------------
    // gainOut
    //----------------------------------

    public float getGainOut() {
        return gainOut;
    }

    float getGainOut(boolean restore) {
        return getValue("gain_out");
    }

    /**
     * @param value (0..1)
     */
    public void setGainOut(float value) {
        if (value == gainOut)
            return;
        gainOut = value;
        if (value < 0f || value > 1f)
            newRangeException("gain_out", "0..1", value);
        setValue("gain_out", value);
    }

    public Crossfader() {
    }

    public Crossfader(MachineNode machineNode, int bay) {
        super(machineNode, bay);
        setLabel("Crossfader");
    }

    @Override
    protected int getNumBays() {
        return 1;
    }

    @Override
    protected void updateComponents() {
        setValue("gain_a", gainA);
        setValue("gain_b", gainB);
        setValue("gain_out", gainOut);
    }

    @Override
    protected void restoreComponents() {
        setGainA(getGainA(true));
        setGainB(getGainB(true));
        setGainOut(getGainOut(true));
    }

    public enum CrossfaderJack implements IModularJack {

        InA(0),

        InB(1),

        InFade(2),

        OutOutput(0);

        private int value;

        @Override
        public final int getValue() {
            return value;
        }

        CrossfaderJack(int value) {
            this.value = value;
        }
    }
}
