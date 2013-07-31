
package com.teotigraphix.caustk.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.teotigraphix.caustk.tone.ToneDescriptor;

/**
 * The {@link LibraryPattern} is a collection of library items linked together
 * by the name tag of the original machine.
 * <p>
 * In a 14 track caustic file, each track is labeled PART1A, PART1B etc. Each
 * part is then combined together to form the full part items for the pattern.
 */
public class LibraryPattern extends LibraryItem {

    private ToneSet toneSet;

    public final ToneSet getToneSet() {
        return toneSet;
    }

    public final void setToneSet(ToneSet value) {
        toneSet = value;
    }

    public LibraryPattern() {

    }

    public int getIndex() {
        // TODO Auto-generated method stub
        //XXX REMOVE since this shouldn't be useful with new LibraryPatterns
        return 0;
    }

    public float getTempo() {
        // TODO Auto-generated method stub
        return 120;
    }

    public int getLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    public static class ToneSet {
        private List<ToneDescriptor> descriptors = new ArrayList<ToneDescriptor>();

        public final List<ToneDescriptor> getDescriptors() {
            return descriptors;
        }

        public ToneSet(List<ToneDescriptor> descriptors) {
            this.descriptors = descriptors;
        }
    }

    private Map<Integer, UUID> partPhrases = new HashMap<Integer, UUID>();

    public void putPhrase(int partIndex, LibraryPhrase phrase) {
        if (phrase == null)
            return;
        partPhrases.put(partIndex, phrase.getId());
    }

    public UUID getPhrase(int index) {
        return partPhrases.get(index);
    }

}
