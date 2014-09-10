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

import com.teotigraphix.caustk.core.osc.EffectsRackMessage.CabinetSimulatorControl;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link CabinetSimulatorEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class CabinetSimulatorEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private float damping;

    private float height;

    private float tone;

    private float wet;

    private float width;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // damping
    //----------------------------------

    /**
     * @see CabinetSimulatorControl#Damping
     */
    public float getDamping() {
        return damping;
    }

    public float queryDamping() {
        return get(CabinetSimulatorControl.Damping);
    }

    /**
     * @param damping (0.25..1.0)
     * @see CabinetSimulatorControl#Damping
     */
    public void setDamping(float damping) {
        if (damping == this.damping)
            return;
        if (damping < 0.25f || damping > 1f)
            throw newRangeException(CabinetSimulatorControl.Damping, "0.25..1", damping);
        this.damping = damping;
        set(CabinetSimulatorControl.Damping, damping);
    }

    //----------------------------------
    // height
    //----------------------------------

    /**
     * @see CabinetSimulatorControl#Height
     */
    public float getHeight() {
        return height;
    }

    public float queryHeight() {
        return get(CabinetSimulatorControl.Height);
    }

    /**
     * @param height (0.0..1.0)
     * @see CabinetSimulatorControl#Height
     */
    public void setHeight(float height) {
        if (height == this.height)
            return;
        if (height < 0f || height > 1f)
            throw newRangeException(CabinetSimulatorControl.Height, "0..1", height);
        this.height = height;
        set(CabinetSimulatorControl.Height, height);
    }

    //----------------------------------
    // tone
    //----------------------------------

    /**
     * @see CabinetSimulatorControl#Tone
     */
    public float getTone() {
        return tone;
    }

    public float queryTone() {
        return get(CabinetSimulatorControl.Tone);
    }

    /**
     * @param tone (0.0..1.0)
     * @see CabinetSimulatorControl#Tone
     */
    public void setTone(float tone) {
        if (tone == this.tone)
            return;
        if (tone < 0f || tone > 1f)
            throw newRangeException(CabinetSimulatorControl.Tone, "0..1", tone);
        this.tone = tone;
        set(CabinetSimulatorControl.Tone, tone);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see CabinetSimulatorControl#Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(CabinetSimulatorControl.Wet);
    }

    /**
     * @param wet (0.0..1.0)
     * @see CabinetSimulatorControl#Wet
     */
    public void setWet(float wet) {
        if (wet == this.wet)
            return;
        if (wet < 0f || wet > 1f)
            throw newRangeException(CabinetSimulatorControl.Wet, "0..1", wet);
        this.wet = wet;
        set(CabinetSimulatorControl.Wet, wet);
    }

    //----------------------------------
    // width
    //----------------------------------

    /**
     * @see CabinetSimulatorControl#Width
     */
    public float getWidth() {
        return width;
    }

    public float queryWidth() {
        return get(CabinetSimulatorControl.Width);
    }

    /**
     * @param width (0.0..1.0)
     * @see CabinetSimulatorControl#Width
     */
    public void setWidth(float width) {
        if (width == this.width)
            return;
        if (width < 0f || width > 1f)
            throw newRangeException(CabinetSimulatorControl.Width, "0..1", width);
        this.width = width;
        set(CabinetSimulatorControl.Width, width);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public CabinetSimulatorEffect() {
    }

    public CabinetSimulatorEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.CabinetSimulator);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        set(CabinetSimulatorControl.Damping, getDamping());
        set(CabinetSimulatorControl.Height, getHeight());
        set(CabinetSimulatorControl.Tone, getTone());
        set(CabinetSimulatorControl.Wet, getWet());
        set(CabinetSimulatorControl.Width, getWidth());
    }

    @Override
    protected void restoreComponents() {
        setDamping(queryDamping());
        setHeight(queryHeight());
        setTone(queryTone());
        setWet(queryWet());
        setWidth(queryWidth());
    }
}
