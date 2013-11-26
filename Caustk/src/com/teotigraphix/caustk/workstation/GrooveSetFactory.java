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

package com.teotigraphix.caustk.workstation;

import java.io.FileNotFoundException;

import com.teotigraphix.caustk.workstation.grooveBox.Bassline2;
import com.teotigraphix.caustk.workstation.grooveBox.MixSynth1;

/**
 * @author Michael Schmalle
 */
public class GrooveSetFactory extends CaustkSubFactoryBase {

    public GrooveSetFactory() {
    }

    public GrooveSet createGrooveSet(RackSet rackSet) {
        ComponentInfo info = getFactory().createInfo(ComponentType.GrooveSet);
        GrooveSet grooveSet = new GrooveSet(info, rackSet);
        return grooveSet;
    }

    public GrooveSet createGrooveSet(ComponentInfo info, RackSet rackSet) {
        GrooveSet grooveSet = new GrooveSet(info, rackSet);
        return grooveSet;
    }

    public GrooveBox createGrooveBox(ComponentInfo info, GrooveSet grooveSet,
            GrooveBoxType machineType) {
        GrooveBox machine = null;
        switch (machineType) {
            case Bassline2:
                try {
                    machine = new Bassline2(info, grooveSet);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case Drum2:
                break;
            case MixSynth1:
                machine = new MixSynth1(info, grooveSet);
                break;
        }

        return machine;
    }

    public GrooveBox createGrooveBox(GrooveSet grooveSet, GrooveBoxType machineType) {
        ComponentInfo info = getFactory().createInfo(ComponentType.GrooveBox);
        return createGrooveBox(info, grooveSet, machineType);
    }

}
