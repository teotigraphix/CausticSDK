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
    private float delay;

    @Tag(202)
    private ChorusMode mode = ChorusMode.TriangleFull;

    @Tag(203)
    private float rate = 0.4f;

    @Tag(204)
    private float wet = 0.5f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // delay
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Chorus_Delay
     */
    public float getDelay() {
        return delay;
    }

    public float queryDelay() {
        return get(EffectControls.Chorus_Delay);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Chorus_Delay
     */
    public void setDelay(float delay) {
        if (!EffectControls.Chorus_Delay.isValid(delay, this.delay))
            return;
        this.delay = delay;
        set(EffectControls.Chorus_Delay, delay);
    }

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Chorus_Depth
     */
    public float getDepth() {
        return depth;
    }

    public float queryDepth() {
        return get(EffectControls.Chorus_Depth);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Chorus_Depth
     */
    public void setDepth(float depth) {
        if (!EffectControls.Chorus_Depth.isValid(depth, this.depth))
            return;
        this.depth = depth;
        set(EffectControls.Chorus_Depth, depth);
    }

    //----------------------------------
    // mode
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Mode
     */
    public ChorusMode getMode() {
        return mode;
    }

    public ChorusMode queryMode() {
        return ChorusMode.fromInt((int)get(EffectControls.Chorus_Mode));
    }

    public void setMode(float mode) {
        setMode(ChorusMode.fromInt((int)mode));
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Mode
     */
    public void setMode(ChorusMode mode) {
        if (!EffectControls.Chorus_Mode.isValid(mode.getValue(), this.mode.getValue()))
            return;
        this.mode = mode;
        set(EffectControls.Chorus_Mode, mode.getValue());
    }

    //----------------------------------
    // rate
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Chorus_Rate
     */
    public float getRate() {
        return rate;
    }

    public float queryRate() {
        return get(EffectControls.Chorus_Rate);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Chorus_Rate
     */
    public void setRate(float rate) {
        if (!EffectControls.Chorus_Rate.isValid(rate, this.rate))
            return;
        this.rate = rate;
        set(EffectControls.Chorus_Rate, rate);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Chorus_Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(EffectControls.Chorus_Wet);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Chorus_Wet
     */
    public void setWet(float wet) {
        if (!EffectControls.Chorus_Wet.isValid(wet, this.wet))
            return;
        this.wet = wet;
        set(EffectControls.Chorus_Wet, wet);
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
        super.updateComponents();
        set(EffectControls.Chorus_Delay, delay);
        set(EffectControls.Chorus_Depth, depth);
        set(EffectControls.Chorus_Mode, mode.getValue());
        set(EffectControls.Chorus_Rate, rate);
        set(EffectControls.Chorus_Wet, wet);
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setDepth(queryDepth());
        setDelay(queryDelay());
        setMode(queryMode());
        setRate(queryRate());
        setWet(queryWet());
    }
}
