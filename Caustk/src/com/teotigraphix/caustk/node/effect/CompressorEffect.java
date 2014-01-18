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

import com.teotigraphix.caustk.core.osc.EffectsRackMessage.CompressorControl;

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

    private float attack = 0.01f;

    private float ratio = 1f;

    private float release = 0.05f;

    private int sidechain = -1;

    private float threshold = 0.1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // attack
    //----------------------------------

    /**
     * @see CompressorControl#Attack
     */
    public float getAttack() {
        return attack;
    }

    public float queryAttack() {
        return get(CompressorControl.Attack);
    }

    /**
     * @param attack (0.00001..0.2)
     * @see CompressorControl#Attack
     */
    public void setAttack(float attack) {
        if (attack == this.attack)
            return;
        if (attack < 0.00001f || attack > 0.2f)
            throw newRangeException(CompressorControl.Attack, "0.00001..0.2", attack);
        this.attack = attack;
        set(CompressorControl.Attack, attack);
    }

    //----------------------------------
    // ratio
    //----------------------------------

    /**
     * @see CompressorControl#Ratio
     */
    public float getRatio() {
        return ratio;
    }

    public float queryRatio() {
        return get(CompressorControl.Ratio);
    }

    /**
     * @param ratio (0.0..1.0)
     * @see CompressorControl#Ratio
     */
    public void setRatio(float ratio) {
        if (ratio == this.ratio)
            return;
        if (ratio < 0f || ratio > 1.0f)
            throw newRangeException(CompressorControl.Ratio, "0.0..1.0", ratio);
        this.ratio = ratio;
        set(CompressorControl.Ratio, ratio);
    }

    //----------------------------------
    // release
    //----------------------------------

    /**
     * @see CompressorControl#Release
     */
    public float getRelease() {
        return release;
    }

    public float queryRelease() {
        return get(CompressorControl.Release);
    }

    /**
     * @param release (0.001..0.2)
     * @see CompressorControl#Release
     */
    public void setRelease(float release) {
        if (release == this.release)
            return;
        if (release < 0.001f || release > 0.2f)
            throw newRangeException(CompressorControl.Release, "0.001..0.2", release);
        this.release = release;
        set(CompressorControl.Release, release);
    }

    //----------------------------------
    // sidechain
    //----------------------------------

    /**
     * @see CompressorControl#Sidechain
     */
    public int getSidechain() {
        return sidechain;
    }

    public int querySidechain() {
        return (int)get(CompressorControl.Sidechain);
    }

    /**
     * @param sidechain (0..13)
     * @see CompressorControl#Sidechain
     */
    public void setSidechain(int sidechain) {
        if (sidechain == this.sidechain)
            return;
        if (sidechain < 0 || sidechain > 13)
            throw newRangeException(CompressorControl.Sidechain, "0..13", sidechain);
        this.sidechain = sidechain;
        set(CompressorControl.Sidechain, sidechain);
    }

    //----------------------------------
    // threshold
    //----------------------------------

    /**
     * @see CompressorControl#Threshold
     */
    public float getThreshold() {
        return threshold;
    }

    public float queryThreshold() {
        return get(CompressorControl.Threshold);
    }

    /**
     * @param threshold (0.0..0.1)
     * @see CompressorControl#Threshold
     */
    public void setThreshold(float threshold) {
        if (threshold == this.threshold)
            return;
        if (threshold < 0f || threshold > 1.0f)
            throw newRangeException(CompressorControl.Threshold, "0.0..1.0", threshold);
        this.threshold = threshold;
        set(CompressorControl.Threshold, threshold);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public CompressorEffect() {
        setType(EffectType.Compressor);
    }

    public CompressorEffect(int slot, int machineIndex) {
        super(slot, machineIndex);
        setType(EffectType.Compressor);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        set(CompressorControl.Attack, attack);
        set(CompressorControl.Ratio, ratio);
        set(CompressorControl.Release, release);
        set(CompressorControl.Sidechain, sidechain);
        set(CompressorControl.Threshold, threshold);
    }

    @Override
    protected void restoreComponents() {
        setAttack(queryAttack());
        setRatio(queryRatio());
        setRelease(queryRelease());
        setSidechain(querySidechain());
        setThreshold(queryThreshold());
    }
}
