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
import com.teotigraphix.caustk.core.osc.FilterMessage;
import com.teotigraphix.caustk.core.osc.FilterMessage.FilterType;
import com.teotigraphix.caustk.core.osc.IOSCControl;
import com.teotigraphix.caustk.node.machine.MachineChannel;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.SubSynthMachine;

/**
 * The synth filter component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see SubSynthMachine#getFilter()
 */
public class SynthFilterChannel extends MachineChannel {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float cutoff = 1.0f;

    @Tag(101)
    private float resonance = 0f;

    @Tag(102)
    private float attack = 0f;

    @Tag(103)
    private float decay = 0f;

    @Tag(104)
    private float release = 1.5f;

    @Tag(105)
    private float sustain = 1.0f;

    @Tag(106)
    private float track = 0f;

    @Tag(107)
    private FilterType type = FilterType.None;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // cutoff
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_CUTOFF
     */
    public float getCutoff() {
        return cutoff;
    }

    public float queryCutoff() {
        return FilterMessage.FILTER_CUTOFF.query(getRack(), getMachineIndex());
    }

    /**
     * @param cutoff (0.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_CUTOFF
     */
    public void setCutoff(float cutoff) {
        if (cutoff == this.cutoff)
            return;
        if (cutoff < 0 || cutoff > 1f)
            throw newRangeException(FilterMessage.FILTER_CUTOFF, "0..1", cutoff);
        this.cutoff = cutoff;
        FilterMessage.FILTER_CUTOFF.send(getRack(), getMachineIndex(), cutoff);
    }

    //----------------------------------
    // resonance
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_RESONANCE
     */
    public float getResonance() {
        return resonance;
    }

    public float queryResonance() {
        return FilterMessage.FILTER_RESONANCE.query(getRack(), getMachineIndex());
    }

    /**
     * @param resonance (0.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_RESONANCE
     */
    public void setResonance(float resonance) {
        if (resonance == this.resonance)
            return;
        if (resonance < 0f || resonance > 1f)
            throw newRangeException(FilterMessage.FILTER_RESONANCE, "0..1", resonance);
        this.resonance = resonance;
        FilterMessage.FILTER_RESONANCE.send(getRack(), getMachineIndex(), resonance);
    }

    //----------------------------------
    // attack
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_ATTACK
     */
    public float getAttack() {
        return attack;
    }

    public float queryAttack() {
        return FilterMessage.FILTER_ATTACK.query(getRack(), getMachineIndex());
    }

    /**
     * @param attack (0.0..1.75)
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_ATTACK
     */
    public void setAttack(float attack) {
        if (attack == this.attack)
            return;
        if (attack < 0 || attack > 1.75f)
            throw newRangeException(FilterMessage.FILTER_ATTACK, "0..1.75", attack);
        this.attack = attack;
        FilterMessage.FILTER_ATTACK.send(getRack(), getMachineIndex(), attack);
    }

    //----------------------------------
    // decay
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_DECAY
     */
    public float getDecay() {
        return decay;
    }

    public float queryDecay() {
        return FilterMessage.FILTER_DECAY.query(getRack(), getMachineIndex());
    }

    /**
     * @param decay (0.0..1.75)
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_DECAY
     */
    public void setDecay(float decay) {
        if (decay == this.decay)
            return;
        if (decay < 0 || decay > 1.75f)
            throw newRangeException(FilterMessage.FILTER_DECAY, "0..1.75", decay);
        this.decay = decay;
        FilterMessage.FILTER_DECAY.send(getRack(), getMachineIndex(), decay);
    }

    //----------------------------------
    // release
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_RELEASE
     */
    public float getRelease() {
        return release;
    }

    public float queryRelease() {
        return FilterMessage.FILTER_RELEASE.query(getRack(), getMachineIndex());
    }

    /**
     * @param release (0.0..1.75)
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_RELEASE
     */
    public void setRelease(float release) {
        if (release == this.release)
            return;
        if (release < 0 || release > 1.75f)
            throw newRangeException(FilterMessage.FILTER_RELEASE, "0..1.75", release);
        this.release = release;
        FilterMessage.FILTER_RELEASE.send(getRack(), getMachineIndex(), release);
    }

    //----------------------------------
    // sustain
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_SUSTAIN
     */
    public float getSustain() {
        return sustain;
    }

    public float querySustain() {
        return FilterMessage.FILTER_SUSTAIN.query(getRack(), getMachineIndex());
    }

    /**
     * @param sustain (0.0..1.75)
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_SUSTAIN
     */
    public void setSustain(float sustain) {
        if (sustain == this.sustain)
            return;
        if (sustain < 0f || sustain > 1.75f)
            throw newRangeException(FilterMessage.FILTER_SUSTAIN, "0..1.75", sustain);
        this.sustain = sustain;
        FilterMessage.FILTER_SUSTAIN.send(getRack(), getMachineIndex(), sustain);
    }

    //----------------------------------
    // track
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_KBTRACK
     */
    public float getTrack() {
        return track;
    }

    public float queryTrack() {
        return FilterMessage.FILTER_KBTRACK.query(getRack(), getMachineIndex());
    }

    /**
     * @param track (0.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_KBTRACK
     */
    public void setTrack(float track) {
        if (track == this.track)
            return;
        if (track < 0 || track > 1.0f)
            throw newRangeException(FilterMessage.FILTER_KBTRACK, "0..1.0", track);
        this.track = track;
        FilterMessage.FILTER_KBTRACK.send(getRack(), getMachineIndex(), track);
    }

    //----------------------------------
    // type
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_TYPE
     */
    public FilterType getType() {
        return type;
    }

    public FilterType queryType() {
        return FilterType.toType(FilterMessage.FILTER_TYPE.query(getRack(), getMachineIndex()));
    }

    public void setType(float type) {
        setType(FilterType.toType(type));
    }

    /**
     * @param type FilterType
     * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_TYPE
     */
    public void setType(FilterType type) {
        if (type == this.type)
            return;
        this.type = type;
        FilterMessage.FILTER_TYPE.send(getRack(), getMachineIndex(), type.getValue());
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public SynthFilterChannel() {
    }

    public SynthFilterChannel(MachineNode machineNode) {
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
        FilterMessage.FILTER_CUTOFF.send(getRack(), getMachineIndex(), cutoff);
        FilterMessage.FILTER_RESONANCE.send(getRack(), getMachineIndex(), resonance);
        FilterMessage.FILTER_ATTACK.send(getRack(), getMachineIndex(), attack);
        FilterMessage.FILTER_DECAY.send(getRack(), getMachineIndex(), decay);
        FilterMessage.FILTER_RELEASE.send(getRack(), getMachineIndex(), release);
        FilterMessage.FILTER_SUSTAIN.send(getRack(), getMachineIndex(), sustain);
        FilterMessage.FILTER_KBTRACK.send(getRack(), getMachineIndex(), track);
        FilterMessage.FILTER_TYPE.send(getRack(), getMachineIndex(), type.getValue());
    }

    @Override
    protected void restoreComponents() {
        setCutoff(queryCutoff());
        setResonance(queryResonance());

        setAttack(queryAttack());
        setDecay(queryDecay());
        setRelease(queryRelease());
        setSustain(querySustain());
        try {
            setTrack(queryTrack());
        } catch (IllegalArgumentException e) {
            // PCMSynth dosn't have tracking
        }

        setType(queryType());
    }

    public static enum SynthFilterControl implements IOSCControl {

        /**
         * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_ATTACK
         */
        Attack,

        /**
         * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_CUTOFF
         */
        Cutoff,

        /**
         * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_DECAY
         */
        Decay,

        /**
         * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_RELEASE
         */
        Release,

        /**
         * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_RESONANCE
         */
        Resonance,

        /**
         * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_SUSTAIN
         */
        Sustain,

        /**
         * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_KBTRACK
         */
        Track,

        /**
         * @see com.teotigraphix.caustk.core.osc.FilterMessage#FILTER_TYPE
         */
        Type;

        @Override
        public String getDisplayName() {
            return name();
        }
    }
}
