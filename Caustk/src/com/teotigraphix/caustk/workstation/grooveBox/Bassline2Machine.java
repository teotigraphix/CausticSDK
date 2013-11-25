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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.caustk.workstation.ComponentInfo;
import com.teotigraphix.caustk.workstation.GrooveBox;
import com.teotigraphix.caustk.workstation.GrooveBoxDescriptor;
import com.teotigraphix.caustk.workstation.GrooveBoxType;
import com.teotigraphix.caustk.workstation.GrooveSet;
import com.teotigraphix.caustk.workstation.MachineType;

/**
 * @author Michael Schmalle
 */
public class Bassline2Machine extends GrooveBox {

    public Bassline2Machine() {
    }

    public Bassline2Machine(ComponentInfo info, GrooveSet grooveSet) throws FileNotFoundException {
        super(info, grooveSet);

        GrooveBoxDescriptor descriptor = new GrooveBoxDescriptor(GrooveBoxType.BasslineMachine2);
        // when the part is created it will be named 'bl1_part1'

        File file = RuntimeUtils.getCausticPresetsFile(MachineType.Bassline, "DRIVE IT");
        if (!file.exists())
            throw new FileNotFoundException();

        byte[] data = null;
        try {
            data = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        descriptor.addPart("p1", MachineType.Bassline, data);
        descriptor.addPart("p2", MachineType.Bassline, data);

        setDescriptor(descriptor);
    }

}
