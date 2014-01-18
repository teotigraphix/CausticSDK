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

import com.teotigraphix.caustk.core.osc.EffectsRackMessage.DelayControl;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.DelayMode;

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

    private float feedback = 0.5f;

    private int time = 8;

    private float wet = 0.5f;

    private DelayMode mode = DelayMode.Mono;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // feedback
    //----------------------------------

    /**
     * @see DelayControl#Feedback
     */
    public float getFeedback() {
        return feedback;
    }

    public float queryFeedback() {
        return get(DelayControl.Feedback);
    }

    /**
     * @param feedback (0.0..1.0)
     * @see DelayControl#Feedback
     */
    public void setFeedback(float feedback) {
        if (feedback == this.feedback)
            return;
        if (feedback < 0f || feedback > 1f)
            throw newRangeException(DelayControl.Feedback, "0.0..1.0", feedback);
        this.feedback = feedback;
        set(DelayControl.Feedback, feedback);
    }

    //----------------------------------
    // time
    //----------------------------------

    /**
     * @see DelayControl#Time
     */
    public int getTime() {
        return time;
    }

    public int queryTime() {
        return (int)get(DelayControl.Time);
    }

    /**
     * @param time (1..12)
     * @see DelayControl#Feedback
     */
    public void setTime(int time) {
        if (time == this.time)
            return;
        if (time < 1 || time > 12)
            throw newRangeException(DelayControl.Time, "1..12", time);
        this.time = time;
        set(DelayControl.Time, time);
    }

    //----------------------------------
    // mode
    //----------------------------------

    /**
     * @see DelayControl#Mode
     */
    public DelayMode getMode() {
        return mode;
    }

    public DelayMode queryMode() {
        return DelayMode.fromInt((int)get(DelayControl.Mode));
    }

    /**
     * @param mode DelayMode
     * @see DelayControl#Mode
     */
    public void setMode(DelayMode mode) {
        if (mode == null)
            return; // XXX
        if (mode == this.mode)
            return;
        this.mode = mode;
        set(DelayControl.Mode, mode.getValue());
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see DelayControl#Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(DelayControl.Wet);
    }

    /**
     * @param wet (0.0..1.0)
     * @see DelayControl#Wet
     */
    public void setWet(float wet) {
        if (wet == this.wet)
            return;
        if (wet < 0f || wet > 1f)
            throw newRangeException(DelayControl.Wet, "0.0..1.0", wet);
        this.wet = wet;
        set(DelayControl.Wet, wet);
    }

    // XXX OSC Nedd Delay.mode

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public DelayEffect() {
        setType(EffectType.Delay);
    }

    public DelayEffect(int slot, int machineIndex) {
        super(slot, machineIndex);
        setType(EffectType.Delay);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        set(DelayControl.Feedback, feedback);
        set(DelayControl.Time, time);
        set(DelayControl.Wet, wet);
        set(DelayControl.Mode, mode.getValue());
    }

    @Override
    protected void restoreComponents() {
        setFeedback(queryFeedback());
        setTime(queryTime());
        setWet(queryWet());
        setMode(queryMode());
    }
}
