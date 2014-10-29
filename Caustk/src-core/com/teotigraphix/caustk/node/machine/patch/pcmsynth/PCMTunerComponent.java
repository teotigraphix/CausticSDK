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

package com.teotigraphix.caustk.node.machine.patch.pcmsynth;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.PCMSynthMessage;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.PCMSynthMachine;

/**
 * The {@link PCMSynthMachine#getTuner()} component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class PCMTunerComponent extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private int cents = 0;

    @Tag(101)
    private int octave = 0;

    @Tag(102)
    private int semis = 0;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // cents
    //----------------------------------

    /**
     * @see PCMSynthMessage#PITCH_CENTS
     */
    public int getCents() {
        return cents;
    }

    public int queryCents() {
        return (int)PCMSynthMessage.PITCH_CENTS.query(getRack(), getMachineIndex());
    }

    public void setCents(float cents) {
        setCents((int)cents);
    }

    /**
     * @param cents (-50..50)
     * @see PCMSynthMessage#PITCH_CENTS
     */
    public void setCents(int cents) {
        if (cents == this.cents)
            return;
        if (cents < -50 || cents > 50)
            throw newRangeException(PCMSynthMessage.PITCH_CENTS, "-50..50", cents);
        this.cents = cents;
        PCMSynthMessage.PITCH_CENTS.send(getRack(), getMachineIndex(), cents);
    }

    //----------------------------------
    // octave
    //----------------------------------

    /**
     * @see PCMSynthMessage#PITCH_OCTAVE
     */
    public int getOctave() {
        return octave;
    }

    public int queryOctave() {
        return (int)PCMSynthMessage.PITCH_OCTAVE.query(getRack(), getMachineIndex());
    }

    public void setOctave(float cents) {
        setOctave((int)cents);
    }

    /**
     * @param octave (-4..4)
     * @see PCMSynthMessage#PITCH_OCTAVE
     */
    public void setOctave(int octave) {
        if (octave == this.octave)
            return;
        if (octave < -4 || octave > 4)
            throw newRangeException(PCMSynthMessage.PITCH_OCTAVE, "-4..4", octave);
        this.octave = octave;
        PCMSynthMessage.PITCH_OCTAVE.send(getRack(), getMachineIndex(), octave);
    }

    //----------------------------------
    // semis
    //----------------------------------

    /**
     * @see PCMSynthMessage#PITCH_SEMIS
     */
    public int getSemis() {
        return semis;
    }

    public int querySemis() {
        return (int)PCMSynthMessage.PITCH_SEMIS.query(getRack(), getMachineIndex());
    }

    public void setSemis(float semis) {
        setSemis((int)semis);
    }

    /**
     * @param semis (-12..12)
     * @see PCMSynthMessage#PITCH_SEMIS
     */
    public void setSemis(int semis) {
        if (semis == this.semis)
            return;
        if (semis < -12 || semis > 12)
            throw newRangeException("pitch_semis", "-12..12", semis);
        this.semis = semis;
        PCMSynthMessage.PITCH_SEMIS.send(getRack(), getMachineIndex(), semis);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public PCMTunerComponent() {
    }

    public PCMTunerComponent(MachineNode machineNode) {
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
        PCMSynthMessage.PITCH_CENTS.send(getRack(), getMachineIndex(), cents);
        PCMSynthMessage.PITCH_OCTAVE.send(getRack(), getMachineIndex(), octave);
        PCMSynthMessage.PITCH_SEMIS.send(getRack(), getMachineIndex(), semis);
    }

    @Override
    protected void restoreComponents() {
        setCents(queryCents());
        setOctave(queryOctave());
        setSemis(querySemis());
    }
}
