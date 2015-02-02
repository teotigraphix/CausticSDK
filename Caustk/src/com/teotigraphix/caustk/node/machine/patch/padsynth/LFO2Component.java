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

import com.teotigraphix.caustk.node.machine.Machine;
import com.teotigraphix.caustk.node.machine.PadSynthMachine;

/**
 * The {@link PadSynthMachine} lfo2 component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see PadSynthMachine#getLFO2()
 */
public class LFO2Component extends LFO1Component {

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public LFO2Component() {
        super();
    }

    public LFO2Component(Machine machineNode) {
        super(machineNode);
    }
}
