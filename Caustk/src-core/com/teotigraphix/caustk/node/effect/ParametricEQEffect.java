////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.node.effect;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.EffectControls;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link ParametricEQEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class ParametricEQEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(200)
    private float frequency = 0.54f;

    @Tag(201)
    private float gain = 0;

    @Tag(202)
    private float width = 0.49999994f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // frequency
    //----------------------------------

    /**
     * @see EffectControls#ParametricEQ_Frequency
     */
    public float getFrequency() {
        return frequency;
    }

    public float queryFrequency() {
        return get(EffectControls.ParametricEQ_Frequency);
    }

    /**
     * @see EffectControls#ParametricEQ_Frequency
     */
    public void setFrequency(float frequency) {
        if (!EffectControls.ParametricEQ_Frequency.isValid(frequency, this.frequency))
            return;
        this.frequency = frequency;
        set(EffectControls.ParametricEQ_Frequency, frequency);
    }

    //----------------------------------
    // gain
    //----------------------------------

    /**
     * @see EffectControls#ParametricEQ_Gain
     */
    public float getGain() {
        return gain;
    }

    public float queryGain() {
        return get(EffectControls.ParametricEQ_Gain);
    }

    /**
     * @see EffectControls#ParametricEQ_Gain
     */
    public void setGain(float gain) {
        if (!EffectControls.ParametricEQ_Gain.isValid(gain, this.gain))
            return;
        this.gain = gain;
        set(EffectControls.ParametricEQ_Gain, gain);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see EffectControls#ParametricEQ_Width
     */
    public float getWidth() {
        return width;
    }

    public float queryWidth() {
        return get(EffectControls.ParametricEQ_Width);
    }

    /**
     * @see EffectControls#ParametricEQ_Width
     */
    public void setWidth(float width) {
        if (!EffectControls.ParametricEQ_Width.isValid(width, this.width))
            return;
        this.width = width;
        set(EffectControls.ParametricEQ_Width, width);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public ParametricEQEffect() {
    }

    public ParametricEQEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.ParametricEQ);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        set(EffectControls.ParametricEQ_Frequency, frequency);
        set(EffectControls.ParametricEQ_Gain, gain);
        set(EffectControls.ParametricEQ_Width, width);
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setFrequency(queryFrequency());
        setGain(queryGain());
        setWidth(queryWidth());
    }
}
