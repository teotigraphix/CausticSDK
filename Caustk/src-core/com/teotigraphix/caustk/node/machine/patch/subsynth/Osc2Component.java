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

package com.teotigraphix.caustk.node.machine.patch.subsynth;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.SubSynthMessage;
import com.teotigraphix.caustk.core.osc.SubSynthMessage.CentsMode;
import com.teotigraphix.caustk.core.osc.SubSynthMessage.Osc2Waveform;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.SubSynthMachine;

/**
 * The {@link SubSynthMachine#getOsc2()} component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class Osc2Component extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private int cents = 0;

    @Tag(101)
    private int octave = 0;

    @Tag(102)
    private float phase = 0f;

    @Tag(103)
    private int semis = 0;

    @Tag(104)
    private CentsMode centsMode = CentsMode.CENTS;

    @Tag(105)
    private Osc2Waveform waveForm = Osc2Waveform.None;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // cents
    //----------------------------------

    /**
     * @see SubSynthMessage#OSC2_CENTS
     */
    public int getCents() {
        return cents;
    }

    int getCents(boolean restore) {
        return (int)SubSynthMessage.OSC2_CENTS.query(getRack(), getMachineIndex());
    }

    /**
     * @see SubSynthMessage#OSC2_CENTS
     * @param value (-50..50)
     */
    public void setCents(int value) {
        if (value == cents)
            return;
        if (value < -50 || value > 50)
            throw newRangeException(SubSynthMessage.OSC2_CENTS.toString(), "-50..50", value);
        cents = value;
        SubSynthMessage.OSC2_CENTS.send(getRack(), getMachineIndex(), cents);
    }

    //----------------------------------
    // octave
    //----------------------------------

    /**
     * @see SubSynthMessage#OSC2_OCTAVE
     */
    public int getOctave() {
        return octave;
    }

    int getOctave(boolean restore) {
        return (int)SubSynthMessage.OSC2_OCTAVE.query(getRack(), getMachineIndex());
    }

    /**
     * @see SubSynthMessage#OSC2_OCTAVE
     * @param value (-3..3)
     */
    public void setOctave(int value) {
        if (value == octave)
            return;
        if (value < -3 || value > 3)
            throw newRangeException(SubSynthMessage.OSC2_OCTAVE.toString(), "-3..3", value);
        octave = value;
        SubSynthMessage.OSC2_OCTAVE.send(getRack(), getMachineIndex(), octave);
    }

    //----------------------------------
    // phase
    //----------------------------------

    /**
     * @see SubSynthMessage#OSC2_PHASE
     */
    public float getPhase() {
        return phase;
    }

    float getPhase(boolean restore) {
        return SubSynthMessage.OSC2_PHASE.query(getRack(), getMachineIndex());
    }

    /**
     * @see SubSynthMessage#OSC2_PHASE
     * @param value (-0.5..0.5)
     */
    public void setPhase(float value) {
        if (value == phase)
            return;
        if (value < -0.5f || value > 0.5f)
            throw newRangeException(SubSynthMessage.OSC2_PHASE.toString(), "-0.5..0.5", value);
        phase = value;
        SubSynthMessage.OSC2_PHASE.send(getRack(), getMachineIndex(), phase);
    }

    //----------------------------------
    // semis
    //----------------------------------

    /**
     * @see SubSynthMessage#OSC2_SEMIS
     */
    public int getSemis() {
        return semis;
    }

    int getSemis(boolean restore) {
        return (int)SubSynthMessage.OSC2_SEMIS.query(getRack(), getMachineIndex());
    }

    /**
     * @see SubSynthMessage#OSC2_SEMIS
     * @param value (-12..12)
     */
    public void setSemis(int value) {
        if (value == semis)
            return;
        if (value < -12 || value > 12)
            throw newRangeException(SubSynthMessage.OSC2_SEMIS.toString(), "-12..12", value);
        semis = value;
        SubSynthMessage.OSC2_SEMIS.send(getRack(), getMachineIndex(), semis);
    }

    //----------------------------------
    // centsMode
    //----------------------------------

    /**
     * @see SubSynthMessage#OSC2_CENTS_MODE
     */
    public CentsMode getCentsMode() {
        return centsMode;
    }

    CentsMode getCentsMode(boolean restore) {
        return CentsMode.toType((int)SubSynthMessage.OSC2_CENTS_MODE.query(getRack(),
                getMachineIndex()));
    }

    /**
     * @see SubSynthMessage#OSC2_CENTS_MODE
     * @param value CentsMode
     */
    public void setCentsMode(CentsMode value) {
        if (value == centsMode)
            return;
        centsMode = value;
        SubSynthMessage.OSC2_CENTS_MODE.send(getRack(), getMachineIndex(), centsMode.getValue());
    }

    //----------------------------------
    // waveForm
    //----------------------------------

    /**
     * @see SubSynthMessage#OSC2_WAVEFORM
     */
    public Osc2Waveform getWaveform() {
        return waveForm;
    }

    Osc2Waveform getWaveform(boolean restore) {
        return Osc2Waveform.toType(SubSynthMessage.OSC2_WAVEFORM
                .query(getRack(), getMachineIndex()));
    }

    /**
     * @see SubSynthMessage#OSC2_WAVEFORM
     * @param value Osc2WaveForm
     */
    public void setWaveform(Osc2Waveform value) {
        if (value == waveForm)
            return;
        waveForm = value;
        SubSynthMessage.OSC2_WAVEFORM.send(getRack(), getMachineIndex(), waveForm.getValue());
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public Osc2Component() {
    }

    public Osc2Component(MachineNode machineNode) {
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
        SubSynthMessage.OSC2_CENTS.send(getRack(), getMachineIndex(), cents);
        SubSynthMessage.OSC2_OCTAVE.send(getRack(), getMachineIndex(), octave);
        SubSynthMessage.OSC2_PHASE.send(getRack(), getMachineIndex(), phase);
        SubSynthMessage.OSC2_SEMIS.send(getRack(), getMachineIndex(), semis);
        SubSynthMessage.OSC2_CENTS_MODE.send(getRack(), getMachineIndex(), centsMode.getValue());
        SubSynthMessage.OSC2_WAVEFORM.send(getRack(), getMachineIndex(), waveForm.getValue());
    }

    @Override
    protected void restoreComponents() {
        setCents(getCents(true));
        setCentsMode(getCentsMode(true));
        setOctave(getOctave(true));
        setPhase(getPhase(true));
        setSemis(getSemis(true));
        setWaveform(getWaveform(true));
    }
}
