
package com.teotigraphix.caustk.gs.memory;

import com.teotigraphix.caustk.gs.machine.GrooveMachine;

public class PresetMemoryBank extends MemoryBank {

    public PresetMemoryBank(GrooveMachine machine) {
        super(machine, Type.PRESET, "Preset");
        put(Category.PATCH, new MemorySlot(Category.PATCH));
        put(Category.PATTERN, new MemorySlot(Category.PATTERN));
        put(Category.PHRASE, new MemorySlot(Category.PHRASE));
        put(Category.SONG, new MemorySlot(Category.SONG));
    }

}
