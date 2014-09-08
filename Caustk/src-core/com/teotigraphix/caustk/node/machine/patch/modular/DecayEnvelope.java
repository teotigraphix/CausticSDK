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

import com.teotigraphix.caustk.node.machine.patch.modular.AREnvelope.EnvelopeSlope;

public class DecayEnvelope extends ModularComponentBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private float decay;

    private EnvelopeSlope decaySlope;

    private float outGain;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // decay
    //----------------------------------

    public float getDecay() {
        return decay;
    }

    float getDecay(boolean restore) {
        return getValue("decay");
    }

    /**
     * @param value (0..2)
     */
    public void setDecay(float value) {
        if (value == decay)
            return;
        decay = value;
        if (value < 0f || value > 2f)
            newRangeException("decay", "0..2", value);
        setValue("decay", value);
    }

    //----------------------------------
    // decaySlope
    //----------------------------------

    public EnvelopeSlope getDecaySlope() {
        return decaySlope;
    }

    EnvelopeSlope getDecaySlope(boolean restore) {
        return EnvelopeSlope.fromFloat(getValue("decay_slope"));
    }

    /**
     * @param value (0,1,2)
     */
    public void setDecaySlope(EnvelopeSlope value) {
        if (value == decaySlope)
            return;
        decaySlope = value;
        setValue("decay_slope", value.getValue());
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

    public DecayEnvelope() {
    }

    public DecayEnvelope(int bay) {
        super(bay);
        setLabel("DecayEnvelope");
    }

    @Override
    protected int getNumBays() {
        return 1;
    }

    @Override
    protected void updateComponents() {
        setValue("decay", decay);
        setValue("decay_slope", decaySlope.getValue());
        setValue("out_gain", outGain);
    }

    @Override
    protected void restoreComponents() {
        setDecay(getDecay(true));
        setDecaySlope(getDecaySlope(true));
        setOutGain(getOutGain(true));
    }

    public enum DecayEnvelopeJack implements IModularJack {

        Out(0),

        InModulation(0);

        private int value;

        @Override
        public final int getValue() {
            return value;
        }

        DecayEnvelopeJack(int value) {
            this.value = value;
        }
    }
}
