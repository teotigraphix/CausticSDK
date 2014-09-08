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

package com.teotigraphix.caustk.node.machine;

import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.node.NodeBase;

/**
 * The {@link MachineComponent} is the base class for all {@link MachineNode}
 * composite components.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public abstract class MachineComponent extends NodeBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    protected int machineIndex = -1;

    protected MachineType machineType;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    /**
     * Returns the machine index this component decorates (0..13).
     */
    public final int getMachineIndex() {
        return machineIndex;
    }

    /**
     * Sets the owning machine index.
     * 
     * @param machineIndex The owner's machine index(0..13).
     */
    public void setMachineIndex(int machineIndex) {
        this.machineIndex = machineIndex;
    }

    /**
     * Returns the machine type of the parent, may be null if not set.
     */
    public MachineType getMachineType() {
        return machineType;
    }

    public void setMachineType(MachineType machineType) {
        this.machineType = machineType;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public MachineComponent() {
    }

    public MachineComponent(int machineIndex) {
        this.machineIndex = machineIndex;
    }

    public MachineComponent(MachineNode machineNode) {
        this(machineNode.getIndex());
    }
}
