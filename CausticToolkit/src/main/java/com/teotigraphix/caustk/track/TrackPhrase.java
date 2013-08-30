
package com.teotigraphix.caustk.track;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.track.ITrackSequencer.OnTrackSequencerPropertyChange;
import com.teotigraphix.caustk.track.ITrackSequencer.PropertyChangeKind;

/**
 * @see ITrackSequencer#getDispatcher()
 * @see OnTrackPhraseLengthChange
 * @see OnTrackPhraseNoteDataChange
 * @see OnTrackPhrasePlayMeasureChange
 * @see OnTrackPhraseEditMeasureChange
 */
public class TrackPhrase implements ISerialize {

    private transient ICaustkController controller;

    ICaustkController getController() {
        return controller;
    }

    final IDispatcher getDispatcher() {
        return controller.getTrackSequencer().getDispatcher();
    }

    final Tone getTone() {
        return controller.getSoundSource().getTone(toneIndex);
    }

    final TrackChannel getTrackChannel() {
        return controller.getTrackSequencer().getTrack(toneIndex);
    }

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // toneIndex
    //----------------------------------

    private final int toneIndex;

    public final int getToneIndex() {
        return toneIndex;
    }

    //----------------------------------
    // bank
    //----------------------------------

    private final int bank;

    /**
     * Returns the assigned bank for this phrase in the native pattern
     * sequencer.
     */
    public final int getBank() {
        return bank;
    }

    //----------------------------------
    // pattern
    //----------------------------------

    private final int pattern;

    /**
     * Returns the assigned pattern for this phrase in the native pattern
     * sequencer.
     */
    public int getPattern() {
        return pattern;
    }

    //----------------------------------
    // length
    //----------------------------------

    private int length;

    /**
     * Returns the assigned number of measurees for this phrase in the native
     * pattern sequencer.
     */
    public final int getLength() {
        return length;
    }

    /**
     * @param value 1, 2, 4, 8
     * @see OnTrackPhraseLengthChange
     */
    public void setLength(int value) {
        if (value == length)
            return;
        length = value;
        getDispatcher()
                .trigger(new OnTrackSequencerPropertyChange(PropertyChangeKind.Length, this));
    }

    //----------------------------------
    // noteData
    //----------------------------------

    String noteData;

    /**
     * Returns the note data serialization when initialized.
     * <p>
     * Clients need to update this with {@link #setNoteData(String)} for correct
     * serialization when a project is saved, if any sequencing commands were
     * issued.
     */
    public final String getNoteData() {
        return noteData;
    }

    /**
     * @param value
     * @see OnTrackPhraseNoteDataChange
     */
    public void setNoteData(String value) {
        noteData = value;
        getDispatcher().trigger(
                new OnTrackSequencerPropertyChange(PropertyChangeKind.NoteData, this));
    }

    private List<PhraseNote> notes = new ArrayList<PhraseNote>();

    public List<PhraseNote> getNotes() {
        return notes;
    }

    public List<PhraseNote> getEditMeasureNotes() {
        List<PhraseNote> result = new ArrayList<PhraseNote>();
        for (PhraseNote note : notes) {
            int measure = (int)Math.floor(note.getStart());
            if (measure >= editMeasure || measure <= editMeasure + 4) {
                result.add(note);
            }
        }
        return result;
    }

    public void addNote(int pitch, float start, float end, float velocity, int flags) {
        PhraseNote note = new PhraseNote(pitch, start, end, velocity, flags);
        notes.add(note);
        getDispatcher().trigger(
                new OnTrackSequencerPropertyChange(PropertyChangeKind.NoteAdd, this, note));
    }

    public void removeNote(int pitch, float start) {
        PhraseNote note = getNote(pitch, start);
        if (note != null) {
            notes.remove(note);
            getDispatcher().trigger(
                    new OnTrackSequencerPropertyChange(PropertyChangeKind.NoteRemove, this, note));
        }
    }

    public PhraseNote getNote(int pitch, float start) {
        for (PhraseNote note : notes) {
            if (note.getPitch() == pitch && note.getStart() == start)
                return note;
        }
        return null;
    }

    public boolean hasNote(int pitch, float start) {
        return getNote(pitch, start) != null;
    }

    //----------------------------------
    // playMeasure
    //----------------------------------

    private int playMeasure;

    /**
     * Returns the current measure of the playhead during a pattern or song
     * play.
     */
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
        playMeasure = value;
        getDispatcher().trigger(
                new OnTrackSequencerPropertyChange(PropertyChangeKind.PlayMeasure, this));
    }

    //----------------------------------
    // editMeasure
    //----------------------------------

    private int editMeasure;

    /**
     * Returns the current editing measure, this value is based of a specific
     * application implementation.
     */
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
        editMeasure = value;
        getDispatcher().trigger(
                new OnTrackSequencerPropertyChange(PropertyChangeKind.EditMeasure, this));
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public TrackPhrase(ICaustkController controller, int toneIndex, int bank, int pattern) {
        this.controller = controller;
        this.toneIndex = toneIndex;
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

}
