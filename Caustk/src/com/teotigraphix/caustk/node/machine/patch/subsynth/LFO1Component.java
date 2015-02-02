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
import com.teotigraphix.caustk.core.osc.SubSynthMessage.LFO1Target;
import com.teotigraphix.caustk.core.osc.SubSynthMessage.LFO1Waveform;
import com.teotigraphix.caustk.node.machine.MachineChannel;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.SubSynthMachine;

/**
 * The {@link SubSynthMachine#getLFO1()} component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class LFO1Component extends MachineChannel {

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
     * @see com.teotigraphix.caustk.core.osc.SubSynthMessage#LFO1_RATE
     */
    public int getRate() {
        return rate;
    }

    public int queryRate() {
        return (int)SubSynthMessage.LFO1_RATE.query(getRack(), getMachineIndex());
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.SubSynthMessage#LFO1_RATE
     * @param value (1..12)
     */
    public void setRate(int value) {
        if (value == rate)
            return;
        if (value < 0 || value > 12)
            throw newRangeException("lfo1_rate", "0..12", value);
        rate = value;
        SubSynthMessage.LFO1_RATE.send(getRack(), getMachineIndex(), rate);
    }

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.SubSynthMessage#LFO1_DEPTH
     */
    public float getDepth() {
        return depth;
    }

    public float queryDepth() {
        return SubSynthMessage.LFO1_DEPTH.query(getRack(), getMachineIndex());
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.SubSynthMessage#LFO1_DEPTH
     * @param value (0.0..1.0)
     */
    public void setDepth(float value) {
        if (value == depth)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("lfo1_depth", "0..1", value);
        depth = value;
        SubSynthMessage.LFO1_DEPTH.send(getRack(), getMachineIndex(), depth);
    }

    //----------------------------------
    // target
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.SubSynthMessage#LFO1_TARGET
     */
    public LFO1Target getTarget() {
        return target;
    }

    public LFO1Target queryTarget() {
        return LFO1Target.toType(SubSynthMessage.LFO1_TARGET.query(getRack(), getMachineIndex()));
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.SubSynthMessage#LFO1_TARGET
     * @param value {@link com.teotigraphix.caustk.core.osc.SubSynthMessage.LFO1Target}
     */
    public void setTarget(LFO1Target value) {
        if (value == target)
            return;
        target = value;
        SubSynthMessage.LFO1_TARGET.send(getRack(), getMachineIndex(), target.getValue());
    }

    //----------------------------------
    // waveForm
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.SubSynthMessage#LFO1_WAVEFORM
     */
    public LFO1Waveform getWaveform() {
        return waveform;
    }

    public LFO1Waveform queryWaveform() {
        return LFO1Waveform.toType(SubSynthMessage.LFO1_WAVEFORM
                .query(getRack(), getMachineIndex()));
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.SubSynthMessage#LFO1_WAVEFORM
     * @param value LFO1Waveform)
     */
    public void setWaveForm(LFO1Waveform value) {
        if (value == waveform)
            return;
        waveform = value;
        SubSynthMessage.LFO1_WAVEFORM.send(getRack(), getMachineIndex(), waveform.getValue());
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
        setDepth(queryDepth());
        setRate(queryRate());
        setTarget(queryTarget());
        setWaveForm(queryWaveform());
    }
}
