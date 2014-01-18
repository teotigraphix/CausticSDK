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

import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.patch.modular.ModularBayComponent;

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

    private int machineIndex;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // machineIndex
    //----------------------------------

    /**
     * Returns the machine's index that represents the machine's native slot
     * assignment in the rack (0..13).
     */
    public final int getMachineIndex() {
        return machineIndex;
    }

    /**
     * TODO get rid of this and implement the constructors correctly IE
     * {@link ModularBayComponent}.
     * 
     * @param machineIndex
     */
    public void updateMachineIndex(int machineIndex) {
        this.machineIndex = machineIndex;
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
