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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.EffectControls;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.MultiFilterMode;
import com.teotigraphix.caustk.node.machine.MachineNode;

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

    @Tag(200)
    private float frequency = 0.54f;

    @Tag(201)
    private float gain = 0f;

    @Tag(202)
    private MultiFilterMode mode = MultiFilterMode.LowShelf;

    @Tag(203)
    private float resonance = 0.5f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // frequency 
    //----------------------------------

    /**
     * @see EffectControls#MultiFilter_Frequency
     */
    public float getFrequency() {
        return frequency;
    }

    public float queryFrequency() {
        return get(EffectControls.MultiFilter_Frequency);
    }

    /**
     * @see EffectControls#MultiFilter_Frequency
     */
    public void setFrequency(float frequency) {
        if (!EffectControls.MultiFilter_Frequency.set(frequency, this.frequency))
            return;
        this.frequency = frequency;
        set(EffectControls.MultiFilter_Frequency, frequency);
    }

    //----------------------------------
    // gain 
    //----------------------------------

    /**
     * @see EffectControls#MultiFilter_Gain
     */
    public float getGain() {
        return gain;
    }

    public float queryGain() {
        return get(EffectControls.MultiFilter_Gain);
    }

    /**
     * @see EffectControls#MultiFilter_Gain
     */
    public void setGain(float gain) {
        if (!EffectControls.MultiFilter_Gain.set(gain, this.gain))
            return;
        this.gain = gain;
        set(EffectControls.MultiFilter_Gain, gain);
    }

    //----------------------------------
    // mode 
    //----------------------------------

    /**
     * @see EffectControls#MultiFilter_Mode
     */
    public MultiFilterMode getMode() {
        return mode;
    }

    public MultiFilterMode queryMode() {
        return MultiFilterMode.fromInt((int)get(EffectControls.MultiFilter_Mode));
    }

    /**
     * @see EffectControls#MultiFilter_Mode
     */
    public void setMode(MultiFilterMode mode) {
        if (!EffectControls.MultiFilter_Mode.set(mode.getValue(), this.mode.getValue()))
            return;
        this.mode = mode;
        set(EffectControls.MultiFilter_Mode, mode.getValue());
    }

    //----------------------------------
    // resonance 
    //----------------------------------

    /**
     * @see EffectControls#MultiFilter_Resonance
     */
    public float getResonance() {
        return resonance;
    }

    public float queryResonance() {
        return get(EffectControls.MultiFilter_Resonance);
    }

    /**
     * @see EffectControls#MultiFilter_Resonance
     */
    public void setResonance(float resonance) {
        if (!EffectControls.MultiFilter_Resonance.set(resonance, this.resonance))
            return;
        this.resonance = resonance;
        set(EffectControls.MultiFilter_Resonance, resonance);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public MultiFilterEffect() {
    }

    public MultiFilterEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.MultiFilter);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        set(EffectControls.MultiFilter_Frequency, frequency);
        set(EffectControls.MultiFilter_Gain, gain);
        set(EffectControls.MultiFilter_Mode, mode.getValue());
        set(EffectControls.MultiFilter_Resonance, resonance);
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setFrequency(queryFrequency());
        setGain(queryGain());
        setMode(queryMode());
        setResonance(queryResonance());
    }
}
