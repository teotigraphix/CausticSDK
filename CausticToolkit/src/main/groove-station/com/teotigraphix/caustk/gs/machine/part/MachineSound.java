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
import com.teotigraphix.caustk.gs.memory.Memory.Category;
import com.teotigraphix.caustk.gs.memory.MemorySlotItem;
import com.teotigraphix.caustk.gs.memory.item.PatternMemoryItem;
import com.teotigraphix.caustk.gs.memory.item.PhraseMemoryItem;

/*
 * This class will implement and wrap the Tone API for each machine.
 * 
 * It will allow the Machine's control user interface to implement
 * whatever it wants and access this class's dispatching and OSC action API.
 * 
 * The class has no user interface.
 */

/**
 * Holds the model and OSC actions for the {@link MachineControls} part.
 */
public class MachineSound extends MachineComponentPart {

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MachineSound(GrooveMachine grooveMachine) {
        super(grooveMachine);
    }

    public MemorySlotItem createInitData(Category category) {
        switch (category) {
            case PATCH:
                return null;
            case PATTERN:
                return createPatternInitData();
            case PATTERN_SET:
                return null;
            case PHRASE:
                return createPhraseInitData();
            case RPSSET:
                return null;
            case SONG:
                return null;
        }
        return null;
    }

    protected PatternMemoryItem createPatternInitData() {
        return new PatternMemoryItem();
    }

    protected MemorySlotItem createPhraseInitData() {
        return new PhraseMemoryItem();
    }

}
