
package com.teotigraphix.caustk.sequencer.track;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.sequencer.ITrackSequencer;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.OnPhraseChange;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.PhraseChangeKind;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.components.PatternSequencerComponent.Resolution;

/**
 * @see ITrackSequencer#getDispatcher()
 * @see OnTrackPhraseLengthChange
 * @see OnTrackPhraseNoteDataChange
 * @see OnTrackPhrasePlayMeasureChange
 * @see OnTrackPhraseEditMeasureChange
 */
public class Phrase implements Serializable {

    private static final long serialVersionUID = -7976616079239236670L;

    private TriggerMap triggerMap;

    private Track track;

    public final Track getTrack() {
        return track;
    }

    final IDispatcher getDispatcher() {
        return track.getDispatcher();
    }

    public final Tone getTone() {
        return track.getRack().getTone(toneIndex);
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

        fireChange(PhraseChangeKind.Length);

        if (position > value)
            setPosition(value);
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
     * The client is responsible for setting the correct bank/pattern before
     * this call.
     * 
     * @param value
     * @see OnTrackPhraseNoteDataChange
     */
    public void setNoteData(String value) {
        noteData = value;
        if (noteData == null || noteData.equals(""))
            return; // XXX Clear the note list?

        String[] notes = noteData.split("\\|");
        for (String noteData : notes) {
            String[] split = noteData.split(" ");

            float start = Float.valueOf(split[0]);
            int pitch = Float.valueOf(split[1]).intValue();
            float velocity = Float.valueOf(split[2]);
            float end = Float.valueOf(split[3]);
            int flags = Float.valueOf(split[4]).intValue();
            addNote(pitch, start, end, velocity, flags);
        }

        fireChange(PhraseChangeKind.NoteData);
    }

    public Collection<Note> getEditMeasureNotes() {
        return getNotes(editMeasure);
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
        fireChange(PhraseChangeKind.PlayMeasure);
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
        fireChange(PhraseChangeKind.EditMeasure);
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

    private float floatBeat = -1;

    private int localBeat;

    /**
     * Return the ISong current beat.
     */
    public int getCurrentBeat() {
        return currentBeat;
    }

    void setCurrentBeat(float value) {
        if (value == floatBeat)
            return;

        floatBeat = value;

        getDispatcher().trigger(new OnPhraseChange(PhraseChangeKind.Beat, this, null));
    }

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

    public int getLocalBeat() {
        return localBeat;
    }

    public void onBeatChange(float beat) {
        localBeat = (int)toLocalBeat(beat, getLength());

        float fullMeasure = beat / 4;
        float measure = fullMeasure % getLength();
        setCurrentBeat(beat);

        setPlayMeasure((int)measure);
    }

    public static float toLocalBeat(float beat, int length) {
        float r = (beat % (length * 4));
        return r;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Phrase(Track track, int toneIndex, int bank, int pattern) {
        this.track = track;
        this.toneIndex = toneIndex;
        this.bank = bank;
        this.pattern = pattern;
        triggerMap = new TriggerMap(this);
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Clears all notes from all measures of the phrase.
     */
    public void clear() {
    }

    /**
     * Clears all notes from measure.
     */
    public void clear(int measure) {
        Collection<Note> list = getNotes(measure);
        for (Note note : list) {
            removeNote(note);
        }
        fireChange(PhraseChangeKind.ClearMeasure);
    }

    //--------------------------------------------------------------------------
    // Added from Phrase
    //--------------------------------------------------------------------------

    //----------------------------------
    // scale
    //----------------------------------

    public enum Scale {
        SIXTEENTH, SIXTEENTH_TRIPLET, THIRTYSECOND, THIRTYSECOND_TRIPLET, SIXTYFORTH
    }

    private Scale scale = Scale.SIXTEENTH;

    public Scale getScale() {
        return scale;
    }

    /**
     * Sets the new scale for the phrase.
     * <p>
     * The scale is used to calculate the {@link Resolution} of input. This
     * property mainly relates the the view of the phrase, where the underlying
     * pattern sequencer can have a higher resolution but the view is showing
     * the scale.
     * <p>
     * So you can have a phrase scale of 16, but the resolution could be 64th,
     * but the view will only show the scale notes which would be 16th.
     * 
     * @param value
     * @see OnPhraseScaleChange
     */
    public void setScale(Scale value) {
        if (scale == value)
            return;
        scale = value;
        fireChange(PhraseChangeKind.Scale);
    }

    //----------------------------------
    // position
    //----------------------------------

    private int position = 1;

    /**
     * Returns the current position in the phrase based on the rules of the
     * view.
     * <p>
     * Depending on the resolution and scale, the position can mean different
     * things.
     * <p>
     * 16th note with a length of 4, has 4 measures and thus 4 positions(1-4).
     * When the position is 2, the view triggers are 16-31(0 index).
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the current position of the phrase.
     * 
     * @param value The new phrase position.
     * @see OnPhrasePositionChange
     */
    public void setPosition(int value) {
        if (position == value)
            return;
        // if p = 1 and len = 1
        if (value < 0 || value > getLength())
            return;
        position = value;
        fireChange(PhraseChangeKind.Position);
    }

    //----------------------------------
    // resolution
    //----------------------------------

    public Resolution getResolution() {
        switch (getScale()) {
            case SIXTEENTH:
                return Resolution.SIXTEENTH;
            case THIRTYSECOND:
                return Resolution.THIRTYSECOND;
            default:
                return Resolution.SIXTYFOURTH;
        }
    }

    //----------------------------------
    // stepCount
    //----------------------------------

    /**
     * Returns the full number of steps in all measures.
     * <p>
     * IE 4 measures of 32nd resolution has 128 steps.
     */
    public int getStepCount() {
        int numStepsInMeasure = Resolution.toSteps(getResolution());
        return numStepsInMeasure * getLength();
    }

    /**
     * Increments the internal pointer of the measure position.
     */
    public void incrementPosition() {
        int len = getLength();
        int value = position + 1;
        if (value > len)
            value = len;
        setPosition(value);
    }

    /**
     * Decrement the internal pointer of the measure position.
     */
    public void decrementPosition() {
        int value = position - 1;
        if (value < 1)
            value = 1;
        setPosition(value);
    }

    public Collection<Note> getNotes() {
        return triggerMap.getNotes();
    }

    public Collection<Note> getNotes(int measure) {
        return triggerMap.getNotes(measure);
    }

    public void addNotes(Collection<Note> notes) {
        triggerMap.addNotes(notes);
    }

    public Note addNote(int pitch, float start, float gate, float velocity, int flags) {
        return triggerMap.addNote(pitch, start, gate, velocity, flags);
    }

    public Note removeNote(int pitch, float beat) {
        return triggerMap.removeNote(beat, pitch);
    }

    public void removeNote(Note note) {
        triggerMap.removeNote(note);
    }

    public Note getNote(int pitch, float start) {
        return triggerMap.getNote(pitch, start);
    }

    public boolean hasNote(int pitch, float beat) {
        return triggerMap.hasNote(pitch, beat);
    }

    public Collection<Trigger> getTriggers() {
        return triggerMap.getTriggers();
    }

    public Collection<Trigger> getViewTriggers() {
        return triggerMap.getViewTriggers();
    }

    public Map<Integer, Trigger> getViewTriggerMap() {
        return triggerMap.getViewTriggerMap();
    }

    public final Trigger getTrigger(float beat) {
        return triggerMap.getTrigger(beat);
    }

    public final Trigger getTrigger(int step) {
        return triggerMap.getTrigger(step);
    }

    public void triggerOn(int step) {
        triggerMap.triggerOn(step);
    }

    public void triggerOn(int step, int pitch, float gate, float velocity, int flags) {
        triggerMap.triggerOn(step, pitch, gate, velocity, flags);
    }

    public void triggerOff(int step) {
        triggerMap.triggerOff(step);
    }

    public void triggerUpdateGate(int step, float gate) {
        triggerMap.triggerUpdateGate(step, gate);
    }

    public final boolean containsTrigger(float beat) {
        return triggerMap.containsTrigger(beat);
    }

    public final boolean containsTrigger(int step) {
        return triggerMap.containsTrigger(step);
    }

    protected void fireChange(PhraseChangeKind kind) {
        getDispatcher().trigger(new OnPhraseChange(kind, this, null));
    }

    protected void fireChange(PhraseChangeKind kind, Note phraseNote) {
        getDispatcher().trigger(new OnPhraseChange(kind, this, phraseNote));
    }

    @Override
    public String toString() {
        return "Bank:" + bank + ",Pattern:" + pattern;
    }

}
