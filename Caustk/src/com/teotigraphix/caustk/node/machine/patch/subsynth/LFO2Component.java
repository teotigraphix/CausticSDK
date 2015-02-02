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
import com.teotigraphix.caustk.core.osc.SubSynthMessage.LFO2Target;
import com.teotigraphix.caustk.node.machine.MachineChannel;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.SubSynthMachine;

/**
 * The {@link SubSynthMachine#getLFO2()} component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class LFO2Component extends MachineChannel {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    protected int rate = 1;

    @Tag(101)
    private float depth = 0.0f;

    @Tag(102)
    private LFO2Target target = LFO2Target.None;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // rate
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.SubSynthMessage#LFO2_RATE
     */
    public int getRate() {
        return rate;
    }

    public int queryRate() {
        return (int)SubSynthMessage.LFO1_RATE.query(getRack(), getMachineIndex());
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.SubSynthMessage#LFO2_RATE
     * @param value (1..12)
     */
    public void setRate(int value) {
        if (value == rate)
            return;
        if (value < 0 || value > 12)
            throw newRangeException("lfo1_rate", "1..12", value);
        rate = value;
        SubSynthMessage.LFO1_RATE.send(getRack(), getMachineIndex(), rate);
    }

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.SubSynthMessage#LFO2_DEPTH
     */
    public float getDepth() {
        return depth;
    }

    public float queryDepth() {
        return SubSynthMessage.LFO1_DEPTH.query(getRack(), getMachineIndex());
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.SubSynthMessage#LFO2_DEPTH
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
     * @see com.teotigraphix.caustk.core.osc.SubSynthMessage#LFO2_TARGET
     */
    public LFO2Target getTarget() {
        return target;
    }

    public LFO2Target queryTarget() {
        return LFO2Target.toType(SubSynthMessage.LFO2_TARGET.query(getRack(), getMachineIndex()));
    }

    /**
     * @see com.teotigraphix.caustk.core.osc.SubSynthMessage#LFO2_TARGET
     * @param value LFO2Target
     */
    public void setTarget(LFO2Target value) {
        if (value == target)
            return;
        target = value;
        SubSynthMessage.LFO2_TARGET.send(getRack(), getMachineIndex(), target.getValue());
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public LFO2Component() {
    }

    public LFO2Component(MachineNode machineNode) {
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
        SubSynthMessage.LFO1_RATE.send(getRack(), getMachineIndex(), rate);
        SubSynthMessage.LFO1_DEPTH.send(getRack(), getMachineIndex(), depth);
        SubSynthMessage.LFO2_TARGET.send(getRack(), getMachineIndex(), target.getValue());
    }

    @Override
    protected void restoreComponents() {
        setDepth(queryDepth());
        setRate(queryRate());
        setTarget(queryTarget());
    }
}
