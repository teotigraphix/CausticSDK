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

import com.teotigraphix.caustk.core.osc.PadSynthMessage;
import com.teotigraphix.caustk.core.osc.PadSynthMessage.LFO1Target;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.PadSynthMachine;

/**
 * The {@link PadSynthMachine} lfo1 component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see PadSynthMachine#getLFO1()
 */
public class LFO1Component extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    protected LFO1Target target = LFO1Target.Off;

    protected int rate = 6;

    protected float depth = 0f;

    protected float phase = 0f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // target
    //----------------------------------

    /**
     * @see PadSynthMessage#LFO1_TARGET
     */
    public LFO1Target getTarget() {
        return target;
    }

    public LFO1Target queryTarget() {
        return LFO1Target.fromInt(PadSynthMessage.LFO1_TARGET.query(getRack(), getMachineIndex()));
    }

    /**
     * @param target LFO1Target
     * @see PadSynthMessage#LFO1_TARGET
     */
    public void setTarget(LFO1Target target) {
        if (target == this.target)
            return;
        this.target = target;
        PadSynthMessage.LFO1_TARGET.send(getRack(), getMachineIndex(), target.getValue());
    }

    //----------------------------------
    // rate
    //----------------------------------

    /**
     * @see PadSynthMessage#LFO1_RATE
     */
    public int getRate() {
        return rate;
    }

    public int queryRate() {
        return (int)PadSynthMessage.LFO1_RATE.query(getRack(), getMachineIndex());
    }

    /**
     * @param rate (0..12)
     * @see PadSynthMessage#LFO1_RATE
     */
    public void setRate(int rate) {
        if (rate == this.rate)
            return;
        if (rate < 0 || rate > 12)
            throw newRangeException(PadSynthMessage.LFO1_RATE, "0..12", rate);
        this.rate = rate;
        PadSynthMessage.LFO1_RATE.send(getRack(), getMachineIndex(), rate);
    }

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see PadSynthMessage#LFO1_DEPTH
     */
    public float getDepth() {
        return depth;
    }

    public float queryDepth() {
        return PadSynthMessage.LFO1_DEPTH.query(getRack(), getMachineIndex());
    }

    /**
     * @param depth (0.0..1.0)
     * @see PadSynthMessage#LFO1_DEPTH
     */
    public void setDepth(float depth) {
        if (depth == this.depth)
            return;
        if (depth < 0f || depth > 1f)
            throw newRangeException(PadSynthMessage.LFO1_DEPTH, "0..1", depth);
        this.depth = depth;
        PadSynthMessage.LFO1_DEPTH.send(getRack(), getMachineIndex(), depth);
    }

    //----------------------------------
    // phase
    //----------------------------------

    /**
     * @see PadSynthMessage#LFO1_PHASE
     */
    public float getPhase() {
        return phase;
    }

    public float queryPhase() {
        return PadSynthMessage.LFO1_PHASE.query(getRack(), getMachineIndex());
    }

    /**
     * @param phase (-0.5..0.5)
     * @see PadSynthMessage#LFO1_PHASE
     */
    public void setPhase(float phase) {
        if (phase == this.phase)
            return;
        if (phase < -0.5f || phase > 0.5f)
            throw newRangeException(PadSynthMessage.LFO1_PHASE, "-0.5..0.5", phase);
        this.phase = phase;
        PadSynthMessage.LFO1_PHASE.send(getRack(), getMachineIndex(), phase);
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
        PadSynthMessage.LFO1_TARGET.send(getRack(), getMachineIndex(), target.getValue());
        PadSynthMessage.LFO1_RATE.send(getRack(), getMachineIndex(), rate);
        PadSynthMessage.LFO1_DEPTH.send(getRack(), getMachineIndex(), depth);
        PadSynthMessage.LFO1_PHASE.send(getRack(), getMachineIndex(), phase);
    }

    @Override
    protected void restoreComponents() {
        setDepth(queryDepth());
        setPhase(queryPhase());
        setRate(queryRate());
        setTarget(queryTarget());
    }
}
