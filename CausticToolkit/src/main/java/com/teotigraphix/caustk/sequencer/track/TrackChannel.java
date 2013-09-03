
package com.teotigraphix.caustk.sequencer.track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sound.midi.Track;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sequencer.ITrackSequencer;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.OnTrackSequencerPropertyChange;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.PropertyChangeKind;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.tone.Tone;

/**
 * @see ITrackSequencer#getDispatcher()
 * @see OnTrackChannelBankChange
 * @see OnTrackChannelPatternChange
 */
public class TrackChannel implements ISerialize {

    private transient ICaustkController controller;

    final IDispatcher getDispatcher() {
        return controller.getTrackSequencer().getDispatcher();
    }

    Map<Integer, Map<Integer, TrackPhrase>> phrases = new TreeMap<Integer, Map<Integer, TrackPhrase>>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    public Tone getTone() {
        return controller.getSoundSource().getTone(getIndex());
    }

    //----------------------------------
    // currentBank
    //----------------------------------

    private final int index;

    public int getIndex() {
        return index;
    }

    //----------------------------------
    // currentBank
    //----------------------------------

    private int currentBank;

    public int getCurrentBank() {
        return currentBank;
    }

    /**
     * @param value
     * @see OnTrackChannelBankChange
     */
    public void setCurrentBank(int value) {
        if (value == currentBank)
            return;
        currentBank = value;
        getDispatcher().trigger(new OnTrackSequencerPropertyChange(PropertyChangeKind.Bank, this));
    }

    //----------------------------------
    // currentPattern
    //----------------------------------

    private int currentPattern;

    public int getCurrentPattern() {
        return currentPattern;
    }

    /**
     * @param value
     * @see OnTrackChannelPatternChange
     */
    public void setCurrentPattern(int value) {
        if (value == currentPattern)
            return;
        currentPattern = value;
        bankEditor.put(currentBank, currentPattern);
        getDispatcher().trigger(
                new OnTrackSequencerPropertyChange(PropertyChangeKind.Pattern, this));
    }

    private Map<Integer, Integer> bankEditor = new HashMap<Integer, Integer>();

    public int getEditPattern() {
        if (!bankEditor.containsKey(currentBank))
            return 0;
        return bankEditor.get(currentBank);
    }

    /**
     * @param bank
     * @param pattern
     * @see OnTrackChannelBankChange
     * @see OnTrackChannelPatternChange
     */
    public void setCurrentBankPattern(int bank, int pattern) {
        setCurrentBank(bank);
        setCurrentPattern(pattern);
    }

    /**
     * Returns a {@link TrackPhrase} for the bank and pattern, will create a new
     * phrase if does not exist.
     * 
     * @param bank
     * @param pattern
     */
    public TrackPhrase getPhrase(int bank, int pattern) {
        Map<Integer, TrackPhrase> banks = phrases.get(bank);
        if (banks == null) {
            banks = new TreeMap<Integer, TrackPhrase>();
            phrases.put(bank, banks);
        }
        TrackPhrase phrase = banks.get(pattern);
        if (phrase == null) {
            phrase = new TrackPhrase(controller, index, bank, pattern);
            banks.put(pattern, phrase);
        }
        return phrase;
    }

    /**
     * Returns the current {@link TrackPhrase} of the {@link #getCurrentBank()}
     * and {@link #getCurrentPattern()}.
     */
    public TrackPhrase getPhrase() {
        return getPhrase(currentBank, currentPattern);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public TrackChannel(ICaustkController controller, int index) {
        this.controller = controller;
        this.index = index;
    }

    public void onAdded() {
    }

    public void onRemoved() {
    }

    //--------------------------------------------------------------------------
    // ISerialize API :: Methods
    //--------------------------------------------------------------------------

    /*
     * A channel serializes;
     * - MixerChannel (bass, mid, high, delay, reverb, pan, width, volume
     * - EffectChannel slot1, slot2
     */

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
        for (Map<Integer, TrackPhrase> bankMap : phrases.values()) {
            for (TrackPhrase phrase : bankMap.values()) {
                phrase.wakeup(controller);
            }
        }
    }

    //--------------------------------------------------------------------------
    // Events
    //--------------------------------------------------------------------------

    public static class TrackChannelEvent {

        private TrackChannel track;

        public TrackChannel getTrack() {
            return track;
        }

        public TrackChannelEvent(TrackChannel track) {
            this.track = track;
        }
    }

    public static class OnTrackChannelBankChange extends TrackChannelEvent {
        private int old;

        public int getOld() {
            return old;
        }

        public OnTrackChannelBankChange(TrackChannel track, int old) {
            super(track);
            this.old = old;
        }
    }

    public static class OnTrackChannelPatternChange extends TrackChannelEvent {
        private int old;

        public int getOld() {
            return old;
        }

        public OnTrackChannelPatternChange(TrackChannel track, int old) {
            super(track);
            this.old = old;
        }
    }

    //--------------------------------------------------------------------------

    // the phrase map is keyed on the measure start
    Map<Integer, TrackItem> items = new HashMap<Integer, TrackItem>();

    public void setCurrentBeat(float currentBeat) {
        // TODO Auto-generated method stub

    }

    /**
     * Returns a collection of items that end at the passed measure.
     * 
     * @param measure The end measure.
     */
    public List<TrackItem> getItemsAtEndMeasure(int measure) {
        List<TrackItem> result = new ArrayList<TrackItem>();
        for (TrackItem item : items.values()) {
            if (item.getEndMeasure() == measure)
                result.add(item);
        }
        return result;
    }

    public List<TrackItem> getItemsOnMeasure(int measure) {
        List<TrackItem> result = new ArrayList<TrackItem>();
        for (TrackItem item : items.values()) {
            if (item.contains(measure))
                result.add(item);
        }
        return result;
    }

    public TrackItem getTrackItem(int measure) {
        return items.get(measure);
    }

    public TrackItem findTrackItem(int startMeasure, int length) {
        for (int i = startMeasure; i < startMeasure + length; i++) {
            for (TrackItem item : items.values()) {
                if (items.containsKey(i))
                    return item;
            }
        }
        return null;
    }

    public TrackItem addPhrase(int numLoops, TrackPhrase channel) throws CausticException {
        int startMeasure = getNextMeasure();
        return addPhraseAt(startMeasure, numLoops, channel, true);
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
    public TrackItem addPhraseAt(int startMeasure, int numLoops, TrackPhrase trackPhrase,
            boolean dispatch) throws CausticException {

        if (contains(startMeasure))
            throw new CausticException("Track contain phrase at start: " + startMeasure);

        final int duration = trackPhrase.getLength() * numLoops;
        int endMeasure = startMeasure + duration;
        if (contains(startMeasure, endMeasure))
            throw new CausticException("Track contain phrase at end: " + endMeasure);

        TrackItem item = new TrackItem();
        item.setTrackIndex(getIndex());
        item.setNumMeasures(trackPhrase.getLength());
        //        item.setPhraseId(trackPhrase.getId());
        item.setStartMeasure(startMeasure);
        item.setBankIndex(trackPhrase.getBank());
        item.setPatternIndex(trackPhrase.getPattern());
        item.setEndMeasure(startMeasure + duration);
        item.setNumLoops(numLoops);

        items.put(startMeasure, item);

        //if (dispatch)
        //   getDispatcher().trigger(new OnTrackPhraseAdd(this, item));
        controller.getSystemSequencer().addPattern(getTone(), item.getBankIndex(),
                item.getPatternIndex(), item.getStartMeasure(), item.getEndMeasure());

        return item;
    }

    public void removePhrase(int startMeasure) throws CausticException {
        if (!items.containsKey(startMeasure))
            throw new CausticException("Patterns does not contain phrase at: " + startMeasure);

        TrackItem item = items.remove(startMeasure);
        @SuppressWarnings("unused")
        BankPatternSlot slot = new BankPatternSlot(item.getBankIndex(), item.getPatternIndex());
        //        queue.push(slot);

        //getDispatcher().trigger(new OnTrackPhraseRemove(this, trackPhrase));
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

    public static float toLocalBeat(float beat, int length) {
        float r = (beat % (length * 4));
        return r;
    }

    @Override
    public String toString() {
        return items.values().toString();
    }

    public static class OnTrackChannelPhraseAdd extends TrackChannelEvent {

        private TrackItem trackItem;

        public OnTrackChannelPhraseAdd(TrackChannel track, TrackItem trackItem) {
            super(track);
            this.trackItem = trackItem;
        }

        public final TrackItem getItem() {
            return trackItem;
        }

    }

    public static class OnTrackChannelPhraseRemove extends TrackChannelEvent {

        private TrackItem trackItem;

        public OnTrackChannelPhraseRemove(TrackChannel track, TrackItem trackItem) {
            super(track);
            this.trackItem = trackItem;
        }

        public final TrackItem getItem() {
            return trackItem;
        }

    }
}
