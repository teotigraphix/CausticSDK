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

    @Tag(200)
    private float damping = 0.25f;

    @Tag(201)
    private float delay = 0.04625f;

    @Tag(202)
    private float room = 0.85f;

    @Tag(203)
    private float wet = 0.195f;

    @Tag(204)
    private float width = 1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // damping
    //----------------------------------

    /**
     * @see EffectControls#Reverb_Damping
     */
    public float getDamping() {
        return damping;
    }

    public float queryDamping() {
        return get(EffectControls.Reverb_Damping);
    }

    /**
     * @see EffectControls#Reverb_Damping
     */
    public void setDamping(float damping) {
        if (!EffectControls.Reverb_Damping.isValid(damping, this.damping))
            return;
        this.damping = damping;
        set(EffectControls.Reverb_Damping, damping);
    }

    //----------------------------------
    // delay
    //----------------------------------

    /**
     * @see EffectControls#Reverb_Delay
     */
    public float getDelay() {
        return delay;
    }

    public float queryDelay() {
        return get(EffectControls.Reverb_Delay);
    }

    /**
     * @see EffectControls#Reverb_Delay
     */
    public void setDelay(float delay) {
        if (!EffectControls.Reverb_Delay.isValid(delay, this.delay))
            return;
        this.delay = delay;
        set(EffectControls.Reverb_Delay, delay);
    }

    //----------------------------------
    // room
    //----------------------------------

    /**
     * @see EffectControls#Reverb_Room
     */
    public float getRoom() {
        return room;
    }

    public float queryRoom() {
        return get(EffectControls.Reverb_Room);
    }

    /**
     * @see EffectControls#Reverb_Room
     */
    public void setRoom(float room) {
        if (!EffectControls.Reverb_Room.isValid(room, this.room))
            return;
        this.room = room;
        set(EffectControls.Reverb_Room, room);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see EffectControls#Reverb_Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(EffectControls.Reverb_Wet);
    }

    /**
     * @see EffectControls#Reverb_Wet
     */
    public void setWet(float wet) {
        if (!EffectControls.Reverb_Wet.isValid(wet, this.wet))
            return;
        this.wet = wet;
        set(EffectControls.Reverb_Wet, wet);
    }

    //----------------------------------
    // width
    //----------------------------------

    /**
     * @see EffectControls#Reverb_Width
     */
    public float getWidth() {
        return width;
    }

    public float queryWidth() {
        return get(EffectControls.Reverb_Width);
    }

    /**
     * @see EffectControls#Width
     */
    public void setWidth(float width) {
        if (!EffectControls.Reverb_Width.isValid(width, this.width))
            return;
        this.width = width;
        set(EffectControls.Reverb_Width, width);
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
        super.updateComponents();
        set(EffectControls.Reverb_Damping, damping);
        set(EffectControls.Reverb_Delay, delay);
        set(EffectControls.Reverb_Room, room);
        set(EffectControls.Reverb_Wet, wet);
        set(EffectControls.Reverb_Width, width);
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setDamping(queryDamping());
        setDelay(queryDelay());
        setRoom(queryRoom());
        setWet(queryWet());
        setWidth(queryWidth());
    }
}
