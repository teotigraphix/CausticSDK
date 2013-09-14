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

package com.teotigraphix.caustk.gs.machine.part;

import com.teotigraphix.caustk.gs.machine.GrooveMachine;

/*
 * - Master volume - correlates to the machine's mixer volume
 * - Digital display
 * - Value Dial
 * - SystemStateMatrix
 * - Write button
 */

/**
 * Holds all controls for system operations.
 */
public class MachineSystem extends MachineComponentPart {

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MachineSystem(GrooveMachine grooveMachine) {
        super(grooveMachine);
    }

}
