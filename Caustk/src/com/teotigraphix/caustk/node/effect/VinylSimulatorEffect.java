////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link VinylSimulatorEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class VinylSimulatorEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(200)
    private float age = 0.5f;

    @Tag(201)
    private float dust = 0.75f;

    @Tag(202)
    private float noise = 0.33f;

    @Tag(203)
    private float scratch = 0.25f;

    @Tag(204)
    private float wet = 1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // age
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#VinylSimulator_Age
     */
    public float getAge() {
        return age;
    }

    public float queryAge() {
        return get(EffectControls.VinylSimulator_Age);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#VinylSimulator_Age
     */
    public void setAge(float age) {
        if (!EffectControls.VinylSimulator_Age.isValid(age, this.age))
            return;
        this.age = age;
        set(EffectControls.VinylSimulator_Age, age);
    }

    //----------------------------------
    // dust
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#VinylSimulator_Dust
     */
    public float getDust() {
        return dust;
    }

    public float queryDust() {
        return get(EffectControls.VinylSimulator_Dust);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#VinylSimulator_Dust
     */
    public void setDust(float dust) {
        if (!EffectControls.VinylSimulator_Dust.isValid(dust, this.dust))
            return;
        this.dust = dust;
        set(EffectControls.VinylSimulator_Dust, dust);
    }

    //----------------------------------
    // noise
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#VinylSimulator_Noise
     */
    public float getNoise() {
        return noise;
    }

    public float queryNoise() {
        return get(EffectControls.VinylSimulator_Noise);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#VinylSimulator_Noise
     */
    public void setNoise(float noise) {
        if (!EffectControls.VinylSimulator_Noise.isValid(noise, this.noise))
            return;
        this.noise = noise;
        set(EffectControls.VinylSimulator_Noise, noise);
    }

    //----------------------------------
    // scratch
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#VinylSimulator_Scratch
     */
    public float getScratch() {
        return scratch;
    }

    public float queryScratch() {
        return get(EffectControls.VinylSimulator_Scratch);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#VinylSimulator_Scratch
     */
    public void setScratch(float scratch) {
        if (!EffectControls.VinylSimulator_Scratch.isValid(scratch, this.scratch))
            return;
        this.scratch = scratch;
        set(EffectControls.VinylSimulator_Scratch, scratch);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#VinylSimulator_Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(EffectControls.VinylSimulator_Wet);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#VinylSimulator_Wet
     */
    public void setWet(float wet) {
        if (!EffectControls.VinylSimulator_Wet.isValid(wet, this.wet))
            return;
        this.wet = wet;
        set(EffectControls.VinylSimulator_Wet, wet);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public VinylSimulatorEffect() {
    }

    public VinylSimulatorEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.VinylSimulator);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        set(EffectControls.VinylSimulator_Age, age);
        set(EffectControls.VinylSimulator_Dust, dust);
        set(EffectControls.VinylSimulator_Noise, noise);
        set(EffectControls.VinylSimulator_Scratch, scratch);
        set(EffectControls.VinylSimulator_Wet, wet);
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setAge(queryAge());
        setDust(queryDust());
        setNoise(queryNoise());
        setScratch(queryScratch());
        setWet(queryWet());
    }
}
