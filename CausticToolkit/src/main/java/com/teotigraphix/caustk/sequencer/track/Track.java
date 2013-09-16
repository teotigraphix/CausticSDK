
package com.teotigraphix.caustk.sequencer.track;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sequencer.ITrackSequencer;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.OnTrackChange;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.TrackChangeKind;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.sound.mixer.SoundMixerChannel;
import com.teotigraphix.caustk.tone.Tone;

/**
 * @see ITrackSequencer#getDispatcher()
 * @see OnTrackBankChange
 * @see OnTrackPatternChange
 */
public class Track implements ISerialize {

    private transient ICaustkController controller;

    final IDispatcher getDispatcher() {
        return controller.getTrackSequencer();
    }

    Map<Integer, Map<Integer, Phrase>> phrases = new TreeMap<Integer, Map<Integer, Phrase>>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    /**
     * Returns the {@link SoundMixerChannel} for the track.
     */
    public SoundMixerChannel getMixerChannel() {
        return controller.getSoundMixer().getChannel(getIndex());
    }

    /**
     * Returns the {@link Tone} for this track located in the
     * {@link ISoundSource}.
     */
    public Tone getTone() {
        return controller.getSoundSource().getTone(getIndex());
    }

    //----------------------------------
    // index
    //----------------------------------

    private final int index;

    /**
     * Returns the index within the {@link ISoundSource}, the same index as the
     * {@link #getTone()}.
     */
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
     * @see OnTrackBankChange
     */
    public void setCurrentBank(int value) {
        if (value == currentBank)
            return;
        currentBank = value;
        getDispatcher().trigger(new OnTrackChange(TrackChangeKind.Bank, this));
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
     * @see OnTrackPatternChange
     */
    public void setCurrentPattern(int value) {
        if (value == currentPattern)
            return;
        currentPattern = value;
        bankEditor.put(currentBank, currentPattern);
        getDispatcher().trigger(new OnTrackChange(TrackChangeKind.Pattern, this));
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
     * @see OnTrackBankChange
     * @see OnTrackPatternChange
     */
    public void setCurrentBankPattern(int bank, int pattern) {
        setCurrentBank(bank);
        setCurrentPattern(pattern);
    }

    public final boolean hasBank(int bankIndex) {
        return phrases.containsKey(bankIndex);
    }

    public final boolean hasPhrase(int bankIndex, int patternIndex) {
        if (!hasBank(bankIndex))
            return false;
        return getPhraseMap(bankIndex).containsKey(patternIndex);
    }

    /**
     * Returns the existing {@link Phrase}s for the bankIndex.
     * <p>
     * Due to lazy load, if a phrase is asked for it can still exist in the
     * collection even without not data.
     */
    public Collection<Phrase> getPhrases(int bankIndex) {
        return phrases.get(bankIndex).values();
    }

    public Map<Integer, Phrase> getPhraseMap(int bankIndex) {
        return phrases.get(bankIndex);
    }

    /**
     * Returns a {@link Phrase} for the bank and pattern, will create a new
     * phrase if does not exist.
     * 
     * @param bank
     * @param pattern
     */
    public Phrase getPhrase(int bank, int pattern) {
        Map<Integer, Phrase> banks = phrases.get(bank);
        if (banks == null) {
            banks = new TreeMap<Integer, Phrase>();
            phrases.put(bank, banks);
        }
        Phrase phrase = banks.get(pattern);
        if (phrase == null) {
            phrase = new Phrase(controller, index, bank, pattern);
            banks.put(pattern, phrase);
        }
        return phrase;
    }

    /**
     * Returns the current {@link Phrase} of the {@link #getCurrentBank()}
     * and {@link #getCurrentPattern()}.
     */
    public Phrase getPhrase() {
        return getPhrase(currentBank, currentPattern);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Track(ICaustkController controller, int index) {
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
        for (Map<Integer, Phrase> map : phrases.values()) {
            for (Phrase phrase : map.values()) {
                phrase.sleep();
            }
        }
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
        for (Map<Integer, Phrase> bankMap : phrases.values()) {
            for (Phrase phrase : bankMap.values()) {
                phrase.wakeup(controller);
            }
        }
    }

    //--------------------------------------------------------------------------
    // Events
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------

    // the phrase map is keyed on the measure start
    Map<Integer, TrackItem> items = new HashMap<Integer, TrackItem>();

    public void setCurrentBeat(float currentBeat) {
        // TODO Auto-generated method stub

    }

    /**
     * Returns an item that ends at the end measure.
     * 
     * @param measure The end measure.
     */
    public TrackItem getItemAtEndMeasure(int measure) {
        for (TrackItem item : items.values()) {
            if (item.getEndMeasure() == measure)
                return item;
        }
        return null;
    }

    public TrackItem getItemOnMeasure(int measure) {
        for (TrackItem item : items.values()) {
            if (item.contains(measure))
                return item;
        }
        return null;
    }

    public TrackItem getTrackItem(int measure) {
        return items.get(measure);
    }

    /**
     * Returns all {@link TrackItem}s for the whole {@link Track}.
     * <p>
     * The map is indexed by start measure.
     */
    public Map<Integer, TrackItem> getTrackItems() {
        return items;
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

    public TrackItem addPhrase(int numLoops, Phrase phrase) throws CausticException {
        int startMeasure = getNextMeasure();
        return addPhraseAt(startMeasure, numLoops, phrase, true);
    }

    /**
     * Adds a {@link Phrase} to the {@link Track} by creating a
     * {@link TrackItem} reference.
     * 
     * @param startMeasure The measure the phrase starts on.
     * @param numLoops The number of times the phrase is repeated. The
     *            {@link Phrase#getNumMeasures()} is used to calculate the
     *            number of total measures.
     * @param trackPhrase The phrase reference which will already have been
     *            created and registered with the Track. This phrase will have
     *            an index of 0-63.
     * @throws CausticException The start or measure span is occupied
     */
    public TrackItem addPhraseAt(int startMeasure, int numLoops, Phrase trackPhrase,
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
        item.setPhraseId(trackPhrase.getPhraseId());
        item.setStartMeasure(startMeasure);
        item.setBankIndex(trackPhrase.getBank());
        item.setPatternIndex(trackPhrase.getPattern());
        item.setEndMeasure(startMeasure + duration);
        item.setNumLoops(numLoops);

        items.put(startMeasure, item);

        getDispatcher().trigger(new OnTrackChange(TrackChangeKind.Add, this, item));

        controller.getSystemSequencer().addPattern(getTone(), item.getBankIndex(),
                item.getPatternIndex(), item.getStartMeasure(), item.getEndMeasure());

        return item;
    }

    public void removePhrase(int startMeasure) throws CausticException {
        if (!items.containsKey(startMeasure))
            throw new CausticException("Patterns does not contain phrase at: " + startMeasure);

        TrackItem item = items.remove(startMeasure);
        getDispatcher().trigger(
                new OnTrackChange(TrackChangeKind.Remove, this, item));
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

    public static float toLocalBeat(float beat, int length) {
        float r = (beat % (length * 4));
        return r;
    }

    @Override
    public String toString() {
        return items.values().toString();
    }
}
