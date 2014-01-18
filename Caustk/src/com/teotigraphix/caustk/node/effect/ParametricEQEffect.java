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

import com.teotigraphix.caustk.core.osc.EffectsRackMessage.ParametricEQControl;

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

    private float frequency = 0.54f;

    private float gain = 0;

    private float width = 0.49999994f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // frequency
    //----------------------------------

    /**
     * @see ParametricEQControl#Frequency
     */
    public float getFrequency() {
        return frequency;
    }

    public float queryFrequency() {
        return get(ParametricEQControl.Frequency);
    }

    /**
     * @param frequency (0.0..1.0)
     * @see ParametricEQControl#Frequency
     */
    public void setFrequency(float frequency) {
        if (frequency == this.frequency)
            return;
        if (frequency < 0f || frequency > 1f)
            throw newRangeException(ParametricEQControl.Frequency, "0.0..1.0", frequency);
        this.frequency = frequency;
        set(ParametricEQControl.Frequency, frequency);
    }

    //----------------------------------
    // gain
    //----------------------------------

    /**
     * @see ParametricEQControl#Gain
     */
    public float getGain() {
        return gain;
    }

    public float queryGain() {
        return get(ParametricEQControl.Gain);
    }

    /**
     * @param gain (-12.0..12.0)
     * @see ParametricEQControl#Gain
     */
    public void setGain(float gain) {
        if (gain == this.gain)
            return;
        if (gain < -12f || gain > 12f)
            throw newRangeException(ParametricEQControl.Gain, "-12..12", gain);
        this.gain = gain;
        set(ParametricEQControl.Gain, gain);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see ParametricEQControl#Width
     */
    public float getWidth() {
        return width;
    }

    public float queryWidth() {
        return get(ParametricEQControl.Width);
    }

    /**
     * @param width (0.0..10.0)
     * @see ParametricEQControl#Width
     */
    public void setWidth(float width) {
        if (width == this.width)
            return;
        if (width < 0f || width > 10f)
            throw newRangeException(ParametricEQControl.Width, "0.0..10.0", width);
        this.width = width;
        set(ParametricEQControl.Width, width);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public ParametricEQEffect() {
        setType(EffectType.ParametricEQ);
    }

    public ParametricEQEffect(int slot, int machineIndex) {
        super(slot, machineIndex);
        setType(EffectType.ParametricEQ);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        set(ParametricEQControl.Frequency, frequency);
        set(ParametricEQControl.Gain, gain);
        set(ParametricEQControl.Width, width);
    }

    @Override
    protected void restoreComponents() {
        setFrequency(queryFrequency());
        setGain(queryGain());
        setWidth(queryWidth());
    }
}
