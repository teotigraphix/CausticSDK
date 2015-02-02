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
import com.teotigraphix.caustk.node.machine.Machine;
import com.teotigraphix.caustk.node.machine.PadSynthMachine;

/**
 * The {@link PadSynthMachine} morph component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see PadSynthMachine#getMorph()
 */
public class MorphComponent extends MachineChannel {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float blend = 0f;

    @Tag(101)
    private boolean evelopeEnabled = true;

    @Tag(102)
    private float attack = 0f;

    @Tag(103)
    private float decay = 0f;

    @Tag(104)
    private float sustain = 1f;

    @Tag(105)
    private float release = 0f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // blend
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#MORPH
     */
    public float getBlend() {
        return blend;
    }

    public float queryBlend() {
        return PadSynthMessage.MORPH.query(getRack(), getMachineIndex());
    }

    /**
     * @param blend (0.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#MORPH
     */
    public void setBlend(float blend) {
        if (blend == this.blend)
            return;
        if (blend < 0f || blend > 1f)
            throw newRangeException(PadSynthMessage.MORPH, "0..1", blend);
        this.blend = blend;
        PadSynthMessage.MORPH.send(getRack(), getMachineIndex(), blend);
    }

    //----------------------------------
    // evelopeEnabled
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#MORPH_ENV
     */
    public boolean isEnvelopeEnabled() {
        return evelopeEnabled;
    }

    public boolean queryEnvelopeEnabled() {
        return PadSynthMessage.MORPH_ENV.query(getRack(), getMachineIndex()) == 1f ? true : false;
    }

    /**
     * @param evelopeEnabled (true|false)
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#MORPH_ENV
     */
    public void setEnvelopeEnabled(boolean evelopeEnabled) {
        if (evelopeEnabled == this.evelopeEnabled)
            return;
        this.evelopeEnabled = evelopeEnabled;
        PadSynthMessage.MORPH_ENV.send(getRack(), getMachineIndex(), evelopeEnabled ? 1 : 0);
    }

    //----------------------------------
    // attack
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#MORPH_ATTACK
     */
    public float getAttack() {
        return attack;
    }

    public float queryAttack() {
        return PadSynthMessage.MORPH_ATTACK.query(getRack(), getMachineIndex());
    }

    /**
     * @param attack (0.0..3.0)
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#MORPH_ATTACK
     */
    public void setAttack(float attack) {
        if (attack == this.attack)
            return;
        if (attack < 0f || attack > 3f)
            throw newRangeException(PadSynthMessage.MORPH_ATTACK, "0..3", attack);
        this.attack = attack;
        PadSynthMessage.MORPH_ATTACK.send(getRack(), getMachineIndex(), attack);
    }

    //----------------------------------
    // decay
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#MORPH_ATTACK
     */
    public float getDecay() {
        return decay;
    }

    public float queryDecay() {
        return PadSynthMessage.MORPH_DECAY.query(getRack(), getMachineIndex());
    }

    /**
     * @param decay (0.0..3.0)
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#MORPH_ATTACK
     */
    public void setDecay(float decay) {
        if (decay == this.decay)
            return;
        if (decay < 0f || decay > 3f)
            throw newRangeException(PadSynthMessage.MORPH_DECAY, "0..3", decay);
        this.decay = decay;
        PadSynthMessage.MORPH_DECAY.send(getRack(), getMachineIndex(), decay);
    }

    //----------------------------------
    // sustain
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#MORPH_SUSTAIN
     */
    public float getSustain() {
        return sustain;
    }

    public float querySustain() {
        return PadSynthMessage.MORPH_SUSTAIN.query(getRack(), getMachineIndex());
    }

    /**
     * @param sustain (0.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#MORPH_SUSTAIN
     */
    public void setSustain(float sustain) {
        if (sustain == this.sustain)
            return;
        if (sustain < 0f || sustain > 1f)
            throw newRangeException(PadSynthMessage.MORPH_SUSTAIN, "0..1", sustain);
        this.sustain = sustain;
        PadSynthMessage.MORPH_SUSTAIN.send(getRack(), getMachineIndex(), sustain);
    }

    //----------------------------------
    // release
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#MORPH_RELEASE
     */
    public float getRelease() {
        return release;
    }

    public float queryRelease() {
        return PadSynthMessage.MORPH_RELEASE.query(getRack(), getMachineIndex());
    }

    /**
     * @param release (0.0..3.0)
     * @see com.teotigraphix.caustk.core.osc.PadSynthMessage#MORPH_RELEASE
     */
    public void setRelease(float release) {
        if (release == this.release)
            return;
        if (release < 0f || release > 3f)
            throw newRangeException(PadSynthMessage.MORPH_RELEASE, "0..3", release);
        this.release = release;
        PadSynthMessage.MORPH_RELEASE.send(getRack(), getMachineIndex(), release);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public MorphComponent() {
    }

    public MorphComponent(Machine machineNode) {
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
        PadSynthMessage.MORPH.send(getRack(), getMachineIndex(), blend);
        PadSynthMessage.MORPH_ENV.send(getRack(), getMachineIndex(), evelopeEnabled ? 1 : 0);
        PadSynthMessage.MORPH_ATTACK.send(getRack(), getMachineIndex(), attack);
        PadSynthMessage.MORPH_DECAY.send(getRack(), getMachineIndex(), decay);
        PadSynthMessage.MORPH_SUSTAIN.send(getRack(), getMachineIndex(), sustain);
        PadSynthMessage.MORPH_RELEASE.send(getRack(), getMachineIndex(), release);
    }

    @Override
    protected void restoreComponents() {
        setAttack(queryAttack());
        setBlend(queryBlend());
        setDecay(queryDecay());
        setEnvelopeEnabled(queryEnvelopeEnabled());
        setRelease(queryRelease());
        setSustain(querySustain());
    }
}
