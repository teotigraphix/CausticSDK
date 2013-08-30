
package com.teotigraphix.caustk.track;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.track.ITrackSequencer.OnTrackSequencerPropertyChange;
import com.teotigraphix.caustk.track.ITrackSequencer.PropertyChangeKind;

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

    Map<Integer, Map<Integer, TrackPhrase>> patterns = new TreeMap<Integer, Map<Integer, TrackPhrase>>();

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
        Map<Integer, TrackPhrase> banks = patterns.get(bank);
        if (banks == null) {
            banks = new TreeMap<Integer, TrackPhrase>();
            patterns.put(bank, banks);
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

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
        for (Map<Integer, TrackPhrase> bankMap : patterns.values()) {
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

}
