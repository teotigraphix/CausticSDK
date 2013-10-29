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

package com.teotigraphix.caustk.machine;

public class CaustkMachineFactory extends CaustkFactoryBase {

    public CaustkMachineFactory() {
    }

    public CaustkMachine createMachine(ComponentInfo info, MachineType machineType,
            String machineName) {
        CaustkMachine caustkMachine = new CaustkMachine(info, machineType, machineName);
        return caustkMachine;
    }

    public CaustkMachine createMachine(int index, MachineType machineType, String machineName) {
        ComponentInfo info = getFactory().createInfo(ComponentType.Machine);
        CaustkMachine caustkMachine = new CaustkMachine(info, index, machineType, machineName);
        caustkMachine.setRack(getFactory().getRack());
        return caustkMachine;
    }
}
