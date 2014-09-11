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
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.ChorusControl;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.ChorusMode;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link ChorusEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class ChorusEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(200)
    private float depth = 0.25f;

    @Tag(201)
    private float rate = 0.4f;

    @Tag(202)
    private float wet = 0.5f;

    @Tag(203)
    private float delay;

    @Tag(204)
    private ChorusMode mode;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see ChorusControl#Depth
     */
    public float getDepth() {
        return depth;
    }

    public float queryDepth() {
        return get(ChorusControl.Depth);
    }

    /**
     * @param depth (0.1..0.95)
     * @see ChorusControl#Depth
     */
    public void setDepth(float depth) {
        if (depth == this.depth)
            return;
        if (depth < 0.1f || depth > 0.95f)
            throw newRangeException(ChorusControl.Depth, "0.1..0.95", depth);
        this.depth = depth;
        set(ChorusControl.Depth, depth);
    }

    //----------------------------------
    // rate
    //----------------------------------

    /**
     * @see ChorusControl#Rate
     */
    public float getRate() {
        return rate;
    }

    public float queryRate() {
        return get(ChorusControl.Rate);
    }

    /**
     * @param rate (0.0..1.0)
     * @see ChorusControl#Rate
     */
    public void setRate(float rate) {
        if (rate == this.rate)
            return;
        if (rate < 0f || rate > 1.0f)
            throw newRangeException(ChorusControl.Rate, "0.0..1.0", rate);
        this.rate = rate;
        set(ChorusControl.Rate, rate);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see ChorusControl#Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(ChorusControl.Wet);
    }

    /**
     * @param wet (0.0..0.5)
     * @see ChorusControl#Wet
     */
    public void setWet(float wet) {
        if (wet == this.wet)
            return;
        if (wet < 0f || wet > 0.5f)
            throw newRangeException(ChorusControl.Wet, "0..0.5", wet);
        this.wet = wet;
        set(ChorusControl.Wet, wet);
    }

    //----------------------------------
    // delay
    //----------------------------------

    /**
     * @see ChorusControl#Delay
     */
    public float getDelay() {
        return delay;
    }

    public float queryDelay() {
        return get(ChorusControl.Delay);
    }

    /**
     * @param delay (0.0..0.7)
     * @see ChorusControl#Delay
     */
    public void setDelay(float delay) {
        if (delay == this.delay)
            return;
        if (delay < 0f || delay > 0.7f)
            throw newRangeException(ChorusControl.Delay, "0..0.7", delay);
        this.delay = delay;
        set(ChorusControl.Delay, delay);
    }

    //----------------------------------
    // mode
    //----------------------------------

    /**
     * @see ChorusControl#Mode
     */
    public ChorusMode getMode() {
        return mode;
    }

    public ChorusMode queryMode() {
        return ChorusMode.fromInt((int)get(ChorusControl.Mode));
    }

    /**
     * @param mode {@link ChorusMode}
     * @see ChorusControl#Mode
     */
    public void setMode(ChorusMode mode) {
        if (mode == this.mode)
            return;
        this.mode = mode;
        set(ChorusControl.Mode, mode.getValue());
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public ChorusEffect() {
    }

    public ChorusEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.Chorus);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        set(ChorusControl.Depth, depth);
        set(ChorusControl.Rate, rate);
        set(ChorusControl.Delay, delay);
        set(ChorusControl.Wet, wet);
    }

    @Override
    protected void restoreComponents() {
        setDepth(queryDepth());
        setRate(queryRate());
        setDelay(queryDelay());
        setWet(queryWet());
    }
}
