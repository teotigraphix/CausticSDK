
package com.teotigraphix.caustk.gs.memory;

import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.memory.item.PatternMemoryItem;
import com.teotigraphix.caustk.gs.memory.item.PhraseMemoryItem;
import com.teotigraphix.caustk.gs.pattern.Part;
import com.teotigraphix.caustk.gs.pattern.Pattern;
import com.teotigraphix.caustk.gs.pattern.Phrase;

public class UserMemoryBank extends MemoryBank {

    /*
     * User memory gets lazy loaded by index.
     */

    public UserMemoryBank(GrooveMachine machine) {
        super(machine, Type.USER, "User");
        put(Category.PATCH, new MemorySlot(Category.PATCH));
        put(Category.PATTERN, new MemorySlot(Category.PATTERN));
        put(Category.PHRASE, new MemorySlot(Category.PHRASE));
        put(Category.SONG, new MemorySlot(Category.SONG));
    }

    @Override
    public Pattern copyPattern(int index) {
        Pattern pattern = getPattern(index);
        getMachine().getMachineSequencer().configure(pattern);
        return pattern;
    }

    @Override
    public Pattern getPattern(int index) {
        PatternMemoryItem item = (PatternMemoryItem)getPatternSlot().getItem(index);
        if (item == null) {
            item = (PatternMemoryItem)getMachine().getMachineSound().createInitData(
                    Category.PATTERN);
        }

        // user memory has no PatternItem data, all defaults until the user either
        // creates data with the step sequencer or copies another pattern into this
        Pattern pattern = new Pattern(getMachine().getController(), item);
        return pattern;
    }

    @Override
    public Phrase copyPhrase(Part part, int index) {
        Phrase phrase = getPhrase(part);
        phrase.configure();
        return phrase;
    }

    @Override
    public Phrase getPhrase(Part part) {
        int index = part.getIndex();

        PatternMemoryItem item = part.getPattern().getPatternMemoryItem();
        PhraseMemoryItem phraseMemoryItem = item.getPhrase(index);
        if (phraseMemoryItem == null) {
            phraseMemoryItem = (PhraseMemoryItem)getMachine().getMachineSound().createInitData(
                    Category.PHRASE);
            item.setPhrase(index, phraseMemoryItem);
        }

        Phrase phrase = new Phrase(part, phraseMemoryItem);
        return phrase;
    }

}
