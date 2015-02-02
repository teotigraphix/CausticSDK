////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.node.machine.patch.padsynth;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.PadSynthMessage;
import com.teotigraphix.caustk.node.machine.MachineChannel;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.PadSynthMachine;

/**
 * The {@link PadSynthMachine} volume envelope component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see PadSynthMachine#getVolumeEnvelope()
 */
public class VolumeEnvelopeComponent extends MachineChannel {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float gain1;

    @Tag(101)
    private float gain2;

    @Tag(102)
    private float attack;

    @Tag(103)
    private float decay;

    @Tag(104)
    private float sustain;

    @Tag(105)
    private float release;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // gain1
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#GAIN1
     */
    public float getGain1() {
        return gain1;
    }

    public float queryGain1() {
        return PadSynthMessage.GAIN1.query(getRack(), getMachineIndex());
    }

    /**
     * @param gain1 (0.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#GAIN1
     */
    public void setGain1(float gain1) {
        if (gain1 == this.gain1)
            return;
        if (gain1 < 0f || gain1 > 1f)
            throw newRangeException(PadSynthMessage.GAIN1, "0..1", gain1);
        this.gain1 = gain1;
        PadSynthMessage.GAIN1.send(getRack(), getMachineIndex(), gain1);
    }

    //----------------------------------
    // gain2
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#GAIN2
     */
    public float getGain2() {
        return gain2;
    }

    public float queryGain2() {
        return PadSynthMessage.GAIN2.query(getRack(), getMachineIndex());
    }

    /**
     * @param gain2 (0.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#GAIN2
     */
    public void setGain2(float gain2) {
        if (gain2 == this.gain2)
            return;
        if (gain2 < 0f || gain2 > 1f)
            throw newRangeException(PadSynthMessage.GAIN2, "0..1", gain2);
        this.gain2 = gain2;
        PadSynthMessage.GAIN2.send(getRack(), getMachineIndex(), gain2);
    }

    //----------------------------------
    // attack
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#VOLUME_ATTACK
     */
    public float getAttack() {
        return attack;
    }

    public float queryAttack() {
        return PadSynthMessage.VOLUME_ATTACK.query(getRack(), getMachineIndex());
    }

    /**
     * @param attack (0.0..3.0)
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#VOLUME_ATTACK
     */
    public void setAttack(float attack) {
        if (attack == this.attack)
            return;
        if (attack < 0f || attack > 3f)
            throw newRangeException(PadSynthMessage.VOLUME_ATTACK, "0..3", attack);
        this.attack = attack;
        PadSynthMessage.VOLUME_ATTACK.send(getRack(), getMachineIndex(), attack);
    }

    //----------------------------------
    // decay
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#VOLUME_DECAY
     */
    public float getDecay() {
        return decay;
    }

    public float queryDecay() {
        return PadSynthMessage.VOLUME_DECAY.query(getRack(), getMachineIndex());
    }

    /**
     * @param decay (0.0..3.0)
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#VOLUME_DECAY
     */
    public void setDecay(float decay) {
        if (decay == this.decay)
            return;
        if (decay < 0f || decay > 3f)
            throw newRangeException(PadSynthMessage.VOLUME_DECAY, "0..3", decay);
        this.decay = decay;
        PadSynthMessage.VOLUME_DECAY.send(getRack(), getMachineIndex(), decay);
    }

    //----------------------------------
    // sustain
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#VOLUME_SUSTAIN
     */
    public float getSustain() {
        return sustain;
    }

    public float querySustain() {
        return PadSynthMessage.VOLUME_SUSTAIN.query(getRack(), getMachineIndex());
    }

    /**
     * @param sustain (0.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#VOLUME_SUSTAIN
     */
    public void setSustain(float sustain) {
        if (sustain == this.sustain)
            return;
        if (sustain < 0f || sustain > 1f)
            throw newRangeException(PadSynthMessage.VOLUME_SUSTAIN, "0..1", sustain);
        this.sustain = sustain;
        PadSynthMessage.VOLUME_SUSTAIN.send(getRack(), getMachineIndex(), sustain);
    }

    //----------------------------------
    // release
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#VOLUME_RELEASE
     */
    public float getRelease() {
        return release;
    }

    public float queryRelease() {
        return PadSynthMessage.VOLUME_RELEASE.query(getRack(), getMachineIndex());
    }

    /**
     * @param release (0.0..3.0)
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#VOLUME_RELEASE
     */
    public void setRelease(float release) {
        if (release == this.release)
            return;
        if (release < 0f || release > 3f)
            throw newRangeException(PadSynthMessage.VOLUME_RELEASE, "0..3", release);
        this.release = release;
        PadSynthMessage.VOLUME_RELEASE.send(getRack(), getMachineIndex(), release);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public VolumeEnvelopeComponent() {
    }

    public VolumeEnvelopeComponent(MachineNode machineNode) {
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
        PadSynthMessage.GAIN1.send(getRack(), getMachineIndex(), gain1);
        PadSynthMessage.GAIN2.send(getRack(), getMachineIndex(), gain2);
        PadSynthMessage.VOLUME_ATTACK.send(getRack(), getMachineIndex(), attack);
        PadSynthMessage.VOLUME_DECAY.send(getRack(), getMachineIndex(), decay);
        PadSynthMessage.VOLUME_SUSTAIN.send(getRack(), getMachineIndex(), sustain);
        PadSynthMessage.VOLUME_RELEASE.send(getRack(), getMachineIndex(), release);
    }

    @Override
    protected void restoreComponents() {
        setAttack(queryAttack());
        setDecay(queryDecay());
        setGain1(queryGain1());
        setGain2(queryGain2());
        setRelease(queryRelease());
        setSustain(querySustain());
    }
}
