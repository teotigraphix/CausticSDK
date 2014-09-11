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
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.FlangerControl;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.StaticFlangerControl;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.StaticFlangerMode;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link StaticFlangerEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class StaticFlangerEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(200)
    private float delay = 0f; // Flanger.depth

    @Tag(201)
    private float feedback = 0.575f;

    @Tag(202)
    private float wet = 0.5f;

    @Tag(203)
    private StaticFlangerMode mode = StaticFlangerMode.TriangleFull;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // delay/depth
    //----------------------------------

    /**
     * @see FlangerControl#Depth
     */
    public float getDelay() {
        return delay;
    }

    public float queryDelay() {
        return get(FlangerControl.Depth);
    }

    /**
     * @param delay (-0.95..0.95)
     * @see StaticFlangerControl#Depth
     */
    public void setDelay(float delay) {
        if (delay == this.delay)
            return;
        if (delay < -0.95f || delay > 0.95f)
            throw newRangeException(FlangerControl.Depth, "-0.95..0.95", delay);
        this.delay = delay;
        set(FlangerControl.Depth, delay);
    }

    //----------------------------------
    // feedback
    //----------------------------------

    /**
     * @see StaticFlangerControl#Feedback
     */
    public float getFeedback() {
        return feedback;
    }

    public float queryFeedback() {
        return get(StaticFlangerControl.Feedback);
    }

    /**
     * @param feedback (0.25..0.9)
     * @see StaticFlangerControl#Feedback
     */
    public void setFeedback(float feedback) {
        if (feedback == this.feedback)
            return;
        if (feedback < 0.25f || feedback > 0.9f)
            throw newRangeException(StaticFlangerControl.Feedback, "0.25..0.9", feedback);
        this.feedback = feedback;
        set(StaticFlangerControl.Feedback, feedback);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see StaticFlangerControl#Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(StaticFlangerControl.Wet);
    }

    /**
     * @param wet (0.0..1.0)
     * @see StaticFlangerControl#Wet
     */
    public void setWet(float wet) {
        if (wet == this.wet)
            return;
        if (wet < 0f || wet > 1f)
            throw newRangeException(StaticFlangerControl.Wet, "0..1", wet);
        this.wet = wet;
        set(StaticFlangerControl.Wet, wet);
    }

    //----------------------------------
    // mode
    //----------------------------------

    /**
     * @see StaticFlangerControl#Mode
     */
    public StaticFlangerMode getMode() {
        return mode;
    }

    public StaticFlangerMode queryMode() {
        return StaticFlangerMode.fromInt((int)get(StaticFlangerControl.Mode));
    }

    /**
     * @param mode {@link StaticFlangerMode}
     * @see StaticFlangerControl#Mode
     */
    public void setMode(StaticFlangerMode mode) {
        if (mode == this.mode)
            return;
        this.mode = mode;
        set(StaticFlangerControl.Mode, mode.getValue());
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public StaticFlangerEffect() {
    }

    public StaticFlangerEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.StaticFlanger);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        set(StaticFlangerControl.Depth, delay);
        set(StaticFlangerControl.Feedback, feedback);
        set(StaticFlangerControl.Mode, mode.getValue());
        set(StaticFlangerControl.Wet, wet);
    }

    @Override
    protected void restoreComponents() {
        setDelay(queryDelay());
        setFeedback(queryFeedback());
        setMode(queryMode());
        setWet(queryWet());
    }
}
