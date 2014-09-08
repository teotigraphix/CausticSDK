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

import com.teotigraphix.caustk.core.osc.EffectsRackMessage.BitcrusherControl;

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

    private int depth = 3;

    private float jitter = 0f;

    private float rate = 0.1f;

    private float wet = 1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see BitcrusherControl#Depth
     */
    public int getDepth() {
        return depth;
    }

    public int queryDepth() {
        return (int)get(BitcrusherControl.Depth);
    }

    /**
     * @param depth (1..16)
     * @see BitcrusherControl#Depth
     */
    public void setDepth(int depth) {
        if (depth == this.depth)
            return;
        if (depth < 1 || depth > 16)
            throw newRangeException(BitcrusherControl.Depth, "1..16", depth);
        this.depth = depth;
        set(BitcrusherControl.Depth, depth);
    }

    //----------------------------------
    // jitter
    //----------------------------------

    /**
     * @see BitcrusherControl#Jitter
     */
    public float getJitter() {
        return jitter;
    }

    public float queryJitter() {
        return get(BitcrusherControl.Jitter);
    }

    /**
     * @param jitter (0.0..1.0)
     * @see BitcrusherControl#Jitter
     */
    public void setJitter(float jitter) {
        if (jitter == this.jitter)
            return;
        if (jitter < 0f || jitter > 1f)
            throw newRangeException(BitcrusherControl.Jitter, "0..1", jitter);
        this.jitter = jitter;
        set(BitcrusherControl.Jitter, jitter);
    }

    //----------------------------------
    // rate
    //----------------------------------

    /**
     * @see BitcrusherControl#Rate
     */
    public float getRate() {
        return rate;
    }

    public float queryRate() {
        return get(BitcrusherControl.Rate);
    }

    /**
     * @param rate (0.01..0.5)
     * @see BitcrusherControl#Rate
     */
    public void setRate(float rate) {
        if (rate == this.rate)
            return;
        if (rate < 0.01f || rate > 0.5f)
            throw newRangeException(BitcrusherControl.Rate, "0.01..0.5", rate);
        this.rate = rate;
        set(BitcrusherControl.Rate, rate);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see BitcrusherControl#Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(BitcrusherControl.Wet);
    }

    /**
     * @param wet (0.0..1.0)
     * @see BitcrusherControl#Wet
     */
    public void setWet(float wet) {
        if (wet == this.wet)
            return;
        if (wet < 0f || wet > 1f)
            throw newRangeException(BitcrusherControl.Wet, "0..1", wet);
        this.wet = wet;
        set(BitcrusherControl.Wet, wet);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public BitcrusherEffect() {
        setType(EffectType.Bitcrusher);
    }

    public BitcrusherEffect(int slot, int machineIndex) {
        super(slot, machineIndex);
        setType(EffectType.Bitcrusher);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        set(BitcrusherControl.Depth, depth);
        set(BitcrusherControl.Jitter, jitter);
        set(BitcrusherControl.Rate, rate);
        set(BitcrusherControl.Wet, wet);
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
