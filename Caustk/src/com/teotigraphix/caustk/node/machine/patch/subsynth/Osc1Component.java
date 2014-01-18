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

import com.teotigraphix.caustk.core.osc.SubSynthMessage;
import com.teotigraphix.caustk.core.osc.SubSynthMessage.ModulationMode;
import com.teotigraphix.caustk.core.osc.SubSynthMessage.Osc1Waveform;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.SubSynthMachine;
import com.teotigraphix.caustk.node.machine.patch.MachineComponent;

/**
 * The {@link SubSynthMachine#getOsc1()} component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class Osc1Component extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private float bend = 0.0f;

    private float modulation = 0.0f;

    private ModulationMode modulationMode = ModulationMode.Fm;

    private float mix = 0.5f;

    private Osc1Waveform waveform = Osc1Waveform.Sine;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // bend
    //----------------------------------

    /**
     * @see SubSynthMessage#OSC_BEND
     */
    public float getBend() {
        return bend;
    }

    public float queryBend() {
        return SubSynthMessage.OSC_BEND.query(getRack(), getMachineIndex());
    }

    /**
     * @see SubSynthMessage#OSC_BEND
     * @param value (0.0..1.0)
     */
    public void setBend(float value) {
        if (value == bend)
            return;
        if (value < 0 || value > 1f)
            throw newRangeException(SubSynthMessage.OSC_BEND.toString(), "0..1", value);
        bend = value;
        SubSynthMessage.OSC_BEND.send(getRack(), getMachineIndex(), bend);
    }

    //----------------------------------
    // modulation
    //----------------------------------

    /**
     * @see SubSynthMessage#OSC1_MODULATION
     */
    public float getModulation() {
        return modulation;
    }

    public float queryModulation() {
        return SubSynthMessage.OSC1_MODULATION.query(getRack(), getMachineIndex());
    }

    /**
     * @see SubSynthMessage#OSC1_MODULATION
     * @param value (0.0..1.0)
     */
    public void setModulation(float value) {
        if (value == modulation)
            return;
        if (value < 0 || value > 1f)
            throw newRangeException(SubSynthMessage.OSC1_MODULATION.toString(), "0..1", value);
        modulation = value;
        SubSynthMessage.OSC1_MODULATION.send(getRack(), getMachineIndex(), modulation);
    }

    //----------------------------------
    // modulationMode
    //----------------------------------

    /**
     * @see SubSynthMessage#OSC1_MODULATION_MODE
     */
    public ModulationMode getModulationMode() {
        return modulationMode;
    }

    public ModulationMode queryModulationMode() {
        return ModulationMode.toType((int)SubSynthMessage.OSC1_MODULATION_MODE.query(getRack(),
                getMachineIndex()));
    }

    /**
     * @see SubSynthMessage#OSC1_MODULATION_MODE
     * @param value {@link ModulationMode}
     */
    public void setModulationMode(ModulationMode value) {
        if (value == modulationMode)
            return;
        modulationMode = value;
        SubSynthMessage.OSC1_MODULATION_MODE.send(getRack(), getMachineIndex(),
                modulationMode.getValue());
    }

    //----------------------------------
    // mix
    //----------------------------------

    /**
     * @see SubSynthMessage#OSC_MIX
     */
    public float getMix() {
        return mix;
    }

    public float queryMix() {
        return SubSynthMessage.OSC_MIX.query(getRack(), getMachineIndex());
    }

    /**
     * @see SubSynthMessage#OSC_MIX
     * @param value (0.0..1.0)
     */
    public void setMix(float value) {
        if (value == mix)
            return;
        if (value < 0 || value > 1.0f)
            throw newRangeException(SubSynthMessage.OSC_MIX.toString(), "0..1", value);
        mix = value;
        SubSynthMessage.OSC_MIX.send(getRack(), getMachineIndex(), mix);
    }

    //----------------------------------
    // waveform
    //----------------------------------

    /**
     * @see SubSynthMessage#OSC1_WAVEFORM
     */
    public Osc1Waveform getWaveform() {
        return waveform;
    }

    public Osc1Waveform queryWaveform() {
        return Osc1Waveform.toType(SubSynthMessage.OSC1_WAVEFORM
                .query(getRack(), getMachineIndex()));
    }

    /**
     * @see SubSynthMessage#OSC1_WAVEFORM
     * @param value Osc1Waveform
     */
    public void setWaveform(Osc1Waveform value) {
        if (value == waveform)
            return;
        waveform = value;
        SubSynthMessage.OSC1_WAVEFORM.send(getRack(), getMachineIndex(), waveform.getValue());
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public Osc1Component() {
    }

    public Osc1Component(MachineNode machineNode) {
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
        SubSynthMessage.OSC_BEND.send(getRack(), getMachineIndex(), bend);
        SubSynthMessage.OSC1_MODULATION.send(getRack(), getMachineIndex(), modulation);
        SubSynthMessage.OSC1_MODULATION_MODE.send(getRack(), getMachineIndex(),
                modulationMode.getValue());
        SubSynthMessage.OSC_MIX.send(getRack(), getMachineIndex(), mix);
        SubSynthMessage.OSC1_WAVEFORM.send(getRack(), getMachineIndex(), waveform.getValue());
    }

    @Override
    protected void restoreComponents() {
        setBend(queryBend());
        setModulation(queryModulation());
        setModulationMode(queryModulationMode());
        setMix(queryMix());
        setWaveform(queryWaveform());
    }
}
