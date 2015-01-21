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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.machine.patch.modular.ModularBayComponent;

/**
 * The Caustic <strong>Modular</strong> OSC decorator.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class ModularMachine extends MachineNode {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private ModularBayComponent bay;

    //--------------------------------------------------------------------------
    // Components
    //--------------------------------------------------------------------------

    //----------------------------------
    // bay
    //----------------------------------

    public ModularBayComponent getBay() {
        return bay;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public ModularMachine() {
    }

    public ModularMachine(RackNode rackNode, int index, String name) {
        super(rackNode, index, MachineType.Modular, name);
    }

    // To create:
    // /caustic/create modular <name> [slot#]

    // to change the synth's only real "global" control
    // /caustic/modular/volume [value] where value's range is [0..2]

    // To delete a component
    // /caustic/modular/remove <component bay#>

    // To query the type of a component
    // /caustic/modular/type <component bay#> returns the type# from the list above

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void intialize() {
        super.intialize();
        //setVolume(new ModularVolumeBug(this));
        bay = new ModularBayComponent(this);
    }

    @Override
    protected void updateComponents() {
        super.updateComponents();
        bay.update();
    }

    @Override
    protected void restorePresetProperties() {
        bay.restore();
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
    }

}
