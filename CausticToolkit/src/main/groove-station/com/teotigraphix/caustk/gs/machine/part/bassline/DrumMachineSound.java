
package com.teotigraphix.caustk.gs.machine.part.bassline;

import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.machine.part.MachineSound;
import com.teotigraphix.caustk.gs.memory.MemorySlotItem;
import com.teotigraphix.caustk.gs.memory.item.PatternMemoryItem;
import com.teotigraphix.caustk.gs.memory.item.PhraseMemoryItem;
import com.teotigraphix.caustk.gs.pattern.Note;

public class DrumMachineSound extends MachineSound {

    public DrumMachineSound(GrooveMachine grooveMachine) {
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
        // 48 - 55
        PhraseMemoryItem item = new PhraseMemoryItem();
        StringBuilder sb = new StringBuilder();
        sb.append(new Note(0f, 48, 0.25f, 1f, 0).serialze());
        sb.append("|");
        sb.append(new Note(1f, 48, 0.25f, 1f, 0).serialze());
        sb.append("|");
        sb.append(new Note(2f, 48, 0.25f, 1f, 0).serialze());
        sb.append("|");
        sb.append(new Note(3f, 48, 0.25f, 1f, 0).serialze());
        item.setInitNoteData(sb.toString());
        return item;
    }

}
