////////////////////////////////////////////////////////////////////////////////
//Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0 
//
//Unless required by applicable law or agreed to in writing, software 
//distributed under the License is distributed on an "AS IS" BASIS, 
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and 
//limitations under the License
//
//Author: Michael Schmalle, Principal Architect
//mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.node.machine.sequencer;

import com.teotigraphix.caustk.node.NodeBase;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class ClipEntryNode extends NodeBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private int machineIndex = -1;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // machineIndex
    //----------------------------------

    /**
     * The owning {@link ClipComponent}'s machine index.
     */
    public int getMachineIndex() {
        return machineIndex;
    }

    /**
     * Sets the machine index.
     * 
     * @param machineIndex The machine index (0..13).
     */
    public void setMachineIndex(int machineIndex) {
        this.machineIndex = machineIndex;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public ClipEntryNode() {
    }

    public ClipEntryNode(int machineIndex) {
        this.machineIndex = machineIndex;
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
    }

    @Override
    protected void restoreComponents() {
    }
}
