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
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.FlangerControl;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.FlangerMode;
import com.teotigraphix.caustk.node.machine.MachineNode;

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
    private float rate = 0.4f;

    @Tag(203)
    private float wet = 0.5f;

    @Tag(204)
    private FlangerMode mode = FlangerMode.TriangleFull;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see FlangerControl#Depth
     */
    public float getDepth() {
        return depth;
    }

    public float queryDepth() {
        return get(FlangerControl.Depth);
    }

    /**
     * @param depth (0.1..0.95)
     * @see FlangerControl#Depth
     */
    public void setDepth(float depth) {
        if (depth == this.depth)
            return;
        if (depth < 0.1f || depth > 0.95f)
            throw newRangeException(FlangerControl.Depth, "0.1..0.95", depth);
        this.depth = depth;
        set(FlangerControl.Depth, depth);
    }

    //----------------------------------
    // feedback
    //----------------------------------

    /**
     * @see FlangerControl#Feedback
     */
    public float getFeedback() {
        return feedback;
    }

    public float queryFeedback() {
        return get(FlangerControl.Feedback);
    }

    /**
     * @param feedback (0.25..0.8)
     * @see FlangerControl#Feedback
     */
    public void setFeedback(float feedback) {
        if (feedback == this.feedback)
            return;
        if (feedback < 0.25f || feedback > 0.8f)
            throw newRangeException(FlangerControl.Feedback, "0.25..0.8", feedback);
        this.feedback = feedback;
        set(FlangerControl.Feedback, feedback);
    }

    //----------------------------------
    // rate
    //----------------------------------

    /**
     * @see FlangerControl#Rate
     */
    public float getRate() {
        return rate;
    }

    public float queryRate() {
        return get(FlangerControl.Rate);
    }

    /**
     * @param rate (0.04..2.0)
     * @see FlangerControl#Rate
     */
    public void setRate(float rate) {
        if (rate == this.rate)
            return;
        if (rate < 0.04f || rate > 2.0f)
            throw newRangeException(FlangerControl.Rate, "0.04..2.0", rate);
        this.rate = rate;
        set(FlangerControl.Rate, rate);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see FlangerControl#Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(FlangerControl.Wet);
    }

    /**
     * @param wet (0.0..1.0)
     * @see FlangerControl#Wet
     */
    public void setWet(float wet) {
        if (wet == this.wet)
            return;
        if (wet < 0f || wet > 1f)
            throw newRangeException(FlangerControl.Wet, "0..1", wet);
        this.wet = wet;
        set(FlangerControl.Wet, wet);
    }

    //----------------------------------
    // mode
    //----------------------------------

    /**
     * @see FlangerControl#Mode
     */
    public FlangerMode getMode() {
        return mode;
    }

    public FlangerMode queryMode() {
        return FlangerMode.fromInt((int)get(FlangerControl.Mode));
    }

    /**
     * @param mode {@link FlangerMode}
     * @see FlangerControl#Mode
     */
    public void setMode(FlangerMode mode) {
        if (mode == this.mode)
            return;
        this.mode = mode;
        set(FlangerControl.Mode, mode.getValue());
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public FlangerEffect() {
    }

    public FlangerEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.Flanger);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        set(FlangerControl.Depth, depth);
        set(FlangerControl.Feedback, feedback);
        set(FlangerControl.Rate, rate);
        set(FlangerControl.Wet, wet);
        set(FlangerControl.Mode, mode.getValue());
    }

    @Override
    protected void restoreComponents() {
        setDepth(queryDepth());
        setFeedback(queryFeedback());
        setRate(queryRate());
        setWet(queryWet());
        setMode(queryMode());
    }
}
