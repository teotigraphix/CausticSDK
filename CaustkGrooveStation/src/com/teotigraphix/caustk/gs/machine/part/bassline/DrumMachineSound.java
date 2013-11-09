
package com.teotigraphix.caustk.gs.machine.part.bassline;

import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.machine.part.MachineSound;
import com.teotigraphix.caustk.gs.memory.MemorySlotItem;
import com.teotigraphix.caustk.gs.memory.item.PatternMemoryItem;
import com.teotigraphix.caustk.gs.memory.item.PhraseMemoryItem;
import com.teotigraphix.caustk.gs.pattern.Part;
import com.teotigraphix.caustk.workstation.Note;

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
    protected MemorySlotItem createPhraseInitData(Part part) {
        // 48 - 55
        PhraseMemoryItem item = new PhraseMemoryItem();
        StringBuilder sb = new StringBuilder();
        sb.append(new Note(48, 0f, 0.25f, 1f, 0).getNoteData());
        sb.append("|");
        sb.append(new Note(48, 1f, 1.25f, 1f, 0).getNoteData());
        sb.append("|");
        sb.append(new Note(48, 2f, 2.25f, 1f, 0).getNoteData());
        sb.append("|");
        sb.append(new Note(48, 3f, 3.25f, 1f, 0).getNoteData());
        item.setInitNoteData(sb.toString());
        return item;
    }

}
