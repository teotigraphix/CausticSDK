
package com.teotigraphix.caustk.sequencer.track;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.sequencer.ITrackSequencer;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.OnTrackSequencerPropertyChange;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.PropertyChangeKind;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.tone.Tone;

/**
 * @see ITrackSequencer#getDispatcher()
 * @see OnTrackPhraseLengthChange
 * @see OnTrackPhraseNoteDataChange
 * @see OnTrackPhrasePlayMeasureChange
 * @see OnTrackPhraseEditMeasureChange
 */
public class TrackPhrase implements ISerialize {

    private transient ICaustkController controller;

    public final ICaustkController getController() {
        return controller;
    }

    final IDispatcher getDispatcher() {
        return controller.getTrackSequencer();
    }

    final Tone getTone() {
        return controller.getSoundSource().getTone(toneIndex);
    }

    final Track getTrack() {
        return controller.getTrackSequencer().getTrack(toneIndex);
    }

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // toneIndex
    //----------------------------------

    private UUID phraseId;

    public UUID getPhraseId() {
        return phraseId;
    }

    public void setPhraseId(UUID value) {
        phraseId = value;
    }

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
        return getNotes(editMeasure);
    }

    public List<PhraseNote> getNotes(int measure) {
        List<PhraseNote> result = new ArrayList<PhraseNote>();
        for (PhraseNote note : notes) {
            int beat = (int)Math.floor(note.getStart());
            int startBeat = (measure * 4);
            if (beat >= startBeat && beat < startBeat + 4) {
                result.add(note);
            }
        }
        return result;
    }

    public void addNotes(Collection<PhraseNote> notes) {
        this.notes.addAll(notes);
    }

    public PhraseNote addNote(int pitch, float start, float end, float velocity, int flags) {
        PhraseNote note = new PhraseNote(pitch, start, end, velocity, flags);
        notes.add(note);
        getDispatcher().trigger(
                new OnTrackSequencerPropertyChange(PropertyChangeKind.NoteAdd, this, note));
        return note;
    }

    public PhraseNote removeNote(int pitch, float start) {
        PhraseNote note = getNote(pitch, start);
        if (note != null) {
            notes.remove(note);
            getDispatcher().trigger(
                    new OnTrackSequencerPropertyChange(PropertyChangeKind.NoteRemove, this, note));
        }
        return note;
    }

    public void removeNote(PhraseNote note) {
        removeNote(note.getPitch(), note.getStart());
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
        //System.err.println(playMeasure);
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

    public void onBeatChange(float beat) {
        localBeat = (int)toLocalBeat(beat, getLength());

        float fullMeasure = beat / 4;
        float measure = fullMeasure % getLength();
        setCurrentBeat(beat);

        setPlayMeasure((int)measure);
    }

    //----------------------------------
    //  currentMeasure
    //----------------------------------

    private int currentMeasure = 0;

    /**
     * Returns the current measure playing in Song mode.
     * <p>
     * Note: The current bar is divisible by 4, the current measure is the sum
     * of all steps played currently in a song.
     * </p>
     * 
     * @return
     */
    public int getCurrentMeasure() {
        return currentMeasure;
    }

    void setCurrentMeasure(int value) {
        currentMeasure = value;
    }

    //----------------------------------
    //  currentBeat
    //----------------------------------

    private int currentBeat = -1;

    /**
     * Return the ISong current beat.
     */
    public int getCurrentBeat() {
        return currentBeat;
    }

    @SuppressWarnings("unused")
    private float beat = -1;

    private int localBeat;

    /**
     * Returns the actual beat in the current measure.
     * <p>
     * Example; measure 4, beat 14 would be beat 2 in the measure (0 index - 3rd
     * beat in measure).
     * </p>
     */
    public int getMeasureBeat() {
        return currentBeat % 4;
    }

    void setCurrentBeat(float value) {
        beat = value;
    }

    void setCurrentBeat(int value) {
        setCurrentBeat(value, false);
    }

    void setCurrentBeat(int value, boolean seeking) {
        int last = currentBeat;
        if (value == currentBeat)
            return;

        currentBeat = value;

        getDispatcher().trigger(new OnTrackSequencerPropertyChange(PropertyChangeKind.Beat, this));

        //        fireBeatChange(mCurrentBeat, last);

        if (last < value) {
            // forward
            if (currentBeat == 0) {
                setCurrentMeasure(0);
            } else {
                int remainder = currentBeat % 4;
                if (seeking) {
                    setCurrentMeasure(currentBeat / 4);
                } else if (remainder == 0) {
                    setCurrentMeasure(currentMeasure + 1);
                }
            }
        } else if (last > value) {
            // reverse
            // if the last beat was a measure change, decrement measure
            int remainder = last % 4;
            if (remainder == 0) {
                setCurrentMeasure(currentMeasure - 1);
            }
        }
    }

    public static float toLocalBeat(float beat, int length) {
        float r = (beat % (length * 4));
        return r;
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
        for (PhraseNote note : notes) {
            note.sleep();
        }
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
        for (PhraseNote note : notes) {
            note.wakeup(controller);
        }
    }

    public int getLocalBeat() {
        return localBeat;

    }

    /**
     * Clears all notes from all measures of the phrase.
     */
    public void clear() {
        // TODO Auto-generated method stub

    }

    /**
     * Clears all notes from measure.
     */
    public void clear(int measure) {
        List<PhraseNote> list = getNotes(measure);
        for (PhraseNote note : list) {
            removeNote(note);
        }
        getDispatcher().trigger(
                new OnTrackSequencerPropertyChange(PropertyChangeKind.ClearMeasure, this));
    }

}
