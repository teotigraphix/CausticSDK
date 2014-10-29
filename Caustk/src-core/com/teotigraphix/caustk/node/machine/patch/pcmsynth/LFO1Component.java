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
import com.teotigraphix.caustk.core.osc.PCMSynthMessage.LFO1Target;
import com.teotigraphix.caustk.core.osc.PCMSynthMessage.LFO1Waveform;
import com.teotigraphix.caustk.core.osc.SubSynthMessage;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.PCMSynthMachine;

/**
 * The {@link PCMSynthMachine#getLFO1()} component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class LFO1Component extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    protected int rate = 1;

    @Tag(101)
    private float depth = 0.0f;

    @Tag(102)
    private LFO1Target target = LFO1Target.None;

    @Tag(103)
    private LFO1Waveform waveform = LFO1Waveform.Sine;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // rate
    //----------------------------------

    /**
     * @see PCMSynthMessage#LFO_RATE
     */
    public int getRate() {
        return rate;
    }

    int getRate(boolean restore) {
        return (int)PCMSynthMessage.LFO_RATE.query(getRack(), getMachineIndex());
    }

    public void setRate(float rate) {
        setRate((int)rate);
    }

    /**
     * @param value (1..12)
     * @see PCMSynthMessage#LFO_RATE
     */
    public void setRate(int value) {
        if (value == rate)
            return;
        if (value < 1 || value > 12)
            throw newRangeException(PCMSynthMessage.LFO_RATE, "0..12", value);
        rate = value;
        PCMSynthMessage.LFO_RATE.send(getRack(), getMachineIndex(), rate);
    }

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see PCMSynthMessage#LFO_DEPTH
     */
    public float getDepth() {
        return depth;
    }

    float getDepth(boolean restore) {
        return PCMSynthMessage.LFO_DEPTH.query(getRack(), getMachineIndex());
    }

    /**
     * @param value (0.0..1.0)
     * @see PCMSynthMessage#LFO_DEPTH
     */
    public void setDepth(float value) {
        if (value == depth)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(PCMSynthMessage.LFO_DEPTH, "0..1", value);
        depth = value;
        PCMSynthMessage.LFO_DEPTH.send(getRack(), getMachineIndex(), depth);
    }

    //----------------------------------
    // target
    //----------------------------------

    /**
     * @see PCMSynthMessage#LFO_TARGET
     */
    public LFO1Target getTarget() {
        return target;
    }

    LFO1Target getTarget(boolean restore) {
        return LFO1Target.toType(PCMSynthMessage.LFO_TARGET.query(getRack(), getMachineIndex()));
    }

    public void setTarget(float target) {
        setTarget(LFO1Target.toType(target));
    }

    /**
     * @param value LFO1Target
     * @see PCMSynthMessage#LFO_TARGET
     */
    public void setTarget(LFO1Target value) {
        if (value == target)
            return;
        target = value;
        PCMSynthMessage.LFO_TARGET.send(getRack(), getMachineIndex(), target.getValue());
    }

    //----------------------------------
    // waveform
    //----------------------------------

    /**
     * @see PCMSynthMessage#LFO_WAVEFORM
     */
    public LFO1Waveform getWaveform() {
        return waveform;
    }

    LFO1Waveform getWaveform(boolean restore) {
        return LFO1Waveform
                .toType(PCMSynthMessage.LFO_WAVEFORM.query(getRack(), getMachineIndex()));
    }

    public void setWaveform(float waveform) {
        setWaveform(LFO1Waveform.toType(waveform));
    }

    /**
     * @param value LFO1Waveform
     * @see PCMSynthMessage#LFO_WAVEFORM
     */
    public void setWaveform(LFO1Waveform value) {
        if (value == waveform)
            return;
        waveform = value;
        PCMSynthMessage.LFO_WAVEFORM.send(getRack(), getMachineIndex(), waveform.getValue());
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public LFO1Component() {
    }

    public LFO1Component(MachineNode machineNode) {
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
        SubSynthMessage.LFO1_DEPTH.send(getRack(), getMachineIndex(), depth);
        SubSynthMessage.LFO1_RATE.send(getRack(), getMachineIndex(), rate);
        SubSynthMessage.LFO1_TARGET.send(getRack(), getMachineIndex(), target.getValue());
        SubSynthMessage.LFO1_WAVEFORM.send(getRack(), getMachineIndex(), waveform.getValue());
    }

    @Override
    protected void restoreComponents() {
        setDepth(getDepth(true));
        setRate(getRate(true));
        setTarget(getTarget(true));
        setWaveform(getWaveform(true));
    }
}
