////////////////////////////////////////////////////////////////////////////////
//Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0 
//
//Unless required by applicable law or agreed to in writing, software 
//distributed under the License is distributed on an "AS IS" BASIS, 
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and 
//limitations under the License
//
//Author: Michael Schmalle, Principal Architect
//mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.node.effect;

import com.teotigraphix.caustk.core.osc.EffectsRackMessage.MultiFilterControl;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.MultiFilterMode;

/**
 * The {@link MultiFilterEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class MultiFilterEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private float frequency = 0.54f;

    private float gain = 0f;

    private MultiFilterMode mode = MultiFilterMode.LowShelf;

    private float resonance = 0.5f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // frequency 
    //----------------------------------

    /**
     * @see MultiFilterControl#Frequency
     */
    public float getFrequency() {
        return frequency;
    }

    public float queryFrequency() {
        return get(MultiFilterControl.Frequency);
    }

    /**
     * @param frequency (0.1..1.0)
     * @see MultiFilterControl#Frequency
     */
    public void setFrequency(float frequency) {
        if (frequency == this.frequency)
            return;
        if (frequency < 0.1f || frequency > 1f)
            throw newRangeException(MultiFilterControl.Frequency, "0.1..1", frequency);
        this.frequency = frequency;
        set(MultiFilterControl.Frequency, frequency);
    }

    //----------------------------------
    // gain 
    //----------------------------------

    /**
     * @see MultiFilterControl#Gain
     */
    public float getGain() {
        return gain;
    }

    public float queryGain() {
        return get(MultiFilterControl.Gain);
    }

    /**
     * @param gain (-12..12)
     * @see MultiFilterControl#Gain
     */
    public void setGain(float gain) {
        if (gain == this.gain)
            return;
        if (gain < -12f || gain > 12f)
            throw newRangeException(MultiFilterControl.Gain, "-12..12", gain);
        this.gain = gain;
        set(MultiFilterControl.Gain, gain);
    }

    //----------------------------------
    // mode 
    //----------------------------------

    /**
     * @see MultiFilterControl#Mode
     */
    public MultiFilterMode getMode() {
        return mode;
    }

    public MultiFilterMode queryMode() {
        return MultiFilterMode.fromInt((int)get(MultiFilterControl.Mode));
    }

    /**
     * @param mode MultiFilterMode
     * @see MultiFilterControl#Mode
     */
    public void setMode(MultiFilterMode mode) {
        if (mode == this.mode)
            return;
        this.mode = mode;
        set(MultiFilterControl.Mode, mode.getValue());
    }

    //----------------------------------
    // resonance 
    //----------------------------------

    /**
     * @see MultiFilterControl#Resonance
     */
    public float getResonance() {
        return resonance;
    }

    public float queryResonance() {
        return get(MultiFilterControl.Resonance);
    }

    /**
     * @param resonance (0.0..1.0)
     * @see MultiFilterControl#Resonance
     */
    public void setResonance(float resonance) {
        if (resonance == this.resonance)
            return;
        if (resonance < 0f || resonance > 1f)
            throw newRangeException(MultiFilterControl.Resonance, "0..1", resonance);
        this.resonance = resonance;
        set(MultiFilterControl.Resonance, resonance);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public MultiFilterEffect() {
        setType(EffectType.MultiFilter);
    }

    public MultiFilterEffect(int slot, int machineIndex) {
        super(slot, machineIndex);
        setType(EffectType.MultiFilter);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        set(MultiFilterControl.Mode, mode.getValue());
        set(MultiFilterControl.Frequency, frequency);
        set(MultiFilterControl.Gain, gain);
        set(MultiFilterControl.Resonance, resonance);
    }

    @Override
    protected void restoreComponents() {
        setFrequency(queryFrequency());
        setGain(queryGain());
        setMode(queryMode());
        setResonance(queryResonance());
    }
}
