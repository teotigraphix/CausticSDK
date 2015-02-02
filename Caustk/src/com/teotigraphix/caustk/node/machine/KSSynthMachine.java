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

package com.teotigraphix.caustk.node.machine;

import com.teotigraphix.caustk.node.RackInstance;

/**
 * The Caustic <strong>KSSynth</strong> OSC decorator.
 * 
 * @author Michael Schmalle
 * @since 3.1
 */
public class KSSynthMachine extends Machine {

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public KSSynthMachine() {
    }

    public KSSynthMachine(RackInstance rackNode, int index, String name) {
        super(rackNode, index, MachineType.KSSynth, name);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void intialize() {
        super.intialize();
    }

    @Override
    protected void createComponents() {
        super.createComponents();
    }

    @Override
    protected void updateComponents() {
        super.updateComponents();
    }

    @Override
    protected void restorePresetProperties() {
    }
}
