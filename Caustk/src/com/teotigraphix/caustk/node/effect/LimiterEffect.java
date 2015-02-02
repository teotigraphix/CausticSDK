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
import com.teotigraphix.caustk.node.machine.Machine;

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

    @Tag(200)
    private float attack = 0.01f;

    @Tag(201)
    private float postGain = 0.5f;

    @Tag(202)
    private float preGain = 2f;

    @Tag(203)
    private float release = 0.5f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // attack
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Limiter_Attack
     */
    public float getAttack() {
        return attack;
    }

    public float queryAttack() {
        return get(EffectControls.Limiter_Attack);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Limiter_Attack
     */
    public void setAttack(float attack) {
        if (!EffectControls.Limiter_Attack.isValid(attack, this.attack))
            return;
        this.attack = attack;
        set(EffectControls.Limiter_Attack, attack);
    }

    //----------------------------------
    // postGain
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Limiter_PostGain
     */
    public float getPostGain() {
        return postGain;
    }

    public float queryPostGain() {
        return get(EffectControls.Limiter_PostGain);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Limiter_PostGain
     */
    public void setPostGain(float postGain) {
        if (!EffectControls.Limiter_PostGain.isValid(postGain, this.postGain))
            return;
        this.postGain = postGain;
        set(EffectControls.Limiter_PostGain, postGain);
    }

    //----------------------------------
    // preGain
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Limiter_PreGain
     */
    public float getPreGain() {
        return preGain;
    }

    public float queryPreGain() {
        return get(EffectControls.Limiter_PreGain);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Limiter_PreGain
     */
    public void setPreGain(float preGain) {
        if (!EffectControls.Limiter_PreGain.isValid(preGain, this.preGain))
            return;
        this.preGain = preGain;
        set(EffectControls.Limiter_PreGain, preGain);
    }

    //----------------------------------
    // release
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Limiter_Release
     */
    public float getRelease() {
        return release;
    }

    public float queryRelease() {
        return get(EffectControls.Limiter_Release);
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.EffectControls#Limiter_Release
     */
    public void setRelease(float release) {
        if (!EffectControls.Limiter_Release.isValid(release, this.release))
            return;
        this.release = release;
        set(EffectControls.Limiter_Release, release);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public LimiterEffect() {
    }

    public LimiterEffect(Machine machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.Limiter);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        set(EffectControls.Limiter_Attack, attack);
        set(EffectControls.Limiter_PostGain, postGain);
        set(EffectControls.Limiter_PreGain, preGain);
        set(EffectControls.Limiter_Release, release);
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setAttack(queryAttack());
        setPostGain(queryPostGain());
        setPreGain(queryPreGain());
        setRelease(queryRelease());
    }
}
