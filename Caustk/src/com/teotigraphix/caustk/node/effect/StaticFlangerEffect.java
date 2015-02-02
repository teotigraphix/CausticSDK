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
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.StaticFlangerMode;
import com.teotigraphix.caustk.node.machine.Machine;

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
    private StaticFlangerMode mode = StaticFlangerMode.TriangleFull;

    @Tag(203)
    private float wet = 0.5f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // delay/depth
    //----------------------------------

    // UI calls it delay, OSC is depth, targeting the same effect natively

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#StaticFlanger_Depth
     */
    public float getDelay() {
        return delay;
    }

    public float queryDelay() {
        return get(EffectControls.StaticFlanger_Depth);
    }

    public void setDepth(float delay) {
        setDelay(delay);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#StaticFlanger_Depth
     */
    public void setDelay(float delay) {
        if (!EffectControls.StaticFlanger_Depth.isValid(delay, this.delay))
            return;
        this.delay = delay;
        set(EffectControls.StaticFlanger_Depth, delay);
    }

    //----------------------------------
    // feedback
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#StaticFlanger_Feedback
     */
    public float getFeedback() {
        return feedback;
    }

    public float queryFeedback() {
        return get(EffectControls.StaticFlanger_Feedback);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#StaticFlanger_Feedback
     */
    public void setFeedback(float feedback) {
        if (!EffectControls.StaticFlanger_Feedback.isValid(feedback, this.feedback))
            return;
        this.feedback = feedback;
        set(EffectControls.StaticFlanger_Feedback, feedback);
    }

    //----------------------------------
    // mode
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#StaticFlanger_Mode
     */
    public StaticFlangerMode getMode() {
        return mode;
    }

    public StaticFlangerMode queryMode() {
        return StaticFlangerMode.fromInt((int)get(EffectControls.StaticFlanger_Mode));
    }

    public void setMode(float mode) {
        setMode(StaticFlangerMode.fromInt((int)mode));
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#StaticFlanger_Mode
     */
    public void setMode(StaticFlangerMode mode) {
        if (!EffectControls.StaticFlanger_Mode.isValid(mode.getValue(), this.mode.getValue()))
            return;
        this.mode = mode;
        set(EffectControls.StaticFlanger_Mode, mode.getValue());
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#StaticFlanger_Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(EffectControls.StaticFlanger_Wet);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#StaticFlanger_Wet
     */
    public void setWet(float wet) {
        if (!EffectControls.StaticFlanger_Wet.isValid(wet, this.wet))
            return;
        this.wet = wet;
        set(EffectControls.StaticFlanger_Wet, wet);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public StaticFlangerEffect() {
    }

    public StaticFlangerEffect(Machine machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.StaticFlanger);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        set(EffectControls.StaticFlanger_Depth, delay);
        set(EffectControls.StaticFlanger_Feedback, feedback);
        set(EffectControls.StaticFlanger_Mode, mode.getValue());
        set(EffectControls.StaticFlanger_Wet, wet);
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setDelay(queryDelay());
        setFeedback(queryFeedback());
        setMode(queryMode());
        setWet(queryWet());
    }
}
