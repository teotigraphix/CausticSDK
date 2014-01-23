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

import com.teotigraphix.caustk.core.osc.EffectsRackMessage.DistortionControl;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.DistortionProgram;

/**
 * The {@link DistortionEffect} effect node.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class DistortionEffect extends EffectNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private DistortionProgram program = DistortionProgram.Overdrive;

    private float preGain = 4.05f;

    private float amount = 16.3f;

    private float postGain = 0.1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // program
    //----------------------------------

    /**
     * @see DistortionControl#Program
     */
    public DistortionProgram getProgram() {
        return program;
    }

    public DistortionProgram queryProgram() {
        return DistortionProgram.fromInt((int)get(DistortionControl.Program));
    }

    /**
     * @param program DistortionProgram
     * @see DistortionControl#Program
     */
    public void setProgram(DistortionProgram program) {
        if (program == this.program)
            return;
        this.program = program;
        set(DistortionControl.Program, program.getValue());
    }

    //----------------------------------
    // pre
    //----------------------------------

    /**
     * @see DistortionControl#PreGain
     */
    public float getPreGain() {
        return preGain;
    }

    public float queryPreGain() {
        return get(DistortionControl.PreGain);
    }

    /**
     * @param preGain (0.0..5.0)
     * @see DistortionControl#PreGain
     */
    public void setPreGain(float preGain) {
        if (preGain == this.preGain)
            return;
        if (preGain < 0f || preGain > 5f)
            newRangeException(DistortionControl.PreGain, "0.0..5.0", preGain);
        this.preGain = preGain;
        set(DistortionControl.PreGain, preGain);
    }

    //----------------------------------
    // amount
    //----------------------------------

    /**
     * @see DistortionControl#Amount
     */
    public float getAmount() {
        return amount;
    }

    public float queryAmount() {
        return get(DistortionControl.Amount);
    }

    /**
     * @param amount (0.0..20.0)
     * @see DistortionControl#Amount
     */
    public void setAmount(float amount) {
        if (amount == this.amount)
            return;
        if (amount < 0f || amount > 20f)
            newRangeException(DistortionControl.Amount, "0.0..20.0", amount);
        this.amount = amount;
        set(DistortionControl.Amount, amount);
    }

    //----------------------------------
    // post
    //----------------------------------

    /**
     * @see DistortionControl#PostGain
     */
    public float getPostGain() {
        return postGain;
    }

    public float queryPostGain() {
        return get(DistortionControl.PostGain);
    }

    /**
     * @param postGain (0.0..1.0)
     * @see DistortionControl#PostGain
     */
    public void setPostGain(float postGain) {
        if (postGain == this.postGain)
            return;
        if (postGain < 0f || postGain > 1f)
            newRangeException(DistortionControl.PostGain, "0.0..1.0", postGain);
        this.postGain = postGain;
        set(DistortionControl.PostGain, postGain);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public DistortionEffect() {
        setType(EffectType.Distortion);
    }

    public DistortionEffect(int slot, int machineIndex) {
        super(slot, machineIndex);
        setType(EffectType.Distortion);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        set(DistortionControl.Amount, amount);
        set(DistortionControl.PostGain, postGain);
        set(DistortionControl.PreGain, preGain);
        set(DistortionControl.Program, program.getValue());
    }

    @Override
    protected void restoreComponents() {
        setAmount(queryAmount());
        setPreGain(queryPreGain());
        setProgram(queryProgram());
        setPostGain(queryPostGain());
    }
}
