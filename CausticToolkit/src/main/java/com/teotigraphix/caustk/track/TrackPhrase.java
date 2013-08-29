
package com.teotigraphix.caustk.track;

import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.service.ISerialize;

/**
 * @see ITrackSequencer#getDispatcher()
 * @see OnTrackPhraseLengthChange
 * @see OnTrackPhraseNoteDataChange
 * @see OnTrackPhrasePlayMeasureChange
 * @see OnTrackPhraseEditMeasureChange
 */
public class TrackPhrase implements ISerialize {

    private transient ICaustkController controller;

    final IDispatcher getDispatcher() {
        return controller.getComponent(ITrackSequencer.class).getDispatcher();
    }

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // bank
    //----------------------------------

    private final int bank;

    public final int getBank() {
        return bank;
    }

    //----------------------------------
    // pattern
    //----------------------------------

    private final int pattern;

    public int getPattern() {
        return pattern;
    }

    //----------------------------------
    // length
    //----------------------------------

    private int length;

    public final int getLength() {
        return length;
    }

    /**
     * @param value
     * @see OnTrackPhraseLengthChange
     */
    public void setLength(int value) {
        if (value == length)
            return;
        int old = length;
        length = value;
        getDispatcher().trigger(new OnTrackPhraseLengthChange(this, old));
    }

    //----------------------------------
    // noteData
    //----------------------------------

    String noteData;

    public final String getNoteData() {
        return noteData;
    }

    /**
     * @param value
     * @see OnTrackPhraseNoteDataChange
     */
    public void setNoteData(String value) {
        if (noteData.equals(value))
            return;
        noteData = value;
        getDispatcher().trigger(new OnTrackPhraseNoteDataChange(this));
    }

    //----------------------------------
    // playMeasure
    //----------------------------------

    private int playMeasure;

    public int getPlayMeasure() {
        return playMeasure;
    }

    /**
     * @param value
     * @see OnTrackPhrasePlayMeasureChange
     */
    public void setPlayMeasure(int value) {
        if (value == playMeasure)
            return;
        int old = playMeasure;
        playMeasure = value;
        getDispatcher().trigger(new OnTrackPhrasePlayMeasureChange(this, old));
    }

    //----------------------------------
    // editMeasure
    //----------------------------------

    private int editMeasure;

    public int getEditMeasure() {
        return editMeasure;
    }

    /**
     * @param value
     * @see OnTrackPhraseEditMeasureChange
     */
    public void setEditMeasure(int value) {
        if (value == editMeasure)
            return;
        int old = editMeasure;
        editMeasure = value;
        getDispatcher().trigger(new OnTrackPhraseEditMeasureChange(this, old));
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public TrackPhrase(ICaustkController controller, int bank, int pattern) {
        this.controller = controller;
        this.bank = bank;
        this.pattern = pattern;
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
    }

    //--------------------------------------------------------------------------
    // Events
    //--------------------------------------------------------------------------

    public static class TrackPhraseEvent {

        private TrackPhrase phrase;

        public TrackPhrase getPhrase() {
            return phrase;
        }

        public TrackPhraseEvent(TrackPhrase phrase) {
            this.phrase = phrase;
        }
    }

    public static class OnTrackPhraseLengthChange extends TrackPhraseEvent {
        private int oldLength;

        public int getOldLength() {
            return oldLength;
        }

        public OnTrackPhraseLengthChange(TrackPhrase phrase, int oldLength) {
            super(phrase);
            this.oldLength = oldLength;
        }
    }

    public static class OnTrackPhraseNoteDataChange extends TrackPhraseEvent {
        public OnTrackPhraseNoteDataChange(TrackPhrase phrase) {
            super(phrase);
        }
    }

    public static class OnTrackPhrasePlayMeasureChange extends TrackPhraseEvent {
        private int oldMeasure;

        public int getOldMeasure() {
            return oldMeasure;
        }

        public OnTrackPhrasePlayMeasureChange(TrackPhrase phrase, int oldMeasure) {
            super(phrase);
            this.oldMeasure = oldMeasure;
        }
    }

    public static class OnTrackPhraseEditMeasureChange extends TrackPhraseEvent {
        private int oldMeasure;

        public int getOldMeasure() {
            return oldMeasure;
        }

        public OnTrackPhraseEditMeasureChange(TrackPhrase phrase, int oldMeasure) {
            super(phrase);
            this.oldMeasure = oldMeasure;
        }
    }
}
