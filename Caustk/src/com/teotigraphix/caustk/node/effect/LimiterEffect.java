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

import com.teotigraphix.caustk.core.osc.EffectsRackMessage.LimiterControl;

/**
 * The {@link LimiterEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class LimiterEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private float attack = 0.01f;

    private float postGain = 0.5f;

    private float preGain = 2f;

    private float release = 0.5f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // attack
    //----------------------------------

    /**
     * @see LimiterControl#Attack
     */
    public float getAttack() {
        return attack;
    }

    public float queryAttack() {
        return get(LimiterControl.Attack);
    }

    /**
     * @param attack (0.0..0.05)
     * @see LimiterControl#Attack
     */
    public void setAttack(float attack) {
        if (attack == this.attack)
            return;
        if (attack < 0f || attack > 0.05f)
            throw newRangeException(LimiterControl.Attack, "0.05", attack);
        this.attack = attack;
        set(LimiterControl.Attack, attack);
    }

    //----------------------------------
    // postGain
    //----------------------------------

    /**
     * @see LimiterControl#PostGain
     */
    public float getPostGain() {
        return postGain;
    }

    public float queryPostGain() {
        return get(LimiterControl.PostGain);
    }

    /**
     * @param postGain (0.0..2.0)
     * @see LimiterControl#PostGain
     */
    public void setPostGain(float postGain) {
        if (postGain == this.postGain)
            return;
        if (postGain < 0f || postGain > 2f)
            throw newRangeException(LimiterControl.PostGain, "0..2", postGain);
        this.postGain = postGain;
        set(LimiterControl.PostGain, postGain);
    }

    //----------------------------------
    // preGain
    //----------------------------------

    /**
     * @see LimiterControl#PreGain
     */
    public float getPreGain() {
        return preGain;
    }

    public float queryPreGain() {
        return get(LimiterControl.PreGain);
    }

    /**
     * @param preGain (0.0..4.0)
     * @see LimiterControl#PreGain
     */
    public void setPreGain(float preGain) {
        if (preGain == this.preGain)
            return;
        if (preGain < 0f || preGain > 4f)
            throw newRangeException(LimiterControl.PreGain, "0..4", preGain);
        this.preGain = preGain;
        set(LimiterControl.PreGain, preGain);
    }

    //----------------------------------
    // release
    //----------------------------------

    /**
     * @see LimiterControl#Release
     */
    public float getRelease() {
        return release;
    }

    public float queryRelease() {
        return get(LimiterControl.Release);
    }

    /**
     * @param release (0.01..0.5)
     * @see LimiterControl#Release
     */
    public void setRelease(float release) {
        if (release == this.release)
            return;
        if (release < 0.01f || release > 0.5f)
            throw newRangeException(LimiterControl.Release, "0.01.5", release);
        this.release = release;
        set(LimiterControl.Release, release);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public LimiterEffect() {
        setType(EffectType.Limiter);
    }

    public LimiterEffect(int slot, int machineIndex) {
        super(slot, machineIndex);
        setType(EffectType.Limiter);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        set(LimiterControl.Attack, attack);
        set(LimiterControl.PostGain, postGain);
        set(LimiterControl.PreGain, preGain);
        set(LimiterControl.Release, release);
    }

    @Override
    protected void restoreComponents() {
        setAttack(queryAttack());
        setPostGain(queryPostGain());
        setPreGain(queryPreGain());
        setRelease(queryRelease());
    }
}
