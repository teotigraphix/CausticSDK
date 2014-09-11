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
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.VinylSimulatorControl;
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
     * @see VinylSimulatorControl#Age
     */
    public float getAge() {
        return age;
    }

    public float queryAge() {
        return get(VinylSimulatorControl.Age);
    }

    /**
     * @param age (0.0..1.0)
     * @see VinylSimulatorControl#Age
     */
    public void setAge(float age) {
        if (age == this.age)
            return;
        if (age < 0f || age > 1f)
            throw newRangeException(VinylSimulatorControl.Age, "0..1", age);
        this.age = age;
        set(VinylSimulatorControl.Age, age);
    }

    //----------------------------------
    // dust
    //----------------------------------

    /**
     * @see VinylSimulatorControl#Dust
     */
    public float getDust() {
        return dust;
    }

    public float queryDust() {
        return get(VinylSimulatorControl.Dust);
    }

    /**
     * @param dust (0.0..1.0)
     * @see VinylSimulatorControl#Dust
     */
    public void setDust(float dust) {
        if (dust == this.dust)
            return;
        if (dust < 0f || dust > 1f)
            throw newRangeException(VinylSimulatorControl.Dust, "0..1", dust);
        this.dust = dust;
        set(VinylSimulatorControl.Dust, dust);
    }

    //----------------------------------
    // noise
    //----------------------------------

    /**
     * @see VinylSimulatorControl#Noise
     */
    public float getNoise() {
        return noise;
    }

    public float queryNoise() {
        return get(VinylSimulatorControl.Noise);
    }

    /**
     * @param noise (0.0..1.0)
     * @see VinylSimulatorControl#Noise
     */
    public void setNoise(float noise) {
        if (noise == this.noise)
            return;
        if (noise < 0f || noise > 1f)
            throw newRangeException(VinylSimulatorControl.Noise, "0..1", noise);
        this.noise = noise;
        set(VinylSimulatorControl.Noise, noise);
    }

    //----------------------------------
    // scratch
    //----------------------------------

    /**
     * @see VinylSimulatorControl#Scratch
     */
    public float getScratch() {
        return scratch;
    }

    public float queryScratch() {
        return get(VinylSimulatorControl.Scratch);
    }

    /**
     * @param scratch (0.0..1.0)
     * @see VinylSimulatorControl#Scratch
     */
    public void setScratch(float scratch) {
        if (scratch == this.scratch)
            return;
        if (scratch < 0f || scratch > 1f)
            throw newRangeException(VinylSimulatorControl.Scratch, "0..1", scratch);
        this.scratch = scratch;
        set(VinylSimulatorControl.Scratch, scratch);
    }

    //----------------------------------
    // wet
    //----------------------------------

    /**
     * @see VinylSimulatorControl#Wet
     */
    public float getWet() {
        return wet;
    }

    public float queryWet() {
        return get(VinylSimulatorControl.Wet);
    }

    /**
     * @param wet (0.0..2.0)
     * @see VinylSimulatorControl#Wet
     */
    public void setWet(float wet) {
        if (wet == this.wet)
            return;
        if (wet < 0f || wet > 2f)
            throw newRangeException(VinylSimulatorControl.Wet, "0..2", wet);
        this.wet = wet;
        set(VinylSimulatorControl.Wet, wet);
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
        set(VinylSimulatorControl.Age, age);
        set(VinylSimulatorControl.Dust, dust);
        set(VinylSimulatorControl.Noise, noise);
        set(VinylSimulatorControl.Scratch, scratch);
        set(VinylSimulatorControl.Wet, wet);
    }

    @Override
    protected void restoreComponents() {
        setAge(queryAge());
        setDust(queryDust());
        setNoise(queryNoise());
        setScratch(queryScratch());
        setWet(queryWet());
    }
}
