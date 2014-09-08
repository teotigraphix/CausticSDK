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

package com.teotigraphix.caustk.node.machine;

import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.node.machine.patch.organ.LeslieComponent;

/**
 * The Caustic <strong>Organ</strong> OSC decorator.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class OrganMachine extends MachineNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private LeslieComponent leslie;

    //--------------------------------------------------------------------------
    // Components
    //--------------------------------------------------------------------------

    //----------------------------------
    // leslie
    //----------------------------------

    public LeslieComponent getLeslie() {
        return leslie;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public OrganMachine() {
    }

    public OrganMachine(int index, String name) {
        super(index, MachineType.Organ, name);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void intialize() {
        super.intialize();
        leslie = new LeslieComponent(this);
    }

    @Override
    protected void createComponents() {
        super.createComponents();
        leslie.create();
    }

    @Override
    protected void destroyComponents() {
        super.destroyComponents();
        leslie.destroy();
    }

    @Override
    protected void updateComponents() {
        super.updateComponents();
        leslie.update();
    }

    @Override
    protected void restorePresetProperties() {
        leslie.restore();
    }
}
