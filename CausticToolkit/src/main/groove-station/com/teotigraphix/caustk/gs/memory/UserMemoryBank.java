
package com.teotigraphix.caustk.gs.memory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.memory.item.PatternMemoryItem;
import com.teotigraphix.caustk.gs.memory.item.PhraseMemoryItem;
import com.teotigraphix.caustk.gs.pattern.Part;
import com.teotigraphix.caustk.gs.pattern.Pattern;
import com.teotigraphix.caustk.sequencer.track.Track;
import com.teotigraphix.caustk.sequencer.track.Phrase;

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
        getMachine().getSequencer().configure(pattern);
        return pattern;
    }

    @Override
    public Pattern getPattern(int index) {
        if (inMemoryPatterns.containsKey(index)) {
            return inMemoryPatterns.get(index);
        }

        PatternMemoryItem item = (PatternMemoryItem)getPatternSlot().getItem(index);
        if (item == null) {
            item = (PatternMemoryItem)getMachine().getSound().createInitData(Category.PATTERN);
        }

        // user memory has no PatternItem data, all defaults until the user either
        // creates data with the step sequencer or copies another pattern into this
        Pattern pattern = new Pattern(getMachine().getController(), item);
        return pattern;
    }

    @Override
    public Phrase copyPhrase(Part part, int index) {
        Phrase phrase = getPhrase(part);
        part.setPhrase(phrase);
        //        phrase.configure();
        return phrase;
    }

    @Override
    public Phrase getPhrase(Part part) {
        int index = part.getIndex();

        PatternMemoryItem item = part.getPattern().getMemoryItem();
        PhraseMemoryItem phraseMemoryItem = item.getPhrase(index);
        if (phraseMemoryItem == null) {
            phraseMemoryItem = (PhraseMemoryItem)getMachine().getSound().createInitData(
                    Category.PHRASE);
            item.setPhrase(index, phraseMemoryItem);
        }
        // the TrackSequencer holds all existing and lazy loaded pattern phrases
        Track track = getMachine().getController().getTrackSequencer()
                .getTrack(part.getToneIndex());
        Phrase phrase = track.getPhrase(part.getPattern().getBankIndex(), part.getPattern()
                .getPatternIndex());

        return phrase;
    }

    private Map<Integer, Pattern> inMemoryPatterns = new HashMap<Integer, Pattern>();

    @Override
    public Collection<Pattern> getInMemoryPatterns() {
        return inMemoryPatterns.values();
    }

    @Override
    public void writePattern(Pattern pattern) {
        int index = pattern.getIndex();
        if (!pattern.isInMemory()) {
            inMemoryPatterns.put(index, pattern);
            pattern.setInMemory(true);
        }
    }
}
