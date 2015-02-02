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

package com.teotigraphix.caustk.node.machine.patch.bassline;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.BasslineMessage;
import com.teotigraphix.caustk.core.osc.BasslineMessage.DistorionProgram;
import com.teotigraphix.caustk.node.machine.BasslineMachine;
import com.teotigraphix.caustk.node.machine.MachineChannel;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The bassline distortion component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see BasslineMachine#getDistortion()
 */
public class DistortionComponent extends MachineChannel {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float amount = 15f;

    @Tag(101)
    private float postGain = 0.2f;

    @Tag(102)
    private float preGain = 4.05f;

    @Tag(103)
    private DistorionProgram program = DistorionProgram.OFF;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // amount
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.BasslineMessage#DISTORTION_AMOUNT
     */
    public float getAmount() {
        return amount;
    }

    public float queryAmount() {
        return BasslineMessage.DISTORTION_AMOUNT.query(getRack(), getMachineIndex());
    }

    /**
     * @param amount (1.0..20.0).
     * @see com.teotigraphix.caustk.core.osc.BasslineMessage#DISTORTION_AMOUNT
     */
    public void setAmount(float amount) {
        if (amount == this.amount)
            return;
        if (amount < 0f || amount > 20f)
            throw newRangeException(BasslineMessage.DISTORTION_AMOUNT, "0..20", amount);
        this.amount = amount;
        BasslineMessage.DISTORTION_AMOUNT.send(getRack(), getMachineIndex(), amount);
    }

    //----------------------------------
    // postGain
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.BasslineMessage#DISTORTION_POSTGAIN
     */
    public float getPostGain() {
        return postGain;
    }

    public float queryPostGain() {
        return BasslineMessage.DISTORTION_POSTGAIN.query(getRack(), getMachineIndex());
    }

    /**
     * @param postGain (0.0..1.0).
     * @see com.teotigraphix.caustk.core.osc.BasslineMessage#DISTORTION_POSTGAIN
     */
    public void setPostGain(float postGain) {
        if (postGain == this.postGain)
            return;
        if (postGain < 0f || postGain > 1f)
            throw newRangeException(BasslineMessage.DISTORTION_POSTGAIN, "0..1", postGain);
        this.postGain = postGain;
        BasslineMessage.DISTORTION_POSTGAIN.send(getRack(), getMachineIndex(), postGain);
    }

    //----------------------------------
    // preGain
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.BasslineMessage#DISTORTION_PREGAIN
     */
    public float getPreGain() {
        return preGain;
    }

    public float queryPreGain() {
        return BasslineMessage.DISTORTION_PREGAIN.query(getRack(), getMachineIndex());
    }

    /**
     * @param preGain (0.0..5.0).
     * @see com.teotigraphix.caustk.core.osc.BasslineMessage#DISTORTION_PREGAIN
     */
    public void setPreGain(float preGain) {
        if (preGain == this.preGain)
            return;
        if (preGain < 0f || preGain > 5f)
            throw newRangeException(BasslineMessage.DISTORTION_PREGAIN.toString(), "0..5", preGain);
        this.preGain = preGain;
        BasslineMessage.DISTORTION_PREGAIN.send(getRack(), getMachineIndex(), preGain);
    }

    //----------------------------------
    // program
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.BasslineMessage#DISTORTION_PROGRAM
     */
    public DistorionProgram getProgram() {
        return program;
    }

    public DistorionProgram queryProgram() {
        return DistorionProgram.toType(BasslineMessage.DISTORTION_PROGRAM.query(getRack(),
                getMachineIndex()));
    }

    /**
     * @param program DistorionProgram
     * @see com.teotigraphix.caustk.core.osc.BasslineMessage#DISTORTION_PROGRAM
     */
    public void setProgram(DistorionProgram program) {
        if (program == this.program)
            return;
        this.program = program;
        BasslineMessage.DISTORTION_PROGRAM.send(getRack(), getMachineIndex(), program.getValue());
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public DistortionComponent() {
    }

    public DistortionComponent(MachineNode machineNode) {
        super(machineNode);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void updateComponents() {
        BasslineMessage.DISTORTION_AMOUNT.send(getRack(), getMachineIndex(), amount);
        BasslineMessage.DISTORTION_POSTGAIN.send(getRack(), getMachineIndex(), postGain);
        BasslineMessage.DISTORTION_PREGAIN.send(getRack(), getMachineIndex(), preGain);
        BasslineMessage.DISTORTION_PROGRAM.send(getRack(), getMachineIndex(), program.getValue());
    }

    @Override
    protected void restoreComponents() {
        setAmount(queryAmount());
        setPostGain(queryPostGain());
        setPreGain(queryPreGain());
        setProgram(queryProgram());
    }
}
