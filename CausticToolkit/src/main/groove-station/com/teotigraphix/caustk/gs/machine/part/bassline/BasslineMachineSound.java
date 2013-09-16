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

package com.teotigraphix.caustk.gs.machine.part.bassline;

import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.machine.part.MachineSound;
import com.teotigraphix.caustk.gs.memory.MemorySlotItem;
import com.teotigraphix.caustk.gs.memory.item.PatternMemoryItem;
import com.teotigraphix.caustk.gs.memory.item.PhraseMemoryItem;
import com.teotigraphix.caustk.sequencer.track.Note;

public class BasslineMachineSound extends MachineSound {

    public BasslineMachineSound(GrooveMachine grooveMachine) {
        super(grooveMachine);
    }

    @Override
    protected PatternMemoryItem createPatternInitData() {
        PatternMemoryItem item = new PatternMemoryItem();
        item.setLength(1);
        item.setTempo(120f);
        return item;
    }

    @Override
    protected MemorySlotItem createPhraseInitData() {
        PhraseMemoryItem item = new PhraseMemoryItem();
        StringBuilder sb = new StringBuilder();
        sb.append(new Note(60, 0f, 0.25f, 1f, 0).serialze());
        sb.append("|");
        sb.append(new Note(60, 1f, 1.25f, 1f, 0).serialze());
        sb.append("|");
        sb.append(new Note(60, 2f, 2.25f, 1f, 0).serialze());
        sb.append("|");
        sb.append(new Note(60, 3f, 3.25f, 1f, 0).serialze());
        item.setInitNoteData(sb.toString());
        return item;
    }

}
