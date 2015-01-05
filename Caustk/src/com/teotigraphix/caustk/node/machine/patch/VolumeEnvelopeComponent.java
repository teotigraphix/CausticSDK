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

package com.teotigraphix.caustk.node.machine.patch;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.VolumeMessage;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.SubSynthMachine;

/**
 * The volume envelope component for machines.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see SubSynthMachine#getVolumeEnvelope()
 */
public class VolumeEnvelopeComponent extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float attack = 0.0f;

    @Tag(101)
    private float decay = 0.0f;

    @Tag(102)
    private float sustain = 1.0f;

    @Tag(103)
    private float release = 0.0f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // attack
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.VolumeMessage#VOLUME_ATTACK
     */
    public float getAttack() {
        return attack;
    }

    public float queryAttack() {
        return VolumeMessage.VOLUME_ATTACK.query(getRack(), getMachineIndex());
    }

    /**
     * XXX 3.0625
     * 
     * @param attack (0.0..3.0)
     * @see com.teotigraphix.caustk.core.osc.VolumeMessage#VOLUME_ATTACK
     */
    public void setAttack(float attack) {
        if (attack == this.attack)
            return;
        if (attack < 0 || attack > 3.0625f)
            throw newRangeException(VolumeMessage.VOLUME_ATTACK, "0..3.0625", attack);
        this.attack = attack;
        VolumeMessage.VOLUME_ATTACK.send(getRack(), getMachineIndex(), attack);
    }

    //----------------------------------
    // decay
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.VolumeMessage#VOLUME_DECAY
     */
    public float getDecay() {
        return decay;
    }

    public float queryDecay() {
        return VolumeMessage.VOLUME_DECAY.query(getRack(), getMachineIndex());
    }

    /**
     * XXX 3.0625
     * 
     * @param decay (0.0..3.0)
     * @see com.teotigraphix.caustk.core.osc.VolumeMessage#VOLUME_DECAY
     */
    public void setDecay(float decay) {
        if (decay == this.decay)
            return;
        if (decay < 0 || decay > 3.0625f)
            throw newRangeException(VolumeMessage.VOLUME_DECAY, "0..3.0625", decay);
        this.decay = decay;
        VolumeMessage.VOLUME_DECAY.send(getRack(), getMachineIndex(), decay);
    }

    //----------------------------------
    // sustain
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.VolumeMessage#VOLUME_SUSTAIN
     */
    public float getSustain() {
        return sustain;
    }

    public float querySustain() {
        return VolumeMessage.VOLUME_SUSTAIN.query(getRack(), getMachineIndex());
    }

    /**
     * @param sustain (0.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.VolumeMessage#VOLUME_SUSTAIN
     */
    public void setSustain(float sustain) {
        if (sustain == this.sustain)
            return;
        if (sustain < 0 || sustain > 1.0f)
            throw newRangeException(VolumeMessage.VOLUME_SUSTAIN, "0..1.0", sustain);
        this.sustain = sustain;
        VolumeMessage.VOLUME_SUSTAIN.send(getRack(), getMachineIndex(), sustain);
    }

    //----------------------------------
    // release
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.VolumeMessage#VOLUME_RELEASE
     */
    public float getRelease() {
        return release;
    }

    public float queryRelease() {
        return VolumeMessage.VOLUME_RELEASE.query(getRack(), getMachineIndex());
    }

    /**
     * XXX 3.0625
     * 
     * @param value (0.0..3.0)
     * @see com.teotigraphix.caustk.core.osc.VolumeMessage#VOLUME_RELEASE
     */
    public void setRelease(float release) {
        if (release == this.release)
            return;
        if (release < 0 || release > 3.0625f)
            throw newRangeException(VolumeMessage.VOLUME_RELEASE.toString(), "0..3.0625", release);
        this.release = release;
        VolumeMessage.VOLUME_RELEASE.send(getRack(), getMachineIndex(), release);
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
        VolumeMessage.VOLUME_ATTACK.send(getRack(), getMachineIndex(), attack);
        VolumeMessage.VOLUME_DECAY.send(getRack(), getMachineIndex(), decay);
        VolumeMessage.VOLUME_SUSTAIN.send(getRack(), getMachineIndex(), sustain);
        VolumeMessage.VOLUME_RELEASE.send(getRack(), getMachineIndex(), release);
    }

    @Override
    protected void restoreComponents() {
        setAttack(queryAttack());
        setDecay(queryDecay());
        setRelease(queryRelease());
        setSustain(querySustain());
    }
}
