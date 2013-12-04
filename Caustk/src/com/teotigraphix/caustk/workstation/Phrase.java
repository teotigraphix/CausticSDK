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
import java.util.TreeMap;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.OutputPanelMessage;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.tone.RackTone;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent.Resolution;
import com.teotigraphix.caustk.utils.PatternUtils;
import com.teotigraphix.caustk.utils.PhraseUtils;

/**
 * @author Michael Schmalle
 */
public class Phrase extends CaustkComponent {

    private static final int BEATS_IN_MEASURE = 4;

    private transient int indciesInView = 16;

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
    private String noteData;

    @Tag(104)
    private int length = 1;

    @Tag(105)
    private int position = 1;

    @Tag(106)
    private Object data;

    // <beat, Trigger<List<Note>>
    @Tag(108)
    private Map<Float, Trigger> map = new TreeMap<Float, Trigger>();

    @Tag(120)
    private int playMeasure = 0;

    @Tag(121)
    private int editMeasure = 0;

    @Tag(122)
    private int currentMeasure = 0;

    @Tag(123)
    private Scale scale = Scale.SIXTEENTH;

    @Tag(124)
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

    void setIndex(int value) {
        index = value;
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

    void setMachine(Machine value) {
        machine = value;
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
     * @see OnPhraseChange
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

    private int currentSixteenthStep;

    public int getCurrentSixteenthStep() {
        return currentSixteenthStep;
    }

    private int localBeat;

    /**
     * Return the ISong current beat.
     */
    public float getCurrentBeat() {
        return floatBeat;
    }

    void setCurrentBeat(float value) {
        if (value == floatBeat)
            return;

        floatBeat = value;
        //fireChange(PhraseChangeKind.Beat);
    }

    public boolean isLastBeat() {
        int beats = length * 4;
        return localBeat % beats == beats - 1;
    }

    public void frameChange(float delta, int measure, float beat) {

        floatBeat = beat;

        int round = (int)Math.floor(beat);
        if (round != currentBeat) {

            localBeat = (int)toLocalBeat(beat, getLength());
            currentBeat = round;

            setCurrentBeat(beat);
            setCurrentMeasure(measure);

            float fullMeasure = beat / 4;
            float playMeasure = fullMeasure % getLength();

            setPlayMeasure((int)playMeasure);

            fireChange(PhraseChangeKind.Beat);
        }

        // sixteenth step calculation
        int step = (int)Math.floor((beat % 4) * 4);
        if (step != currentSixteenthStep) {
            currentSixteenthStep = step;
            fireChange(PhraseChangeKind.Step);
        }
    }

    public void beatChange(int measure, float beat) {

    }

    /**
     * Returns the actual beat in the current measure.
     * <p>
     * Example; measure 4, beat 14 would be beat 2 in the measure (0 index - 3rd
     * beat in measure).
     * </p>
     * 
     * @return 0-3
     */
    public float getMeasureBeat() {
        return PhraseUtils.toMeasureBeat(floatBeat);
    }

    /**
     * Returns 0-31(8 bars), 0-15(4 bars), 0-7(2 bars), 0-3(1 bar) based on the
     * length.
     */
    public float getLocalBeat() {
        return PhraseUtils.toLocalBeat(currentBeat, getLength());
    }

    /**
     * Returns the local measure calculated from the {@link #getLocalBeat()}.
     */
    public int getLocalMeasure() {
        return PhraseUtils.toLocalMeasure(floatBeat, getLength());
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
    }

    Phrase(ComponentInfo info, int index, Machine machine) {
        setInfo(info);
        this.index = index;
        this.machine = machine;
        this.machineType = machine.getMachineType();
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
                PatternSequencerMessage.NUM_MEASURES.send(machine.getEngine(),
                        machine.getMachineIndex(), length);
                for (Trigger trigger : getTriggers()) {
                    if (trigger.isSelected()) {
                        for (Note note : trigger.getNotes()) {
                            //                            if (note.isSelected()) {
                            PhraseUtils.update(this, note);
                            //                            } else {
                            //                                System.err.println("Didn't add note: " + note.toString());
                            //                            }
                        }
                    } else {
                        //System.err.println("Didn't add note: " + note.toString());
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

    //----------------------------------
    // Note
    //----------------------------------

    /**
     * Returns whether a {@link Note} exists for the beat trigger at the
     * specified pitch.
     * 
     * @param pitch The MIDI pitch.
     * @param beat The trigger start beat.
     */
    public boolean hasNote(int pitch, float beat) {
        final Trigger trigger = getTrigger(beat);
        if (trigger == null)
            return false;
        return trigger.hasNote(pitch);
    }

    /**
     * Returns one whole collection of notes for all triggers defined.
     * <p>
     * The {@link Note}s are in no specific order.
     */
    public Collection<Note> getNotes() {
        final Collection<Note> result = new ArrayList<Note>();
        for (Trigger trigger : getTriggers()) {
            for (Note note : trigger.getNotes()) {
                result.add(note);
            }
        }
        return result;
    }

    /**
     * Returns the note at the trigger beat, <code>null</code> if the trigger or
     * note does not exist.
     * 
     * @param pitch The MIDI pitch.
     * @param beat The trigger start beat.
     */
    public Note getNote(int pitch, float beat) {
        final Trigger trigger = getTrigger(beat);
        if (trigger == null)
            return null;
        return trigger.getNote(pitch);
    }

    /**
     * Returns a collection of {@link Note}s for the measure span of 4 beats.
     * <p>
     * The measure does not need to be within the current phrase length if they
     * were added prior to changing the phrase length.
     * 
     * @param measure The measure to retrieve notes for.
     */
    public Collection<Note> getNotes(int measure) {
        final Collection<Note> result = new ArrayList<Note>();
        for (Trigger trigger : getTriggers()) {
            for (Note note : trigger.getNotes()) {
                int beat = (int)Math.floor(note.getStart());
                int startBeat = (measure * BEATS_IN_MEASURE);
                if (beat >= startBeat && beat < startBeat + BEATS_IN_MEASURE) {
                    result.add(note);
                }
            }
        }
        return result;
    }

    /**
     * Adds a {@link Note} to the {@link Trigger} at the specified beat.
     * <p>
     * Will create a {@link Trigger} based on the {@link Phrase#getResolution()}
     * if it doesn't exist.
     * <p>
     * The {@link Trigger} is automatically selected when inserted into the map.
     * 
     * @param pitch The MIDI pitch value.
     * @param beat The start beat.
     * @param gate The gate length.
     * @param velocity The note velocity.
     * @param flags The accent, slide flags.
     * @see OnPhraseChange
     * @see PhraseChangeKind.NoteAdd
     */
    public Note addNote(int pitch, float beat, float gate, float velocity, int flags) {
        Trigger trigger = getTrigger(beat);
        if (trigger == null) {
            trigger = new Trigger(beat);
            trigger.setSelected(true);
            map.put(beat, trigger);
        }

        Note note = trigger.addNote(beat, pitch, gate, velocity, flags);
        // addNote(pitch, beat, beat + gate, velocity, flags);
        PatternSequencerMessage.NOTE_DATA.send(getMachine().getRackTone().getEngine(), getMachine()
                .getMachineIndex(), note.getStart(), note.getPitch(), note.getVelocity(), note
                .getEnd(), note.getFlags());
        fireChange(PhraseChangeKind.NoteAdd, note);

        return note;
    }

    /**
     * Adds a {@link Note} to the {@link Trigger} at the specified step.
     * <p>
     * Will create a {@link Trigger} based on the {@link Phrase#getResolution()}
     * if it doesn't exist.
     * 
     * @param pitch The MIDI pitch value.
     * @param step The trigger step based on the {@link Phrase#getResolution()}.
     * @param gate The gate length.
     * @param velocity The note velocity.
     * @param flags The accent, slide flags.
     * @see TriggerMap#addNote(int, float, float, float, int)
     */
    public Note addNote(int pitch, int step, float gate, float velocity, int flags) {
        float beat = Resolution.toBeat(step, getResolution());
        return addNote(pitch, beat, gate, velocity, flags);
    }

    /**
     * Adds an existing {@link Note} instance to the trigger.
     * <p>
     * This does not dispatch an Add event nor does it call the {@link RackTone}
     * 's native pattern sequencer, just adds the note to the trigger model.
     * 
     * @param note The {@link Note} to add.
     */
    public void addNote(Note note) {
        Trigger trigger = getTrigger(note.getStart());
        if (trigger == null) {
            trigger = new Trigger(note.getStart());
            map.put(note.getStart(), trigger);
        }
        trigger.addNote(note);
    }

    /**
     * Adds the collection of notes to the model, will not dispatch Add or add
     * to the {@link RackTone}'s pattern sequencer.
     * 
     * @param notes The collection of notes.
     */
    public void addNotes(Collection<Note> notes) {
        for (Note note : notes) {
            addNote(note);
        }
    }

    /**
     * Removes a {@link Note} at the specified beat and pitch.
     * 
     * @param pitch The MIDI pitch value.
     * @param beat The start beat.
     * @see OnPhraseChange
     * @see PhraseChangeKind#NoteRemove
     * @return <code>null</code> if the beat and pitch does not exist.
     */
    public Note removeNote(int pitch, float beat) {
        Trigger trigger = getTrigger(beat);
        if (trigger == null)
            return null;
        Note note = trigger.getNote(pitch);
        if (note != null) {
            //removeNote(pitch, beat);
            PatternSequencerMessage.NOTE_DATA_REMOVE.send(getMachine().getRackTone().getEngine(),
                    getMachine().getMachineIndex(), note.getStart(), note.getPitch());
            // with remove note, we actually take the note out of the collection
            trigger.removeNote(note);
            fireChange(PhraseChangeKind.NoteRemove, note);
        }
        return note;
    }

    /**
     * Removes a {@link Note} at the specified step and pitch based on the
     * {@link Phrase#getResolution()}.
     * 
     * @param pitch The MIDI pitch value.
     * @param step The start step.
     * @return <code>null</code> if the beat and pitch does not exist.
     * @see #removeNote(float, int)
     */
    public Note removeNote(int pitch, int step) {
        float beat = Resolution.toBeat(step, getResolution());
        return removeNote(pitch, beat);
    }

    /**
     * Removes a {@link Note} using the passed note's start and pitch.
     * <p>
     * Does not check for equal identy on the Note instance.
     * 
     * @param note The {@link Note} to remove.
     * @return <code>null</code> if the beat and pitch does not exist.
     */
    public Note removeNote(Note note) {
        return removeNote(note.getPitch(), note.getStart());
    }

    //----------------------------------
    // Trigger
    //----------------------------------

    /**
     * Returns whether the map contains a {@link Trigger} at the specified beat.
     * 
     * @param beat The start beat of the trigger.
     */
    public final boolean containsTrigger(float beat) {
        return map.containsKey(beat);
    }

    /**
     * Returns whether the map contains a {@link Trigger} at the specified step.
     * 
     * @param step The start step of the trigger.
     */
    public final boolean containsTrigger(int step) {
        float beat = Resolution.toBeat(step, getResolution());
        return containsTrigger(beat);
    }

    /**
     * Returns the {@link Trigger} at the specified beat or <code>null</code> if
     * the {@link Trigger} has not been created.
     * 
     * @param beat The start beat of the trigger.
     */
    public final Trigger getTrigger(float beat) {
        return map.get(beat);
    }

    /**
     * Returns the {@link Trigger} at the specified step or <code>null</code> if
     * the {@link Trigger} has not been created.
     * 
     * @param step The step of the trigger based on the
     *            {@link Phrase#getResolution()}.
     */
    public final Trigger getTrigger(int step) {
        return getTrigger(Resolution.toBeat(step, getResolution()));
    }

    /**
     * Returns all the triggers in the phrase, no view calculations(position).
     * <p>
     * A copy of the collection is returned.
     */
    public Collection<Trigger> getTriggers() {
        return new ArrayList<Trigger>(map.values());
    }

    /**
     * Returns a collection of triggers found within the current view using the
     * position and number of indicies, default 16 steps.
     */
    public Collection<Trigger> getViewTriggers() {
        final ArrayList<Trigger> result = new ArrayList<Trigger>();
        // find the start (position - 1) * resolution
        int fromStep = (getPosition() - 1) * indciesInView;
        // find the end fromIndex + scale
        int toStep = endStepInView(fromStep);
        for (Trigger trigger : map.values()) {
            int step = trigger.getStep(getResolution());
            if (step >= fromStep && step < toStep)
                result.add(trigger);
        }
        return result;
    }

    /**
     * Returns a sorted map of all view triggers with key as the step based on
     * {@link Phrase#getResolution()} and value of {@link Trigger}.
     */
    public Map<Integer, Trigger> getViewTriggerMap() {
        final Map<Integer, Trigger> result = new TreeMap<Integer, Trigger>();
        for (Trigger trigger : getViewTriggers()) {
            final int step = trigger.getStep(getResolution());
            result.put(step, trigger);
        }
        return result;
    }

    /**
     * Triggers a note at the specified step and will not calculate the view
     * step.
     * <p>
     * This method assumes the step passed is NOT relative or the view step.
     * 
     * @param step The trigger step.
     * @param pitch The trigger pitch.
     * @param gate The trigger gate.
     * @param velocity The trigger velocity.
     * @param flags The trigger flags.
     */
    public void triggerOn(int step, int pitch, float gate, float velocity, int flags) {
        final float beat = Resolution.toBeat(step, getResolution());
        Trigger trigger = getTrigger(step);
        if (trigger == null) {
            trigger = new Trigger(beat);
            map.put(beat, trigger);
        }
        Note note = trigger.getNote(pitch);
        if (note == null) {
            note = trigger.addNote(beat, pitch, gate, velocity, flags);
        } else {
            note.update(pitch, beat, beat + gate, velocity, flags);
        }
        trigger.setSelected(true);
        addNote(pitch, beat, beat + gate, velocity, flags);
    }

    /**
     * Triggers the step notes on, not changing an trigger/note properties.
     * 
     * @param step The trigger step.
     */
    public void triggerOn(int step) {
        Trigger trigger = getTrigger(step);
        if (trigger == null) {
            trigger = PhraseUtils.createInitTrigger(this, step);
            trigger.setSelected(true);
            map.put(Resolution.toBeat(step, getResolution()), trigger);
        }
        for (Note note : trigger.getNotes()) {
            triggerOn(step, note.getPitch(), note.getGate(), note.getVelocity(), note.getFlags());
        }
    }

    /**
     * Triggers a note off at the specified step, this is an absolute step
     * within the known trigger map's length of measures.
     * 
     * @param step The absolute step to unselect.
     */
    public void triggerOff(int step) {
        Trigger trigger = getTrigger(step);
        for (Note note : trigger.getNotes()) {
            removeNote(note.getPitch(), Resolution.toBeat(step, getResolution()));
        }
        trigger.setSelected(false);
    }

    /**
     * Updates an existing trigger by pitch, if the {@link Trigger} does not
     * exist with the pitch, no event is dispatched.
     * 
     * @param step The trigger step.
     * @param pitch The trigger pitch.
     * @param gate The trigger gate.
     * @param velocity The trigger velocity.
     * @param flags The trigger flags.
     */
    public void triggerUpdate(int step, int pitch, float gate, float velocity, int flags) {
        Trigger trigger = getTrigger(step);
        if (trigger == null)
            return;
        final float beat = Resolution.toBeat(step, getResolution());
        Note note = trigger.getNote(pitch);
        if (note == null)
            return;
        note.update(pitch, beat, beat + gate, velocity, flags);
        PatternSequencerMessage.NOTE_DATA_REMOVE.send(getMachine().getEngine(), getMachine()
                .getMachineIndex(), note.getStart(), note.getPitch());
        PhraseUtils.update(this, note);
    }

    /**
     * Updates all trigger notes with the new pitch.
     * 
     * @param step The trigger step.
     * @param pitch The new trigger pitch.
     */
    public void triggerUpdatePitch(int step, int pitch, int oldPitch) {
        Trigger trigger = getTrigger(step);
        for (Note note : trigger.getNotes()) {
            if (note.getPitch() == oldPitch) {
                final float beat = Resolution.toBeat(step, getResolution());
                PatternSequencerMessage.NOTE_DATA_REMOVE.send(getMachine().getEngine(),
                        getMachine().getMachineIndex(), note.getStart(), oldPitch);
                note.update(pitch, beat, beat + note.getGate(), note.getVelocity(), note.getFlags());
                if (trigger.isSelected())
                    PhraseUtils.update(this, note);
            }
        }
    }

    /**
     * Updates all trigger notes with the new gate.
     * 
     * @param step The trigger gate.
     * @param pitch The new trigger pitch.
     */
    public void triggerUpdateGate(int step, float gate) {
        Trigger trigger = getTrigger(step);
        for (Note note : trigger.getNotes()) {
            triggerUpdate(step, note.getPitch(), gate, note.getVelocity(), note.getFlags());
        }
    }

    /**
     * Updates all trigger notes with the new velocity.
     * 
     * @param step The trigger step.
     * @param velocity The new trigger velocity.
     */
    public void triggerUpdateVelocity(int step, float velocity) {
        Trigger trigger = getTrigger(step);
        for (Note note : trigger.getNotes()) {
            triggerUpdate(step, note.getPitch(), note.getGate(), velocity, note.getFlags());
        }
    }

    /**
     * Updates all trigger notes with the new flags.
     * 
     * @param step The trigger step.
     * @param flags The new trigger flags.
     */
    public void triggerUpdateFlags(int step, int flags) {
        Trigger trigger = getTrigger(step);
        for (Note note : trigger.getNotes()) {
            triggerUpdate(step, note.getPitch(), note.getGate(), note.getVelocity(), flags);
        }
    }

    //--------------------------------------------------------------------------
    // Private Method :: API
    //--------------------------------------------------------------------------

    protected void fireChange(PhraseChangeKind kind) {
        fireChange(kind, null);
    }

    protected void fireChange(PhraseChangeKind kind, Note note) {
        machine.getDispatcher().trigger(new OnPhraseChange(kind, this, note));
    }

    public static float toLocalBeat(float beat, int length) {
        float r = (beat % (length * 4));
        return r;
    }

    private int endStepInView(int fromStep) {
        if (scale == Scale.SIXTEENTH)
            return fromStep + indciesInView;
        if (scale == Scale.THIRTYSECOND)
            return fromStep + indciesInView;
        return -1;
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

        Step,

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
     * @see Machine#getDispatcher()
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
