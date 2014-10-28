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
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link CombFilterEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class CombFilterEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(200)
    private int freq = 10; // Phaser.rate

    @Tag(201)
    private float reso = 0.47f; // Phaser.feedback

    @Tag(202)
    private float wet = 0.8f; // Phaser.depth

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // freq
    //----------------------------------

    /**
     * @see EffectControls#CombFilter_Freq
     */
    public int getFreq() {
        return freq;
    }

    public int queryFreq() {
        return (int)get(EffectControls.CombFilter_Freq);
    }

    public void setRate(float freq) {
        setFreq((int)freq);
    }

    /**
     * @see EffectControls#CombFilter_Freq
     */
    public void setFreq(int freq) {
        if (!EffectControls.CombFilter_Freq.set(freq, this.freq))
            return;
        this.freq = freq;
        set(EffectControls.CombFilter_Freq, freq);
    }

    //----------------------------------
    // reso
    //----------------------------------

    /**
     * @see EffectControls#CombFilter_Reso
     */
    public float getReso() {
        return reso;
    }

    public float queryReso() {
        return get(EffectControls.CombFilter_Reso);
    }

    public void setFeedback(float reso) {
        setReso(reso);
    }

    /**
     * @see EffectControls#CombFilter_Reso
     */
    public void setReso(float reso) {
        if (!EffectControls.CombFilter_Reso.set(reso, this.reso))
            return;
        this.reso = reso;
        set(EffectControls.CombFilter_Reso, reso);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see EffectControls#CombFilter_Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(EffectControls.CombFilter_Wet);
    }

    public void setDepth(float depth) {
        setWet(depth);
    }

    /**
     * @see EffectControls#CombFilter_Wet
     */
    public void setWet(float wet) {
        if (!EffectControls.CombFilter_Wet.set(wet, this.wet))
            return;
        this.wet = wet;
        set(EffectControls.CombFilter_Wet, wet);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public CombFilterEffect() {
    }

    public CombFilterEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.CombFilter);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        set(EffectControls.CombFilter_Freq, getFreq());
        set(EffectControls.CombFilter_Reso, getFreq());
        set(EffectControls.CombFilter_Wet, getWet());
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setFreq(queryFreq());
        setReso(queryReso());
        setWet(queryWet());
    }
}
