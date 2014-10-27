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
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.DelayMode;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link DelayEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class DelayEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(200)
    private float feedback = 0.5f;

    @Tag(201)
    private DelayMode mode = DelayMode.Mono;

    @Tag(202)
    private int time = 8;

    @Tag(203)
    private float wet = 0.5f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // feedback
    //----------------------------------

    /**
     * @see EffectControls#Delay_Feedback
     */
    public float getFeedback() {
        return feedback;
    }

    public float queryFeedback() {
        return get(EffectControls.Delay_Feedback);
    }

    /**
     * @see EffectControls#Delay_Feedback
     */
    public void setFeedback(float feedback) {
        if (!EffectControls.Delay_Feedback.set(feedback, this.feedback))
            return;
        this.feedback = feedback;
        set(EffectControls.Delay_Feedback, feedback);
    }

    //----------------------------------
    // mode
    //----------------------------------

    /**
     * @see EffectControls#Delay_Mode
     */
    public DelayMode getMode() {
        return mode;
    }

    public DelayMode queryMode() {
        return DelayMode.fromInt((int)get(EffectControls.Delay_Mode));
    }

    /**
     * @see EffectControls#Delay_Mode
     */
    public void setMode(DelayMode mode) {
        // XXX Unit test Bug?
        if (mode == null)
            return;
        if (!EffectControls.Delay_Mode.set(mode.getValue(), this.mode.getValue()))
            return;
        this.mode = mode;
        set(EffectControls.Delay_Mode, mode.getValue());
    }

    //----------------------------------
    // time
    //----------------------------------

    /**
     * @see EffectControls#Delay_Time
     */
    public int getTime() {
        return time;
    }

    public int queryTime() {
        return (int)get(EffectControls.Delay_Time);
    }

    /**
     * @see EffectControls#Delay_Time
     */
    public void setTime(int time) {
        if (!EffectControls.Delay_Time.set(time, this.time))
            return;
        this.time = time;
        set(EffectControls.Delay_Time, time);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see EffectControls#Delay_Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(EffectControls.Delay_Wet);
    }

    /**
     * @param wet (0.0..1.0)
     * @see EffectControls#Delay_Wet
     */
    public void setWet(float wet) {
        if (!EffectControls.Delay_Wet.set(wet, this.wet))
            return;
        this.wet = wet;
        set(EffectControls.Delay_Wet, wet);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public DelayEffect() {
    }

    public DelayEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.Delay);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        set(EffectControls.Delay_Feedback, feedback);
        set(EffectControls.Delay_Mode, mode.getValue());
        set(EffectControls.Delay_Time, time);
        set(EffectControls.Delay_Wet, wet);
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setFeedback(queryFeedback());
        setTime(queryTime());
        setWet(queryWet());
        setMode(queryMode());
    }
}
