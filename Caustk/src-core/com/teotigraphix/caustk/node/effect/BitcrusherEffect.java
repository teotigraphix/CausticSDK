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
 * The {@link BitcrusherEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class BitcrusherEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(200)
    private int depth = 3;

    @Tag(201)
    private float jitter = 0f;

    @Tag(202)
    private float rate = 0.1f;

    @Tag(203)
    private float wet = 1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see EffectControls#Bitcrusher_Depth
     */
    public int getDepth() {
        return depth;
    }

    public int queryDepth() {
        return (int)get(EffectControls.Bitcrusher_Depth);
    }

    public void setDepth(float depth) {
        setDepth((int)depth);
    }

    /**
     * @see EffectControls#Bitcrusher_Depth
     */
    public void setDepth(int depth) {
        if (!EffectControls.Bitcrusher_Depth.isValid(depth, this.depth))
            return;
        this.depth = depth;
        set(EffectControls.Bitcrusher_Depth, depth);
    }

    //----------------------------------
    // jitter
    //----------------------------------

    /**
     * @see EffectControls#Bitcrusher_Jitter
     */
    public float getJitter() {
        return jitter;
    }

    public float queryJitter() {
        return get(EffectControls.Bitcrusher_Jitter);
    }

    /**
     * @see EffectControls#Bitcrusher_Jitter
     */
    public void setJitter(float jitter) {
        if (!EffectControls.Bitcrusher_Jitter.isValid(jitter, this.jitter))
            return;
        this.jitter = jitter;
        set(EffectControls.Bitcrusher_Jitter, jitter);
    }

    //----------------------------------
    // rate
    //----------------------------------

    /**
     * @see EffectControls#Bitcrusher_Rate
     */
    public float getRate() {
        return rate;
    }

    public float queryRate() {
        return get(EffectControls.Bitcrusher_Rate);
    }

    /**
     * @see EffectControls#Bitcrusher_Rate
     */
    public void setRate(float rate) {
        if (!EffectControls.Bitcrusher_Rate.isValid(rate, this.rate))
            return;
        this.rate = rate;
        set(EffectControls.Bitcrusher_Rate, rate);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see EffectControls#Bitcrusher_Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(EffectControls.Bitcrusher_Wet);
    }

    /**
     * @see EffectControls#Bitcrusher_Wet
     */
    public void setWet(float wet) {
        if (!EffectControls.Bitcrusher_Wet.isValid(wet, this.wet))
            return;
        this.wet = wet;
        set(EffectControls.Bitcrusher_Wet, wet);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public BitcrusherEffect() {
    }

    public BitcrusherEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.Bitcrusher);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        set(EffectControls.Bitcrusher_Depth, depth);
        set(EffectControls.Bitcrusher_Jitter, jitter);
        set(EffectControls.Bitcrusher_Rate, rate);
        set(EffectControls.Bitcrusher_Wet, wet);
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setDepth(queryDepth());
        setJitter(queryJitter());
        setRate(queryRate());
        setWet(queryWet());
    }
}
