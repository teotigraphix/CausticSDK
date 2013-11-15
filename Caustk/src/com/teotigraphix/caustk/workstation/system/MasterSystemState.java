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

package com.teotigraphix.caustk.workstation.system;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.workstation.MasterSystem;

/**
 * The main system state model for the {@link MasterSystem} component.
 * <p>
 * This instance is not serialized with the {@link MasterSystem}.
 * 
 * @author Michael Schmalle
 */
public class MasterSystemState {

    List<SystemState> states = new ArrayList<SystemState>();

    private ICaustkController controller;

    public MasterSystemState(ICaustkController controller) {
        this.controller = controller;
    }

    public void add(SystemState state) {
        states.add(state);

        for (SystemStateItem item : state.getItems()) {
            if (item.hasCommand()) {
                controller.put(item.getMessage(), item.getCommand());
            }
        }
    }

    public SystemState[] getStates() {
        return states.toArray(new SystemState[] {});
    }

    public SystemStateItem getItem(int stateIndex, int stateItemIndex) {
        SystemState state = states.get(stateIndex);
        return state.getItem(stateItemIndex);
    }

}
