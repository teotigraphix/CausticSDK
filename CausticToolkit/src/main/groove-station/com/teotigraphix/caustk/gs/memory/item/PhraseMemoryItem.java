
package com.teotigraphix.caustk.gs.memory.item;

import com.teotigraphix.caustk.gs.memory.MemorySlotItem;
import com.teotigraphix.caustk.tone.components.PatternSequencerComponent.Resolution;

public class PhraseMemoryItem extends MemorySlotItem {

    // length, tempo managed by PatternMemoryItem

    // init note data

    public Resolution getResolution() {
        return Resolution.SIXTEENTH;
    }

    private String initNoteData;

    public String getInitNoteData() {
        return initNoteData;
    }

    public void setInitNoteData(String value) {
        initNoteData = value;
    }

    // swing

    public PhraseMemoryItem() {
    }

}
