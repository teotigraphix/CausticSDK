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
import com.teotigraphix.caustk.node.machine.Machine;

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

    @Tag(200)
    private float damping;

    @Tag(201)
    private float height;

    @Tag(202)
    private float tone;

    @Tag(203)
    private float wet;

    @Tag(204)
    private float width;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // damping
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Damping
     */
    public float getDamping() {
        return damping;
    }

    public float queryDamping() {
        return get(EffectControls.CabinetSimulator_Damping);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Damping
     */
    public void setDamping(float damping) {
        if (!EffectControls.CabinetSimulator_Damping.isValid(damping, this.damping))
            return;
        this.damping = damping;
        set(EffectControls.CabinetSimulator_Damping, damping);
    }

    //----------------------------------
    // height
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#CabinetSimulator_Height
     */
    public float getHeight() {
        return height;
    }

    public float queryHeight() {
        return get(EffectControls.CabinetSimulator_Height);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#CabinetSimulator_Height
     */
    public void setHeight(float height) {
        if (!EffectControls.CabinetSimulator_Height.isValid(height, this.height))
            return;
        this.height = height;
        set(EffectControls.CabinetSimulator_Height, height);
    }

    //----------------------------------
    // tone
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#CabinetSimulator_Tone
     */
    public float getTone() {
        return tone;
    }

    public float queryTone() {
        return get(EffectControls.CabinetSimulator_Tone);
    }

    /**
     * @param tone (0.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.EffectControls#CabinetSimulator_Tone
     */
    public void setTone(float tone) {
        if (!EffectControls.CabinetSimulator_Tone.isValid(tone, this.tone))
            return;
        this.tone = tone;
        set(EffectControls.CabinetSimulator_Tone, tone);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#CabinetSimulator_Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(EffectControls.CabinetSimulator_Wet);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#CabinetSimulator_Wet
     */
    public void setWet(float wet) {
        if (!EffectControls.CabinetSimulator_Wet.isValid(wet, this.wet))
            return;
        this.wet = wet;
        set(EffectControls.CabinetSimulator_Wet, wet);
    }

    //----------------------------------
    // width
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#CabinetSimulator_Width
     */
    public float getWidth() {
        return width;
    }

    public float queryWidth() {
        return get(EffectControls.CabinetSimulator_Width);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#CabinetSimulator_Width
     */
    public void setWidth(float width) {
        if (!EffectControls.CabinetSimulator_Width.isValid(width, this.width))
            return;
        this.width = width;
        set(EffectControls.CabinetSimulator_Width, width);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public CabinetSimulatorEffect() {
    }

    public CabinetSimulatorEffect(Machine machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.CabinetSimulator);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        set(EffectControls.CabinetSimulator_Damping, getDamping());
        set(EffectControls.CabinetSimulator_Height, getHeight());
        set(EffectControls.CabinetSimulator_Tone, getTone());
        set(EffectControls.CabinetSimulator_Wet, getWet());
        set(EffectControls.CabinetSimulator_Width, getWidth());
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setDamping(queryDamping());
        setHeight(queryHeight());
        setTone(queryTone());
        setWet(queryWet());
        setWidth(queryWidth());
    }
}
