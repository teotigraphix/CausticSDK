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

import com.teotigraphix.caustk.core.osc.EffectsRackMessage.ReverbControl;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link ReverbEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class ReverbEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private float damping = 0.25f;

    private float delay = 0.04625f;

    private float room = 0.85f;

    private float wet = 0.195f;

    private float width = 1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // damping
    //----------------------------------

    /**
     * @see ReverbControl#Damping
     */
    public float getDamping() {
        return damping;
    }

    public float queryDamping() {
        return get(ReverbControl.Damping);
    }

    /**
     * @param damping (0.0..0.8)
     * @see ReverbControl#Damping
     */
    public void setDamping(float damping) {
        if (damping == this.damping)
            return;
        if (damping < 0f || damping > 0.8f)
            throw newRangeException(ReverbControl.Damping, "0..0.8", delay);
        this.damping = damping;
        set(ReverbControl.Damping, damping);
    }

    //----------------------------------
    // delay
    //----------------------------------

    /**
     * @see ReverbControl#Delay
     */
    public float getDelay() {
        return delay;
    }

    public float queryDelay() {
        return get(ReverbControl.Delay);
    }

    /**
     * @param delay (0.0..0.925)
     * @see ReverbControl#Delay
     */
    public void setDelay(float delay) {
        if (delay == this.delay)
            return;
        if (delay < 0f || delay > 0.925f)
            throw newRangeException(ReverbControl.Delay, "0..0.925", delay);
        this.delay = delay;
        set(ReverbControl.Delay, delay);
    }

    //----------------------------------
    // room
    //----------------------------------

    /**
     * @see ReverbControl#Room
     */
    public float getRoom() {
        return room;
    }

    public float queryRoom() {
        return get(ReverbControl.Room);
    }

    /**
     * @param room (0.0..0.925)
     * @see ReverbControl#Room
     */
    public void setRoom(float room) {
        if (room == this.room)
            return;
        if (room < 0f || room > 0.925f)
            throw newRangeException(ReverbControl.Room, "0..0.925", room);
        this.room = room;
        set(ReverbControl.Room, room);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see ReverbControl#Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(ReverbControl.Wet);
    }

    /**
     * @param wet (0.0..1.0)
     * @see ReverbControl#Wet
     */
    public void setWet(float wet) {
        if (wet == this.wet)
            return;
        if (wet < 0f || wet > 1f)
            throw newRangeException(ReverbControl.Wet, "0..1", wet);
        this.wet = wet;
        set(ReverbControl.Wet, wet);
    }

    //----------------------------------
    // width
    //----------------------------------

    /**
     * @see ReverbControl#Width
     */
    public float getWidth() {
        return width;
    }

    public float queryWidth() {
        return get(ReverbControl.Width);
    }

    /**
     * @param width (0.0..1.0)
     * @see ReverbControl#Width
     */
    public void setWidth(float width) {
        if (width == this.width)
            return;
        if (width < 0f || width > 1f)
            throw newRangeException(ReverbControl.Width, "0..1", width);
        this.width = width;
        set(ReverbControl.Width, width);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public ReverbEffect() {
    }

    public ReverbEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.Reverb);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        set(ReverbControl.Damping, damping);
        set(ReverbControl.Delay, delay);
        set(ReverbControl.Room, room);
        set(ReverbControl.Wet, wet);
        set(ReverbControl.Width, width);
    }

    @Override
    protected void restoreComponents() {
        setDamping(queryDamping());
        setDelay(queryDelay());
        setRoom(queryRoom());
        setWet(queryWet());
        setWidth(queryWidth());
    }
}
