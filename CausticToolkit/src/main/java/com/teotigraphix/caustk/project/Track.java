
package com.teotigraphix.caustk.project;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.core.IDispatcher;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.sequencer.IPatternSequencer2;
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
    Map<Integer, TrackPhrase> map = new HashMap<Integer, TrackPhrase>();

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

    //--------------------------------------------------------------------------
    //  Constructor
    //--------------------------------------------------------------------------

    public Track() {
        initialize();
    }

    /**
     * Creates a unique {@link TrackPhrase} from a {@link LibraryPhrase}.
     * 
     * @param libraryPhrase The {@link LibraryPhrase} to copy into the track
     *            phrase.
     */
    public TrackPhrase createTrackPhraseFrom(ICaustkController controller,
            LibraryPhrase libraryPhrase) {
        TrackPhrase trackPhrase = findTrackPhraseById(libraryPhrase.getId());

        BankPatternSlot slot = null;
        boolean initializePhrase = trackPhrase == null;
        if (initializePhrase) {
            slot = queue.pop();
        } else {
            slot = new BankPatternSlot(trackPhrase.getBankIndex(), trackPhrase.getPatternIndex());
        }

        trackPhrase = new TrackPhrase();
        trackPhrase.setId(libraryPhrase.getId());

        // a Track can hold 64 different phrases assigned to the native
        // machines bank and pattern index
        // The track manages these entries in an in/out queue
        // when a phrase is added to the track, the bank and pattern are incremented
        // when a phrase is removed, it's bank and pattern is set in a unused queue
        // the next phrase to be added will pop the unused items off the queue
        // when all unused items are used up, the bank and pattern are incremented again

        trackPhrase.setBankIndex(slot.getBank());
        trackPhrase.setPatternIndex(slot.getPattern());
        trackPhrase.setExplicitLength(libraryPhrase.getLength());

        String noteData = libraryPhrase.getNoteData();
        trackPhrase.setNoteData(noteData);

        if (initializePhrase) {
            assignNotes(controller, trackPhrase);
        }

        return trackPhrase;
    }

    public void assignNotes(ICaustkController controller, TrackPhrase trackPhrase) {
        Tone tone = controller.getSoundSource().getTone(getIndex());
        IMachine machine = tone.getMachine();
        IPatternSequencer2 sequencer = machine.getPatternSequencer();

        int lastBank = sequencer.getSelectedBank();
        int lastPattern = sequencer.getSelectedIndex();
        sequencer.setSelectedPattern(trackPhrase.getBankIndex(), trackPhrase.getPatternIndex());

        sequencer.setLength(trackPhrase.getExplicitLength());

        applyNoteData(sequencer, trackPhrase.getNoteData());

        sequencer.setSelectedPattern(lastBank, lastPattern);
    }

    private void applyNoteData(IPatternSequencer2 patternSequencer2, String data) {
        // push the notes into the machines sequencer
        String[] notes = data.split("\\|");
        for (String noteData : notes) {
            String[] split = noteData.split(" ");

            float start = Float.valueOf(split[0]);
            int pitch = Float.valueOf(split[1]).intValue();
            float velocity = Float.valueOf(split[2]);
            float end = Float.valueOf(split[3]);
            //float gate = end - start;
            int flags = Float.valueOf(split[4]).intValue();
            //int step = Resolution.toStep(start, getResolution());

            //triggerOn(step, pitch, gate, velocity, flags);
            patternSequencer2.addNote(pitch, start, end, velocity, flags);
        }
    }

    private TrackPhrase findTrackPhraseById(UUID id) {
        for (TrackPhrase trackPhrase : map.values()) {
            if (trackPhrase.getId().equals(id))
                return trackPhrase;
        }
        return null;
    }

    private transient Deque<BankPatternSlot> queue;

    public void addPhrase(int startMeasure, int numMeasures, TrackPhrase trackPhrase)
            throws CausticException {
        if (map.containsKey(startMeasure))
            throw new CausticException("Patterns contain phrase at: " + startMeasure);

        trackPhrase.setStartMeasure(startMeasure);
        trackPhrase.setEndMeasure(startMeasure + numMeasures);
        map.put(startMeasure, trackPhrase);
        getDispatcher().trigger(new OnTrackPhraseAdd(this, trackPhrase));
    }

    public void removePhrase(TrackPhrase trackPhrase) throws CausticException {
        int measure = trackPhrase.getStartMeasure();
        if (!map.containsKey(measure))
            throw new CausticException("Patterns does not contain phrase at: " + measure);
        map.remove(measure);
        getDispatcher().trigger(new OnTrackPhraseRemove(this, trackPhrase));
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

        private TrackPhrase trackPhrase;

        public OnTrackPhraseAdd(Track track, TrackPhrase trackPhrase) {
            super(track);
            this.trackPhrase = trackPhrase;
        }

        public final TrackPhrase getTrackPhrase() {
            return trackPhrase;
        }

    }

    public static class OnTrackPhraseRemove extends TrackEvent {

        private TrackPhrase trackPhrase;

        public OnTrackPhraseRemove(Track track, TrackPhrase trackPhrase) {
            super(track);
            this.trackPhrase = trackPhrase;
        }

        public final TrackPhrase getTrackPhrase() {
            return trackPhrase;
        }

    }

    public void clearPhrases() throws CausticException {
        Collection<TrackPhrase> removed = new ArrayList<TrackPhrase>(map.values());
        Iterator<TrackPhrase> i = removed.iterator();
        while (i.hasNext()) {
            TrackPhrase trackPhrase = (TrackPhrase)i.next();
            removePhrase(trackPhrase);
        }
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
        for (TrackPhrase trackPhrase : map.values()) {
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
    public void wakeup() {
        initialize();

    }
}
