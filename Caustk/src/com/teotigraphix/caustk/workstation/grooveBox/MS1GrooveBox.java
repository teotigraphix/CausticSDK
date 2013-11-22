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

package com.teotigraphix.caustk.workstation.grooveBox;

import com.teotigraphix.caustk.workstation.ComponentInfo;
import com.teotigraphix.caustk.workstation.GrooveBox;
import com.teotigraphix.caustk.workstation.GrooveBoxDescriptor;
import com.teotigraphix.caustk.workstation.GrooveBoxType;
import com.teotigraphix.caustk.workstation.GrooveSet;
import com.teotigraphix.caustk.workstation.MachineType;

/**
 * @author Michael Schmalle
 */
public class MS1GrooveBox extends GrooveBox {

    public MS1GrooveBox() {
    }

    public MS1GrooveBox(ComponentInfo info, GrooveSet grooveSet) {
        super(info, grooveSet);

        GrooveBoxDescriptor descriptor = new GrooveBoxDescriptor(GrooveBoxType.MS1Machine);

        // when the part is created it will be named 'ms1_p1'
        descriptor.addPart("p1", MachineType.Beatbox);
        descriptor.addPart("p2", MachineType.Beatbox);

        descriptor.addPart("p3", MachineType.Bassline);
        descriptor.addPart("p4", MachineType.SubSynth);
        descriptor.addPart("p5", MachineType.Modular);
        descriptor.addPart("p6", MachineType.SubSynth); // FMSynth
        descriptor.addPart("p7", MachineType.PadSynth);
        descriptor.addPart("p8", MachineType.PCMSynth);

        setDescriptor(descriptor);
    }

}
