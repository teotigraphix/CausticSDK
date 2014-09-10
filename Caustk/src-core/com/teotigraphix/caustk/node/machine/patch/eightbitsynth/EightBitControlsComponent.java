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

package com.teotigraphix.caustk.node.machine.patch.eightbitsynth;

import com.teotigraphix.caustk.core.osc.EightBitSynthMessage;
import com.teotigraphix.caustk.node.machine.EightBitSynthMachine;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The 8bitsynth controls component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see EightBitSynthMachine#getControls()
 */
public class EightBitControlsComponent extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private float abBlend = 0f;

    private int octave = 0;

    private int semis = 0;

    private int cents = 0;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // abBlend
    //----------------------------------

    /**
     * @see EightBitSynthMessage#AB_BLEND
     */
    public float getAbBlend() {
        return abBlend;
    }

    public float queryAbBlend() {
        return EightBitSynthMessage.AB_BLEND.query(getRack(), getMachineIndex());
    }

    /**
     * @param abBlend (0.0..1.0)
     * @see EightBitSynthMessage#AB_BLEND
     */
    public void setAbBlend(float abBlend) {
        if (abBlend == this.abBlend)
            return;

        if (abBlend < 0f || abBlend > 1f)
            throw newRangeException(EightBitSynthMessage.AB_BLEND, "0..2", abBlend);

        this.abBlend = abBlend;
        EightBitSynthMessage.AB_BLEND.send(getRack(), getMachineIndex(), abBlend);
    }

    //----------------------------------
    // octave
    //----------------------------------

    /**
     * @see EightBitSynthMessage#OCTAVE
     */
    public int getOctave() {
        return octave;
    }

    public int queryOctave() {
        return (int)EightBitSynthMessage.OCTAVE.query(getRack(), getMachineIndex());
    }

    /**
     * @param octave (-4..4)
     * @see EightBitSynthMessage#OCTAVE
     */
    public void setOctave(int octave) {
        if (octave == this.octave)
            return;

        if (octave < -4 || octave > 4)
            throw newRangeException(EightBitSynthMessage.OCTAVE, "-4..4", octave);

        this.octave = octave;
        EightBitSynthMessage.OCTAVE.send(getRack(), getMachineIndex(), octave);
    }

    //----------------------------------
    // semis
    //----------------------------------

    /**
     * @see EightBitSynthMessage#SEMIS
     */
    public int getSemis() {
        return semis;
    }

    public int querySemis() {
        return (int)EightBitSynthMessage.SEMIS.query(getRack(), getMachineIndex());
    }

    /**
     * @param semis (-12..12)
     * @see EightBitSynthMessage#SEMIS
     */
    public void setSemis(int semis) {
        if (semis == this.semis)
            return;

        if (semis < -12 || semis > 12)
            throw newRangeException(EightBitSynthMessage.SEMIS, "-12..12", semis);

        this.semis = semis;
        EightBitSynthMessage.SEMIS.send(getRack(), getMachineIndex(), semis);
    }

    //----------------------------------
    // cents
    //----------------------------------

    /**
     * @see EightBitSynthMessage#CENTS
     */
    public int getCents() {
        return cents;
    }

    public int queryCents() {
        return (int)EightBitSynthMessage.CENTS.query(getRack(), getMachineIndex());
    }

    /**
     * @param cents (-100..100)
     * @see EightBitSynthMessage#CENTS
     */
    public void setCents(int cents) {
        if (cents == this.cents)
            return;

        if (cents < -100 || cents > 100)
            throw newRangeException(EightBitSynthMessage.CENTS, "-100..100", cents);

        this.cents = cents;
        EightBitSynthMessage.CENTS.send(getRack(), getMachineIndex(), cents);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public EightBitControlsComponent() {
    }

    public EightBitControlsComponent(int machineIndex) {
        super(machineIndex);
    }

    public EightBitControlsComponent(MachineNode machineNode) {
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
        EightBitSynthMessage.AB_BLEND.send(getRack(), getMachineIndex(), abBlend);
        EightBitSynthMessage.OCTAVE.send(getRack(), getMachineIndex(), octave);
        EightBitSynthMessage.SEMIS.send(getRack(), getMachineIndex(), semis);
        EightBitSynthMessage.CENTS.send(getRack(), getMachineIndex(), cents);
    }

    @Override
    protected void restoreComponents() {
        setAbBlend(queryAbBlend());
        setCents(queryCents());
        setOctave(queryOctave());
        setSemis(querySemis());
    }
}
