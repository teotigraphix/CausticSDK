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

import com.teotigraphix.caustk.core.osc.BasslineMessage;
import com.teotigraphix.caustk.core.osc.BasslineMessage.Osc1Waveform;
import com.teotigraphix.caustk.node.machine.BasslineMachine;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.patch.MachineComponent;

/**
 * The bassline oscillator component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see BasslineMachine#getOsc1()
 */
public class Osc1Component extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private float accent = 0.5f;

    private float pulseWidth = 0.5f;

    private int tune = 0;

    private Osc1Waveform waveform = Osc1Waveform.SAW;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // accent
    //----------------------------------

    /**
     * @see BasslineMessage#ACCENT
     */
    public float getAccent() {
        return accent;
    }

    public float queryAccent() {
        return BasslineMessage.ACCENT.query(getRack(), getMachineIndex());
    }

    /**
     * @param accent (0.0..1.0)
     * @see BasslineMessage#ACCENT
     */
    public void setAccent(float accent) {
        if (accent == this.accent)
            return;
        if (accent < 0f || accent > 1f)
            throw newRangeException(BasslineMessage.ACCENT, "0..1", accent);
        this.accent = accent;
        BasslineMessage.ACCENT.send(getRack(), getMachineIndex(), accent);
    }

    //----------------------------------
    // pulseWidth
    //----------------------------------

    /**
     * @see BasslineMessage#PULSE_WIDTH
     */
    public float getPulseWidth() {
        return pulseWidth;
    }

    public float queryPulseWidth() {
        return BasslineMessage.PULSE_WIDTH.query(getRack(), getMachineIndex());
    }

    /**
     * @param pulseWidth (0.05..0.5)
     * @see BasslineMessage#PULSE_WIDTH
     */
    public void setPulseWidth(float pulseWidth) {
        if (pulseWidth == this.pulseWidth)
            return;
        if (pulseWidth < 0.05f || pulseWidth > 0.5f)
            throw newRangeException(BasslineMessage.PULSE_WIDTH, "0.05..0.5", pulseWidth);
        this.pulseWidth = pulseWidth;
        BasslineMessage.PULSE_WIDTH.send(getRack(), getMachineIndex(), pulseWidth);
    }

    //----------------------------------
    // tune
    //----------------------------------

    /**
     * @see BasslineMessage#TUNE
     */
    public int getTune() {
        return tune;
    }

    public int queryTune() {
        return (int)BasslineMessage.TUNE.query(getRack(), getMachineIndex());
    }

    /**
     * @param tune (-12..12)
     * @see BasslineMessage#TUNE
     */
    public void setTune(int tune) {
        if (tune == this.tune)
            return;
        if (tune < -12 || tune > 12)
            throw newRangeException(BasslineMessage.TUNE, "-12..12", tune);
        this.tune = tune;
        BasslineMessage.TUNE.send(getRack(), getMachineIndex(), tune);
    }

    //----------------------------------
    // waveform
    //----------------------------------

    /**
     * @see BasslineMessage#WAVEFORM
     */
    public Osc1Waveform getWaveform() {
        return waveform;
    }

    public Osc1Waveform queryWaveform() {
        return Osc1Waveform.toType(BasslineMessage.WAVEFORM.query(getRack(), getMachineIndex()));
    }

    /**
     * @param waveform Osc1Waveform
     * @see BasslineMessage#WAVEFORM
     */
    public void setWaveform(Osc1Waveform waveform) {
        if (waveform == this.waveform)
            return;
        this.waveform = waveform;
        BasslineMessage.WAVEFORM.send(getRack(), getMachineIndex(), waveform.getValue());
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
        BasslineMessage.ACCENT.send(getRack(), getMachineIndex(), accent);
        BasslineMessage.PULSE_WIDTH.send(getRack(), getMachineIndex(), pulseWidth);
        BasslineMessage.TUNE.send(getRack(), getMachineIndex(), tune);
        BasslineMessage.WAVEFORM.send(getRack(), getMachineIndex(), waveform.getValue());
    }

    @Override
    protected void restoreComponents() {
        setAccent(queryAccent());
        setPulseWidth(queryPulseWidth());
        setTune(queryTune());
        setWaveform(queryWaveform());
    }
}
