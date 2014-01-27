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

package com.teotigraphix.caustk.node.machine.patch;

import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.core.osc.OSCUtils;
import com.teotigraphix.caustk.core.osc.VolumeMessage;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The volume out component for machines.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see MachineNode#getVolume()
 */
public class VolumeComponent extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private float out;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // out
    //----------------------------------

    /**
     * @see VolumeMessage#VOLUME_OUT
     */
    public float getOut() {
        return out;
    }

    public float queryOut() {
        return VolumeMessage.VOLUME_OUT.query(getRack(), getMachineIndex());
    }

    /**
     * @param out (0.0..2.0)
     * @see VolumeMessage#VOLUME_OUT
     */
    public void setOut(float out) {
        if (out == this.out)
            return;

        MachineType machineType = OSCUtils.toMachineType(getRack(), getMachineIndex());
        if (machineType == MachineType.BeatBox || machineType == MachineType.PCMSynth) {
            if (out < 0f || out > 4f)
                throw newRangeException("beatbox out", "0..4", out);
        } else if (machineType == MachineType.FMSynth) {
            if (out < 0f || out > 1f)
                throw newRangeException("fmsynth out", "0..1", out);
        } else {
            if (out < 0f || out > 2f)
                throw newRangeException("out", "0..2", out);
        }

        this.out = out;
        VolumeMessage.VOLUME_OUT.send(getRack(), getMachineIndex(), out);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public VolumeComponent() {
    }

    public VolumeComponent(MachineNode machineNode) {
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
        VolumeMessage.VOLUME_OUT.send(getRack(), getMachineIndex(), out);
    }

    @Override
    protected void restoreComponents() {
        setOut(queryOut());
    }
}
