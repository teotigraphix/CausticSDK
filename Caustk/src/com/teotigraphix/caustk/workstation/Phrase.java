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

package com.teotigraphix.caustk.workstation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.OutputPanelMessage;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent.Resolution;
import com.teotigraphix.caustk.utils.PatternUtils;
import com.teotigraphix.caustk.workstation.Note.NoteFlag;

/**
 * @author Michael Schmalle
 */
public class Phrase extends CaustkComponent {

    final IDispatcher getDispatcher() {
        return machine.getRack().getDispatcher();
    }

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private int index;

    @Tag(101)
    private Machine machine;

    @Tag(102)
    private MachineType machineType;

    @Tag(103)
    private TriggerMap triggerMap;

    @Tag(104)
    private String noteData;

    @Tag(105)
    private int length = 1;

    @Tag(106)
    private int position = 1;

    @Tag(107)
    private Object data;

    @Tag(108)
    private int playMeasure = 0;

    @Tag(109)
    private int editMeasure = 0;

    @Tag(110)
    private int currentMeasure = 0;

    @Tag(111)
    private Scale scale = Scale.SIXTEENTH;

    @Tag(112)
    List<Trigger> editSelections = new ArrayList<Trigger>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        return machine.getMachineName() + "_"
                + PatternUtils.toString(getBankIndex(), getPatternIndex());
    }

    //----------------------------------
    // index
    //----------------------------------

    /**
     * Returns the index of this phrase in the pattern sequencer (0..63).
     */
    public final int getIndex() {
        return index;
    }

    /**
     * Returns the assigned bank for this phrase in the native pattern
     * sequencer.
     */
    public final int getBankIndex() {
        return PatternUtils.getBank(index);
    }

    /**
     * Returns the assigned pattern for this phrase in the native pattern
     * sequencer.
     */
    public final int getPatternIndex() {
        return PatternUtils.getPattern(index);
    }

    //----------------------------------
    // machine
    //----------------------------------

    public final Machine getMachine() {
        return machine;
    }

    //----------------------------------
    // machineType
    //----------------------------------

    public final MachineType getMachineType() {
        return machineType;
    }

    //----------------------------------
    // tempo
    //----------------------------------

    //    public final float getTempo() {
    //        return tempo;
    //    }

    //----------------------------------
    // length
    //----------------------------------

    /**
     * Returns the assigned number of measures for this phrase in the native
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

        if (getMachine() != null) {
            getMachine().getRackTone().getPatternSequencer()
                    .setLength(getBankIndex(), getPatternIndex(), length);
            fireChange(PhraseChangeKind.Length);
        }

        if (position > value)
            setPosition(value);
    }

    //----------------------------------
    // data
    //----------------------------------

    /**
     * Returns the phrase's abstract data if any.
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets the data for the phrase.
     * <p>
     * Clients of the phrase can use this property to tack on additional data
     * that relates to the phrase in the context of their application.
     * <p>
     * Works with serialization.
     * 
     * @param value The data associated with the phrase and client application.
     */
    void setData(Object value) {
        data = value;
    }

    //----------------------------------
    // noteData
    //----------------------------------

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
    void setNoteData(String value) {
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
            addNote(pitch, start, end - start, velocity, flags);
        }

        fireChange(PhraseChangeKind.NoteData);
    }

    //----------------------------------
    // scale
    //----------------------------------

    public enum Scale {
        SIXTEENTH, SIXTEENTH_TRIPLET, THIRTYSECOND, THIRTYSECOND_TRIPLET, SIXTYFORTH
    }

    /**
     * Returns the current {@link Scale} used to determine the
     * {@link Resolution}.
     */
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
    // editSelections
    //----------------------------------

    /**
     * Clears all selected edit triggers.
     * 
     * @see OnPhraseChange
     * @see PhraseChangeKind#EditSelection
     */
    public void clearSelections() {
        editSelections.clear();
        fireChange(PhraseChangeKind.EditSelection);
    }

    /**
     * Returns a list of selected edit triggers within the phrase.
     */
    public List<Trigger> getEditSelections() {
        return editSelections;
    }

    /**
     * Returns the selected edit trigger at index 0.
     * <p>
     * Useful if the application only deal in monophonic mode, then the actual
     * list does not have to be accessed.
     */
    public Trigger getEditSelection() {
        if (editSelections.size() == 0)
            return null;
        return editSelections.get(0);
    }

    /**
     * Sets the list of selected edit triggers.
     * 
     * @param value The selected triggers of this phrase.
     * @see OnPhraseChange
     * @see PhraseChangeKind#EditSelection
     */
    public void setEditSelections(List<Trigger> value) {
        editSelections = value;
        fireChange(PhraseChangeKind.EditSelection);
    }

    /**
     * Sets the single selected edit trigger of this phrase.
     * 
     * @param value The selected trigger.
     * @see OnPhraseChange
     * @see PhraseChangeKind#EditSelection
     */
    public void setEditSelected(Trigger value) {
        editSelections.clear();
        editSelections.add(value);
        fireChange(PhraseChangeKind.EditSelection);
    }

    /**
     * Sets the selected eidt trigger or triggers of the beat.
     * <p>
     * Since {@link Trigger} instances are lazily created, if there is no
     * trigger found at the start beat, this setter will create a default
     * trigger with one {@link Note} and the {@link Trigger} will be set as the
     * edit selection.
     * 
     * @param value The selected float beat.
     */
    public void setSelectedBeat(float beat) {
        Trigger trigger = getTrigger(beat);
        if (trigger == null) {
            addNote(createDefaultNote(beat, 0.25f));
            trigger = getTrigger(beat);
        }
        setEditSelected(trigger);
    }

    private Note createDefaultNote(float start, float gate) {
        Note note = new Note(60, start, start + gate, 1f, 0);
        return note;
    }

    //----------------------------------
    // position
    //----------------------------------

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
     * @see OnPhraseChange
     * @see PhraseChangeKind#Position
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
    // playMeasure
    //----------------------------------

    /**
     * Returns the current measure of the play head based on the current length
     * of this phrase.
     * <p>
     * This measure value is considered "local", meaning it will never be
     * greater than 8.
     * <p>
     * In Pattern mode, this value is the same as the
     * {@link #getCurrentMeasure()} value.
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

    public Collection<Note> getEditMeasureNotes() {
        return getNotes(editMeasure);
    }

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

    /**
     * Returns the current measure playing in Song mode.
     * <p>
     * Note: The current bar is divisible by 4, the current measure is the sum
     * of all steps played currently in a song.
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

        //        getDispatcher().trigger(new OnPhraseChange(PhraseChangeKind.Beat, this, null));
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

    public boolean isLastBeat() {
        int beats = length * 4;
        return localBeat % beats == beats - 1;
    }

    public void onBeatChange(float beat) {
        localBeat = (int)toLocalBeat(beat, getLength());

        float fullMeasure = beat / 4;
        float measure = fullMeasure % getLength();
        setCurrentBeat(beat);

        setPlayMeasure((int)measure);
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
     * Returns the local current 16th step; possible values (0..15)
     */
    public int getLocalCurrentStep() {
        return machine.getRackSet().getSequencer().getCurrentSixteenthStep();
    }

    /**
     * Returns the global current 16th step; possible values (0..127)
     */
    public int getCurrentStep() {
        //return 0;
        throw new UnsupportedOperationException("getCurrentStep() not implemented yet");
    }

    /**
     * Increments the internal pointer of the measure position (1-8).
     * <p>
     * If the position is greater the {@link #getLength()}, the position is
     * clamped to the length.
     */
    public void incrementPosition() {
        int len = getLength();
        int value = position + 1;
        if (value > len)
            value = len;
        setPosition(value);
    }

    /**
     * Decrement the internal pointer of the measure position (1-8).
     * <p>
     * If the position is less than 1 the position is clamped to 1.
     */
    public void decrementPosition() {
        int value = position - 1;
        if (value < 1)
            value = 1;
        setPosition(value);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    Phrase() {
    }

    Phrase(ComponentInfo info, int index, MachineType machineType) {
        setInfo(info);
        this.index = index;
        this.machineType = machineType;
        this.triggerMap = new TriggerMap(this);
    }

    Phrase(ComponentInfo info, int index, Machine machine) {
        setInfo(info);
        this.index = index;
        this.machine = machine;
        this.machineType = machine.getMachineType();
        this.triggerMap = new TriggerMap(this);
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void transpose(int octave) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        switch (phase) {
            case Create:
                break;

            case Load:
                final IRack rack = context.getRack();

                final int machineIndex = machine.getMachineIndex();

                // set the current bank and pattern of the machine to query
                // the string pattern data
                // XXX when pattern_sequencer takes bank, pattern FIX THIS
                PatternSequencerMessage.BANK.send(rack, machineIndex, getBankIndex());
                PatternSequencerMessage.PATTERN.send(rack, machineIndex, getPatternIndex());

                // load one phrase per pattern; load ALL patterns
                // as caustic machine patterns
                length = (int)PatternSequencerMessage.NUM_MEASURES.query(rack, machineIndex);
                noteData = PatternSequencerMessage.QUERY_NOTE_DATA.queryString(rack, machineIndex);

                setNoteData(noteData);

                float tempo = OutputPanelMessage.BPM.query(rack);
                getInfo().addTag("" + tempo);
                break;

            case Update:
                PatternSequencerMessage.NUM_MEASURES.send(machine.getRack(),
                        machine.getMachineIndex(), length);
                for (Note note : triggerMap.getNotes()) {
                    if (note.isSelected()) {
                        triggerMap.update(note);
                    } else {
                        System.err.println("Didn't add note: " + note.toString());
                    }
                }
                break;

            case Restore:
                break;

            case Connect:
                break;

            case Disconnect:
                machine = null;
                break;
        }
    }

    /**
     * Clears all notes from all measures of the phrase.
     */
    public void clear() {
        Collection<Note> list = getNotes();
        for (Note note : list) {
            removeNote(note);
        }
        if (getNotes().size() != 0)
            throw new IllegalStateException("Phrase was not cleared");
        fireChange(PhraseChangeKind.ClearMeasure);
    }

    /**
     * Clears all notes from measure.
     * 
     * @see OnPhraseChange
     * @see PhraseChangeKind#ClearMeasure
     */
    public void clear(int measure) {
        Collection<Note> list = getNotes(measure);
        for (Note note : list) {
            removeNote(note);
        }
        if (getNotes(measure).size() != 0)
            throw new IllegalStateException("Phrase measure was not cleared");
        fireChange(PhraseChangeKind.ClearMeasure);
    }

    //--------------------------------------------------------------------------
    // TriggerMap Method :: API
    //--------------------------------------------------------------------------

    /**
     * @see TriggerMap#hasNote(int, float)
     */
    public boolean hasNote(int pitch, float beat) {
        return triggerMap.hasNote(pitch, beat);
    }

    /**
     * @see TriggerMap#getNotes()
     */
    public Collection<Note> getNotes() {
        return triggerMap.getNotes();
    }

    /**
     * @see TriggerMap#getNote(int, float)
     */
    public Note getNote(int pitch, float start) {
        return triggerMap.getNote(pitch, start);
    }

    /**
     * @see TriggerMap#getNotes(int)
     */
    public Collection<Note> getNotes(int measure) {
        return triggerMap.getNotes(measure);
    }

    /**
     * @see TriggerMap#addNote(int, float, float, float, int)
     */
    public Note addNote(int pitch, float beat, float gate, float velocity, int flags) {
        return triggerMap.addNote(pitch, beat, gate, velocity, flags);
    }

    /**
     * @see TriggerMap#addNote(int, int, float, float, int)
     */
    public Note addNote(int pitch, int step, float gate, float velocity, int flags) {
        return triggerMap.addNote(pitch, step, gate, velocity, flags);
    }

    /**
     * @see TriggerMap#addNote(Note)
     */
    public void addNote(Note note) {
        triggerMap.addNote(note);
    }

    /**
     * @see TriggerMap#addNotes(Collection)
     */
    public void addNotes(Collection<Note> notes) {
        triggerMap.addNotes(notes);
    }

    /**
     * @see TriggerMap#removeNote(int, float)
     */
    public Note removeNote(int pitch, float beat) {
        return triggerMap.removeNote(pitch, beat);
    }

    /**
     * @see TriggerMap#removeNote(int, int)
     */
    public Note removeNote(int pitch, int step) {
        return triggerMap.removeNote(pitch, step);
    }

    /**
     * @see TriggerMap#removeNote(Note)
     */
    public void removeNote(Note note) {
        triggerMap.removeNote(note);
    }

    /**
     * @see TriggerMap#containsTrigger(float)
     */
    public final boolean containsTrigger(float beat) {
        return triggerMap.containsTrigger(beat);
    }

    /**
     * @see TriggerMap#containsTrigger(int)
     */
    public final boolean containsTrigger(int step) {
        return triggerMap.containsTrigger(step);
    }

    /**
     * @see TriggerMap#getTrigger(float)
     */
    public final Trigger getTrigger(float beat) {
        return triggerMap.getTrigger(beat);
    }

    /**
     * @see TriggerMap#getTrigger(int)
     */
    public final Trigger getTrigger(int step) {
        return triggerMap.getTrigger(step);
    }

    /**
     * @see TriggerMap#getTriggers()
     */
    public Collection<Trigger> getTriggers() {
        return triggerMap.getTriggers();
    }

    /**
     * @see TriggerMap#getViewTriggers()
     */
    public Collection<Trigger> getViewTriggers() {
        return triggerMap.getViewTriggers();
    }

    /**
     * @see TriggerMap#getViewTriggerMap()
     */
    public Map<Integer, Trigger> getViewTriggerMap() {
        return triggerMap.getViewTriggerMap();
    }

    /**
     * @see TriggerMap#triggerOn(int, int, float, float, int)
     */
    public void triggerOn(int step, int pitch, float gate, float velocity, int flags) {
        triggerMap.triggerOn(step, pitch, gate, velocity, flags);
    }

    /**
     * @see TriggerMap#triggerOn(int)
     */
    public void triggerOn(int step) {
        triggerMap.triggerOn(step);
    }

    /**
     * @see TriggerMap#triggerOff(int)
     */
    public void triggerOff(int step) {
        triggerMap.triggerOff(step);
    }

    /**
     * @see TriggerMap#triggerUpdate(int, int, float, float, int)
     */
    public void triggerUpdate(int step, int pitch, float gate, float velocity, int flags) {
        triggerMap.triggerUpdate(step, pitch, gate, velocity, flags);
    }

    /**
     * @see TriggerMap#triggerUpdatePitch(int, int)
     */
    public void triggerUpdatePitch(int step, int pitch) {
        triggerMap.triggerUpdatePitch(step, pitch);
    }

    /**
     * @see TriggerMap#triggerUpdateGate(int, float)
     */
    public void triggerUpdateGate(int step, float gate) {
        triggerMap.triggerUpdateGate(step, gate);
    }

    /**
     * @see TriggerMap#triggerUpdateVelocity(int, float)
     */
    public void triggerUpdateVelocity(int step, float velocity) {
        triggerMap.triggerUpdateVelocity(step, velocity);
    }

    /**
     * @see TriggerMap#triggerUpdateFlags(int, int)
     * @see NoteFlag#None
     * @see NoteFlag#Slide
     * @see NoteFlag#Accent
     */
    public void triggerUpdateFlags(int step, int flags) {
        triggerMap.triggerUpdateFlags(step, flags);
    }

    //--------------------------------------------------------------------------
    // Private Method :: API
    //--------------------------------------------------------------------------

    protected void fireChange(PhraseChangeKind kind) {
        getDispatcher().trigger(new OnPhraseChange(kind, this, null));
    }

    protected void fireChange(PhraseChangeKind kind, Note phraseNote) {
        getDispatcher().trigger(new OnPhraseChange(kind, this, phraseNote));
    }

    public static float toLocalBeat(float beat, int length) {
        float r = (beat % (length * 4));
        return r;
    }

    @Override
    public String toString() {
        return "Bank:" + getBankIndex() + ",Pattern:" + getPatternIndex();
    }

    public enum PhraseChangeKind {

        /**
         * Dispatched every beat when the phrase is active.
         */
        Beat,

        /**
         * @see TrackPhrase#setLength(int)
         */
        Length,

        /**
         * @see TrackPhrase#setNoteData(String)
         */
        NoteData,

        /**
         * @see TrackPhrase#addNote(int, float, float, float, int)
         */
        NoteAdd,

        /**
         * @see TrackPhrase#removeNote(int, float)
         */
        NoteRemove,

        ClearMeasure,

        ClearPhrase,

        /**
         * @see TrackPhrase#setPlayMeasure(int)
         */
        PlayMeasure,

        /**
         * @see TrackPhrase#setEditMeasure(int)
         */
        EditMeasure,

        Scale,

        Position,

        EditSelection;
    }

    /**
     * @author Michael Schmalle
     * @see IRack#getDispatcher()
     */
    public static class OnPhraseChange {

        private final PhraseChangeKind kind;

        public PhraseChangeKind getKind() {
            return kind;
        }

        private final Phrase phrase;

        public Phrase getPhrase() {
            return phrase;
        }

        private final Note note;

        public Note getNote() {
            return note;
        }

        public OnPhraseChange(PhraseChangeKind kind, Phrase phrase, Note note) {
            this.kind = kind;
            this.phrase = phrase;
            this.note = note;
        }
    }
}
