
package com.teotigraphix.caustk.gs.memory.item;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustk.gs.memory.MemorySlotItem;

public class PatternMemoryItem extends MemorySlotItem {

    public PatternMemoryItem() {
    }

    private float tempo;

    public float getTempo() {
        return tempo;
    }

    public void setTempo(float tempo) {
        this.tempo = tempo;
    }

    private int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    private List<PhraseMemoryItem> phrases = new ArrayList<PhraseMemoryItem>(14);

    public PhraseMemoryItem getPhrase(int index) {
        if (index > phrases.size() - 1)
            return null;
        return phrases.get(index);
    }

    public void setPhrase(int index, PhraseMemoryItem item) {
        item.setIndex(index);
        phrases.add(index, item);
    }

}
