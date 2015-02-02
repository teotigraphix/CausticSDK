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
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.FlangerMode;
import com.teotigraphix.caustk.node.machine.Machine;

/**
 * The {@link FlangerEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class FlangerEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(200)
    private float depth = 0.25f;

    @Tag(201)
    private float feedback = 0.4f;

    @Tag(202)
    private FlangerMode mode = FlangerMode.TriangleFull;

    @Tag(203)
    private float rate = 0.4f;

    @Tag(204)
    private float wet = 0.5f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Flanger_Depth
     */
    public float getDepth() {
        return depth;
    }

    public float queryDepth() {
        return get(EffectControls.Flanger_Depth);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Depth
     */
    public void setDepth(float depth) {
        if (!EffectControls.Flanger_Depth.isValid(depth, this.depth))
            return;
        this.depth = depth;
        set(EffectControls.Flanger_Depth, depth);
    }

    //----------------------------------
    // feedback
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Flanger_Feedback
     */
    public float getFeedback() {
        return feedback;
    }

    public float queryFeedback() {
        return get(EffectControls.Flanger_Feedback);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Flanger_Feedback
     */
    public void setFeedback(float feedback) {
        if (!EffectControls.Flanger_Feedback.isValid(feedback, this.feedback))
            return;
        this.feedback = feedback;
        set(EffectControls.Flanger_Feedback, feedback);
    }

    //----------------------------------
    // mode
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Flanger_Mode
     */
    public FlangerMode getMode() {
        return mode;
    }

    public FlangerMode queryMode() {
        return FlangerMode.fromInt((int)get(EffectControls.Flanger_Mode));
    }

    public void setMode(float mode) {
        setMode(FlangerMode.fromInt((int)mode));
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Flanger_Mode
     */
    public void setMode(FlangerMode mode) {
        if (!EffectControls.Flanger_Mode.isValid(mode.getValue(), this.mode.getValue()))
            return;
        this.mode = mode;
        set(EffectControls.Flanger_Mode, mode.getValue());
    }

    //----------------------------------
    // rate
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Flanger_Rate
     */
    public float getRate() {
        return rate;
    }

    public float queryRate() {
        return get(EffectControls.Flanger_Rate);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Flanger_Rate
     */
    public void setRate(float rate) {
        if (!EffectControls.Flanger_Rate.isValid(rate, this.rate))
            return;
        this.rate = rate;
        set(EffectControls.Flanger_Rate, rate);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Flanger_Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(EffectControls.Flanger_Wet);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Flanger_Wet
     */
    public void setWet(float wet) {
        if (!EffectControls.Flanger_Wet.isValid(wet, this.wet))
            return;
        this.wet = wet;
        set(EffectControls.Flanger_Wet, wet);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public FlangerEffect() {
    }

    public FlangerEffect(Machine machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.Flanger);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        set(EffectControls.Flanger_Depth, depth);
        set(EffectControls.Flanger_Feedback, feedback);
        set(EffectControls.Flanger_Mode, mode.getValue());
        set(EffectControls.Flanger_Rate, rate);
        set(EffectControls.Flanger_Wet, wet);
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setDepth(queryDepth());
        setFeedback(queryFeedback());
        setRate(queryRate());
        setWet(queryWet());
        setMode(queryMode());
    }
}
