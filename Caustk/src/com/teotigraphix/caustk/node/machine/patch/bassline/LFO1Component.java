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
import com.teotigraphix.caustk.core.osc.BasslineMessage.LFOTarget;
import com.teotigraphix.caustk.node.machine.BasslineMachine;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.patch.MachineComponent;

/**
 * The bassline lfo component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see BasslineMachine#getLFO1()
 */
public class LFO1Component extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    protected int rate = 1;

    private float depth = 0.0f;

    private float phase = 0f;

    private LFOTarget target = LFOTarget.OFF;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // rate
    //----------------------------------

    /**
     * @see BasslineMessage#LFO_RATE
     */
    public int getRate() {
        return rate;
    }

    public int queryRate() {
        return (int)BasslineMessage.LFO_RATE.query(getRack(), getMachineIndex());
    }

    /**
     * @param rate (1..12)
     * @see BasslineMessage#LFO_RATE
     */
    public void setRate(int rate) {
        if (rate == this.rate)
            return;
        if (rate < 0 || rate > 12)
            throw newRangeException(BasslineMessage.LFO_RATE, "0..12", rate);
        this.rate = rate;
        BasslineMessage.LFO_RATE.send(getRack(), getMachineIndex(), rate);
    }

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see BasslineMessage#LFO_DEPTH
     */
    public float getDepth() {
        return depth;
    }

    public float queryDepth() {
        return BasslineMessage.LFO_DEPTH.query(getRack(), getMachineIndex());
    }

    /**
     * @param depth (0.0..1.0)
     * @see BasslineMessage#LFO_DEPTH
     */
    public void setDepth(float depth) {
        if (depth == this.depth)
            return;
        if (depth < 0f || depth > 1f)
            throw newRangeException(BasslineMessage.LFO_DEPTH, "0..1", depth);
        this.depth = depth;
        BasslineMessage.LFO_DEPTH.send(getRack(), getMachineIndex(), depth);
    }

    //----------------------------------
    // phase
    //----------------------------------

    /**
     * @see BasslineMessage#LFO_PHASE
     */
    public float getPhase() {
        return phase;
    }

    public float queryPhase() {
        return BasslineMessage.LFO_PHASE.query(getRack(), getMachineIndex());
    }

    /**
     * @param phase (0.0..1.0)
     * @see BasslineMessage#LFO_PHASE
     */
    public void setPhase(float phase) {
        if (phase == this.phase)
            return;
        if (phase < 0f || phase > 1f)
            throw newRangeException(BasslineMessage.LFO_PHASE, "0..1", phase);
        this.phase = phase;
        BasslineMessage.LFO_PHASE.send(getRack(), getMachineIndex(), phase);
    }

    //----------------------------------
    // target
    //----------------------------------

    /**
     * @see BasslineMessage#LFO_TARGET
     */
    public LFOTarget getTarget() {
        return target;
    }

    public LFOTarget queryTarget() {
        return LFOTarget.toType(BasslineMessage.LFO_TARGET.query(getRack(), getMachineIndex()));
    }

    /**
     * @param target LFOTarget
     * @see BasslineMessage#LFO_TARGET
     */
    public void setTarget(LFOTarget target) {
        if (target == this.target)
            return;
        this.target = target;
        BasslineMessage.LFO_TARGET.send(getRack(), getMachineIndex(), target.getValue());
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public LFO1Component() {
        rate = 0;
    }

    public LFO1Component(MachineNode machineNode) {
        super(machineNode);
        rate = 0;
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
        BasslineMessage.LFO_DEPTH.send(getRack(), getMachineIndex(), depth);
        BasslineMessage.LFO_RATE.send(getRack(), getMachineIndex(), rate);
        BasslineMessage.LFO_PHASE.send(getRack(), getMachineIndex(), phase);
        BasslineMessage.LFO_TARGET.send(getRack(), getMachineIndex(), target.getValue());
    }

    @Override
    protected void restoreComponents() {
        setDepth(queryDepth());
        setRate(queryRate());
        setPhase(queryPhase());
        setTarget(queryTarget());
    }
}
