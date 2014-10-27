////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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
 * The {@link CompressorEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class CompressorEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(200)
    private float attack = 0.01f;

    @Tag(201)
    private float ratio = 1f;

    @Tag(202)
    private float release = 0.05f;

    @Tag(203)
    private int sidechain = -1;

    @Tag(204)
    private float threshold = 0.1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // attack
    //----------------------------------

    /**
     * @see EffectControls#Compressor_Attack
     */
    public float getAttack() {
        return attack;
    }

    public float queryAttack() {
        return get(EffectControls.Compressor_Attack);
    }

    /**
     * @see EffectControls#Compressor_Attack
     */
    public void setAttack(float attack) {
        if (!EffectControls.Compressor_Attack.set(attack, this.attack))
            return;
        this.attack = attack;
        set(EffectControls.Compressor_Attack, attack);
    }

    //----------------------------------
    // ratio
    //----------------------------------

    /**
     * @see EffectControls#Compressor_Ratio
     */
    public float getRatio() {
        return ratio;
    }

    public float queryRatio() {
        return get(EffectControls.Compressor_Ratio);
    }

    /**
     * @see EffectControls#Compressor_Ratio
     */
    public void setRatio(float ratio) {
        if (!EffectControls.Compressor_Attack.set(ratio, this.ratio))
            return;
        this.ratio = ratio;
        set(EffectControls.Compressor_Ratio, ratio);
    }

    //----------------------------------
    // release
    //----------------------------------

    /**
     * @see EffectControls#Compressor_Release
     */
    public float getRelease() {
        return release;
    }

    public float queryRelease() {
        return get(EffectControls.Compressor_Release);
    }

    /**
     * @param release (0.001..0.2)
     * @see EffectControls#Compressor_Release
     */
    public void setRelease(float release) {
        if (!EffectControls.Compressor_Release.set(release, this.release))
            return;
        this.release = release;
        set(EffectControls.Compressor_Release, release);
    }

    //----------------------------------
    // sidechain
    //----------------------------------

    /**
     * @see EffectControls#Compressor_Sidechain
     */
    public int getSidechain() {
        return sidechain;
    }

    public int querySidechain() {
        return (int)get(EffectControls.Compressor_Sidechain);
    }

    /**
     * @see EffectControls#Compressor_Sidechain
     */
    public void setSidechain(int sidechain) {
        if (!EffectControls.Compressor_Sidechain.set(sidechain, this.sidechain))
            return;
        this.sidechain = sidechain;
        set(EffectControls.Compressor_Sidechain, sidechain);
    }

    //----------------------------------
    // threshold
    //----------------------------------

    /**
     * @see EffectControls#Compressor_Threshold
     */
    public float getThreshold() {
        return threshold;
    }

    public float queryThreshold() {
        return get(EffectControls.Compressor_Threshold);
    }

    /**
     * @see EffectControls#Compressor_Threshold
     */
    public void setThreshold(float threshold) {
        if (!EffectControls.Compressor_Threshold.set(threshold, this.threshold))
            return;
        this.threshold = threshold;
        set(EffectControls.Compressor_Threshold, threshold);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public CompressorEffect() {
    }

    public CompressorEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.Compressor);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        set(EffectControls.Compressor_Attack, attack);
        set(EffectControls.Compressor_Ratio, ratio);
        set(EffectControls.Compressor_Release, release);
        set(EffectControls.Compressor_Sidechain, sidechain);
        set(EffectControls.Compressor_Threshold, threshold);
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setAttack(queryAttack());
        setRatio(queryRatio());
        setRelease(queryRelease());
        setSidechain(querySidechain());
        setThreshold(queryThreshold());
    }
}
