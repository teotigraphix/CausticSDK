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

package com.teotigraphix.caustk.tone;

import com.teotigraphix.caustic.machine.MachineType;

/**
 * Each application must define the {@link Tone} instances that will get created
 * at startup.
 */
public class ToneDescriptor {
    private final int index;

    public int getIndex() {
        return index;
    }

    private final String id;

    public String getId() {
        return id;
    }

    private final MachineType machineType;

    public MachineType getMachineType() {
        return machineType;
    }

    public ToneDescriptor(int index, String name, MachineType machineType) {
        this.index = index;
        this.id = name;
        this.machineType = machineType;
    }
}
