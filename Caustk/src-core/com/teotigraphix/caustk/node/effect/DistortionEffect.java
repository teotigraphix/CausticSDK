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
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.DistortionProgram;
import com.teotigraphix.caustk.node.machine.MachineNode;

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

    @Tag(200)
    private float amount = 16.3f;

    @Tag(201)
    private float postGain = 0.1f;

    @Tag(202)
    private float preGain = 4.05f;

    @Tag(203)
    private DistortionProgram program = DistortionProgram.Overdrive;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // amount
    //----------------------------------

    /**
     * @see EffectControls#Distortion_Amount
     */
    public float getAmount() {
        return amount;
    }

    public float queryAmount() {
        return get(EffectControls.Distortion_Amount);
    }

    /**
     * @see EffectControls#Distortion_Amount
     */
    public void setAmount(float amount) {
        if (!EffectControls.Distortion_Amount.set(amount, this.amount))
            return;
        this.amount = amount;
        set(EffectControls.Distortion_Amount, amount);
    }

    //----------------------------------
    // post
    //----------------------------------

    /**
     * @see EffectControls#Distortion_PostGain
     */
    public float getPostGain() {
        return postGain;
    }

    public float queryPostGain() {
        return get(EffectControls.Distortion_PostGain);
    }

    /**
     * @see EffectControls#Distortion_PostGain
     */
    public void setPostGain(float postGain) {
        if (!EffectControls.Distortion_PostGain.set(postGain, this.postGain))
            return;
        this.postGain = postGain;
        set(EffectControls.Distortion_PostGain, postGain);
    }

    //----------------------------------
    // pre
    //----------------------------------

    /**
     * @see EffectControls#Distortion_PreGain
     */
    public float getPreGain() {
        return preGain;
    }

    public float queryPreGain() {
        return get(EffectControls.Distortion_PreGain);
    }

    /**
     * @see EffectControls#Distortion_PreGain
     */
    public void setPreGain(float preGain) {
        if (!EffectControls.Distortion_PreGain.set(preGain, this.preGain))
            return;
        this.preGain = preGain;
        set(EffectControls.Distortion_PreGain, preGain);
    }

    //----------------------------------
    // program
    //----------------------------------

    /**
     * @see EffectControls#Distortion_Program
     */
    public DistortionProgram getProgram() {
        return program;
    }

    public DistortionProgram queryProgram() {
        return DistortionProgram.fromInt((int)get(EffectControls.Distortion_Program));
    }

    public void setProgram(float program) {
        setProgram(DistortionProgram.fromInt((int)program));
    }

    /**
     * @see EffectControls#Distortion_Program
     */
    public void setProgram(DistortionProgram program) {
        if (!EffectControls.Distortion_Program.set(program.getValue(), this.program.getValue()))
            return;
        this.program = program;
        set(EffectControls.Distortion_Program, program.getValue());
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public DistortionEffect() {
    }

    public DistortionEffect(MachineNode machineNode, int slot) {
        super(machineNode, slot);
        setType(EffectType.Distortion);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void updateComponents() {
        super.updateComponents();
        set(EffectControls.Distortion_Amount, amount);
        set(EffectControls.Distortion_PostGain, postGain);
        set(EffectControls.Distortion_PreGain, preGain);
        set(EffectControls.Distortion_Program, program.getValue());
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setAmount(queryAmount());
        setPreGain(queryPreGain());
        setProgram(queryProgram());
        setPostGain(queryPostGain());
    }
}
