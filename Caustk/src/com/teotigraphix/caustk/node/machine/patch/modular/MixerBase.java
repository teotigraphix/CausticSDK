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

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.ModularMessage;
import com.teotigraphix.caustk.node.machine.Machine;

public abstract class MixerBase extends ModularComponentBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    @SuppressLint("UseSparseArrays")
    private Map<Integer, Float> gains = new HashMap<Integer, Float>();

    @Tag(101)
    private float outGain;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // gain[i]
    //----------------------------------

    /**
     * @param jack See
     *            {@link com.teotigraphix.caustk.node.machine.patch.modular.MixerBase.MixerJack#In6Gain}
     *            through
     *            {@link com.teotigraphix.caustk.node.machine.patch.modular.MixerBase.MixerJack#In1Gain}
     *            based on the mixer type.
     * @param value The gain value (0..1)
     */
    public void setGain(MixerJack jack, float value) {
        String control = "in" + jack.getValue() + "_gain";
        if (value < 0f || value > 1f)
            newRangeException(control, "0..1", value);
        gains.put(jack.getValue(), value);
        ModularMessage.SET.send(getRack(), getMachineIndex(), getBay(), control, value);
    }

    public float getGain(MixerJack jack, boolean restore) {
        String control = "in" + jack.getValue() + "_gain";
        return getValue(control);
    }

    public float getGain(MixerJack jack) {
        return gains.get(jack.getValue());
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

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MixerBase() {
    }

    public MixerBase(Machine machineNode, int bay) {
        super(machineNode, bay);
        setLabel("MixerBase");
    }

    @Override
    protected void restoreComponents() {
        setOutGain(getOutGain(true));
    }

    public enum MixerJack implements IModularJack {

        In1Gain(0),

        In2Gain(1),

        In3Gain(2),

        In4Gain(3),

        In5Gain(4),

        In6Gain(5),

        /**
         * Only available on the {@link TwoInputMixer} and {@link SixInputMixer}
         * .
         */
        OutModulation(0),

        OutGain(1);

        private int value;

        @Override
        public final int getValue() {
            return value;
        }

        MixerJack(int value) {
            this.value = value;
        }
    }
}
