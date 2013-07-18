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

package com.teotigraphix.caustk.project;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.core.IDispatcher;
import com.teotigraphix.caustic.internal.utils.PatternUtils;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.ISerialize;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.tone.Tone;

/*

What is a Track?
- a channel in a rack (machine) which pattern and automation state is saved over time.

A track has;
- index, the current Tone index
- name, the current Tone name, can be set different than the tone name temp
- current beat, measure proxied from the parent Song
- soft transient reference to its containing song

- patterns, Map<Integer, TrackPattern> NO REFERENCE data structure

- add/remove/get TrackPattern API
- some type of listener API

*/

public class Track implements ISerialize {

    // the phrase map is keyed on the measure start
    Map<Integer, TrackItem> items = new HashMap<Integer, TrackItem>();

    // A01->TrackPhrase
    Map<String, TrackPhrase> registry = new HashMap<String, TrackPhrase>();

    //----------------------------------
    //  dispatcher
    //----------------------------------

    private transient IDispatcher dispatcher;

    /**
     * The {@link TrackSong#getDispatcher()}.
     */
    public final IDispatcher getDispatcher() {
        return dispatcher;
    }

    public final void setDispatcher(IDispatcher value) {
        dispatcher = value;
    }

    //----------------------------------
    //  index
    //----------------------------------

    private int index;

    /**
     * The index within the core rack.
     */
    public final int getIndex() {
        return index;
    }

    public final void setIndex(int value) {
        index = value;
    }

    public Tone getTone(ICaustkController controller) {
        return TrackUtils.getTone(controller, this);
    }

    //--------------------------------------------------------------------------
    //  Constructor
    //--------------------------------------------------------------------------

    public Track() {
        initialize();
    }

    public TrackPhrase copyTrackPhrase(LibraryPhrase libraryPhrase) {
        // TrackItem are unique and hold the start and end measures
        // within a Track. The TrackPhrase is created to hold the referenced note data
        // and pointer back to the original library phrase
        // ALL TrackPhrase are copied from a source LibraryPhrase.

        // will always create a new instance and get a bank/patter slot off the stack
        BankPatternSlot slot = queue.pop();

        TrackPhrase trackPhrase = new TrackPhrase();
        trackPhrase.setId(UUID.randomUUID());
        trackPhrase.setBankIndex(slot.getBank());
        trackPhrase.setPatternIndex(slot.getPattern());
        trackPhrase.setNumMeasures(libraryPhrase.getLength());
        trackPhrase.setNoteData(libraryPhrase.getNoteData());

        String patternName = PatternUtils.toString(trackPhrase.getBankIndex(),
                trackPhrase.getPatternIndex());
        registry.put(patternName, trackPhrase);

        return trackPhrase;
    }

    public void deleteTrackPhrase(TrackPhrase trackPhrase) {
        String patternName = PatternUtils.toString(trackPhrase.getBankIndex(),
                trackPhrase.getPatternIndex());
        TrackPhrase phrase = registry.get(patternName);
        if (phrase == null) {

            return;
        }

        registry.remove(patternName);
        // remove all TrackItem ?
    }

    @SuppressWarnings("unused")
    private TrackItem findTrackItem(LibraryPhrase libraryPhrase) {
        TrackItem trackItem = items.get(libraryPhrase.getId());
        return trackItem;
    }

    @SuppressWarnings("unused")
    private TrackPhrase findTrackPhraseById(UUID id) {
        for (TrackPhrase trackPhrase : registry.values()) {
            if (trackPhrase.getId().equals(id))
                return trackPhrase;
        }
        return null;
    }

    private transient Deque<BankPatternSlot> queue;

    public TrackItem addPhrase(int numLoops, TrackPhrase trackPhrase) throws CausticException {
        int startMeasure = getNextMeasure();
        return addPhraseAt(startMeasure, numLoops, trackPhrase);
    }

    /**
     * Returns the valid next 'last' measure.
     * <p>
     * This measure is for patterns being appended to the end of the current
     * last pattern's measure.
     */
    private int getNextMeasure() {
        int next = 0;
        for (TrackItem item : items.values()) {
            if (item.getEndMeasure() > next)
                next = item.getEndMeasure();
        }
        return next;
    }

    /**
     * Adds a {@link TrackPhrase} to the {@link Track} by creating a
     * {@link TrackItem} reference.
     * 
     * @param startMeasure The measure the phrase starts on.
     * @param numLoops The number of times the phrase is repeated. The
     *            {@link TrackPhrase#getNumMeasures()} is used to calculate the
     *            number of total measures.
     * @param trackPhrase The phrase reference which will already have been
     *            created and registered with the Track. This phrase will have
     *            an index of 0-63.
     * @throws CausticException The start or measure span is occupied
     */
    public TrackItem addPhraseAt(int startMeasure, int numLoops, TrackPhrase trackPhrase)
            throws CausticException {

        if (contains(startMeasure))
            throw new CausticException("Track contain phrase at start: " + startMeasure);

        final int duration = trackPhrase.getNumMeasures() * numLoops;
        int endMeasure = startMeasure + duration;
        if (contains(startMeasure, endMeasure))
            throw new CausticException("Track contain phrase at end: " + endMeasure);

        TrackItem item = new TrackItem();
        item.setTrackPhraseId(trackPhrase.getId());
        item.setStartMeasure(startMeasure);

        item.setEndMeasure(startMeasure + duration);
        item.setNumLoops(numLoops);

        items.put(startMeasure, item);

        getDispatcher().trigger(new OnTrackPhraseAdd(this, item));

        return item;
    }

    private boolean contains(int startMeasure, int endMeasure) {
        //        for (int measure = startMeasure; measure < endMeasure; measure++) {
        //            for (TrackItem item : items.values()) {
        //                if (item.containsInSpan(measure))
        //                    return true;
        //            }
        //        }
        return false;
    }

    public boolean contains(int measure) {
        for (TrackItem item : items.values()) {
            if (item.contains(measure))
                return true;
        }
        return false;
    }

    public void removePhrase(int startMeasure) throws CausticException {
        if (!items.containsKey(startMeasure))
            throw new CausticException("Patterns does not contain phrase at: " + startMeasure);

        TrackItem item = items.remove(startMeasure);
        BankPatternSlot slot = new BankPatternSlot(item.getBankIndex(), item.getPatternIndex());
        queue.push(slot);

        //getDispatcher().trigger(new OnTrackPhraseRemove(this, trackPhrase));
    }

    public void _addPhrase(int startMeasure, int numMeasures, TrackPhrase trackPhrase)
            throws CausticException {
        //        if (map.containsKey(startMeasure))
        //            throw new CausticException("Patterns contain phrase at: " + startMeasure);
        //
        //        trackPhrase.setStartMeasure(startMeasure);
        //        trackPhrase.setEndMeasure(startMeasure + numMeasures);
        //        map.put(startMeasure, trackPhrase);
        //        getDispatcher().trigger(new OnTrackPhraseAdd(this, trackPhrase));
    }

    public void _removePhrase(TrackPhrase trackPhrase) throws CausticException {
        //        int measure = trackPhrase.getStartMeasure();
        //        if (!map.containsKey(measure))
        //            throw new CausticException("Patterns does not contain phrase at: " + measure);
        //        map.remove(measure);
        //        getDispatcher().trigger(new OnTrackPhraseRemove(this, trackPhrase));
    }

    public static class TrackEvent {

        private Track track;

        public final Track getTrack() {
            return track;
        }

        public TrackEvent(Track track) {
            this.track = track;
        }

    }

    public static class OnTrackPhraseAdd extends TrackEvent {

        private TrackItem trackItem;

        public OnTrackPhraseAdd(Track track, TrackItem trackItem) {
            super(track);
            this.trackItem = trackItem;
        }

        public final TrackItem getItem() {
            return trackItem;
        }

    }

    public static class OnTrackPhraseRemove extends TrackEvent {

        private TrackItem trackItem;

        public OnTrackPhraseRemove(Track track, TrackItem trackItem) {
            super(track);
            this.trackItem = trackItem;
        }

        public final TrackItem getItem() {
            return trackItem;
        }

    }

    public void clearPhrases() throws CausticException {
        //        Collection<TrackPhrase> removed = new ArrayList<TrackPhrase>(map.values());
        //        Iterator<TrackPhrase> i = removed.iterator();
        //        while (i.hasNext()) {
        //            TrackPhrase trackPhrase = (TrackPhrase)i.next();
        //            removePhrase(trackPhrase);
        //        }
    }

    class BankPatternSlot {

        private int bank;

        private int pattern;

        public final int getBank() {
            return bank;
        }

        public final int getPattern() {
            return pattern;
        }

        public BankPatternSlot(int bank, int pattern) {
            this.bank = bank;
            this.pattern = pattern;
        }

        @Override
        public String toString() {
            return "[" + bank + ":" + pattern + "]";
        }
    }

    private void initialize() {
        queue = new ArrayDeque<BankPatternSlot>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 16; j++) {
                TrackPhrase phrase = findPhrase(i, j);
                if (phrase == null) {
                    queue.addLast(new BankPatternSlot(i, j));
                } else {

                }
            }
        }
    }

    private TrackPhrase findPhrase(int bankIndex, int patternIndex) {
        for (TrackPhrase trackPhrase : registry.values()) {
            if (trackPhrase.getBankIndex() == bankIndex
                    && trackPhrase.getPatternIndex() == patternIndex) {
                return trackPhrase;
            }
        }
        return null;
    }

    @Override
    public void sleep() {
        // TODO Auto-generated method stub

    }

    @Override
    public void wakeup(ICaustkController controller) {
        initialize();

    }

}
