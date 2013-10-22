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

package com.teotigraphix.caustk.rack.track;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.rack.ITrackSequencer.OnPhraseChange;
import com.teotigraphix.caustk.rack.ITrackSequencer.OnTriggerChange;
import com.teotigraphix.caustk.rack.ITrackSequencer.PhraseChangeKind;
import com.teotigraphix.caustk.rack.ITrackSequencer.TriggerChangeKind;
import com.teotigraphix.caustk.rack.tone.Tone;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent.Resolution;
import com.teotigraphix.caustk.rack.track.Phrase.Scale;

/**
 * A {@link TriggerMap} holds all {@link Trigger} instances for a {@link Phrase}
 * with the key being the trigger's start beat.
 */
public class TriggerMap {

    private static final int BEATS_IN_MEASURE = 4;

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private int indciesInView = 16;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    // <beat, Trigger<List<Note>>
    @Tag(0)
    private Map<Float, Trigger> map = new TreeMap<Float, Trigger>();

    @Tag(1)
    private Phrase phrase;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    final Phrase getPhrase() {
        return phrase;
    }

    final Tone getTone() {
        return phrase.getTone();
    }

    final PatternSequencerComponent getPatternSequencer() {
        return getTone().getPatternSequencer();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public TriggerMap() {
    }

    public TriggerMap(Phrase phrase) {
        this.phrase = phrase;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
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
        getPatternSequencer().addNote(pitch, beat, beat + gate, velocity, flags);
        firePhraseChange(PhraseChangeKind.NoteAdd, note);
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
        float beat = Resolution.toBeat(step, phrase.getResolution());
        return addNote(pitch, beat, gate, velocity, flags);
    }

    /**
     * Adds an existing {@link Note} instance to the trigger.
     * <p>
     * This does not dispatch an Add event nor does it call the {@link Tone}'s
     * native pattern sequencer, just adds the note to the trigger model.
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
     * to the {@link Tone}'s pattern sequencer.
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
            getPatternSequencer().removeNote(pitch, beat);
            // with remove note, we actually take the note out of the collection
            trigger.removeNote(note);
            firePhraseChange(PhraseChangeKind.NoteRemove, note);
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
        float beat = Resolution.toBeat(step, phrase.getResolution());
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
        float beat = Resolution.toBeat(step, phrase.getResolution());
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
        return getTrigger(Resolution.toBeat(step, phrase.getResolution()));
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
        int fromStep = (phrase.getPosition() - 1) * indciesInView;
        // find the end fromIndex + scale
        int toStep = endStepInView(fromStep);
        for (Trigger trigger : map.values()) {
            int step = trigger.getStep(phrase.getResolution());
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
            final int step = trigger.getStep(phrase.getResolution());
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
        final float beat = Resolution.toBeat(step, phrase.getResolution());
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
        getTone().getPatternSequencer().addNote(pitch, beat, beat + gate, velocity, flags);
        fireTriggerChange(TriggerChangeKind.Select, trigger);
    }

    /**
     * Triggers the step notes on, not changing an trigger/note properties.
     * 
     * @param step The trigger step.
     */
    public void triggerOn(int step) {
        Trigger trigger = getTrigger(step);
        if (trigger == null) {
            trigger = createInitTrigger(step);
            trigger.setSelected(true);
            map.put(Resolution.toBeat(step, phrase.getResolution()), trigger);
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
            getPatternSequencer().removeNote(note.getPitch(),
                    Resolution.toBeat(step, phrase.getResolution()));
        }
        trigger.setSelected(false);
        fireTriggerChange(TriggerChangeKind.Deselect, trigger);
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
        final float beat = Resolution.toBeat(step, phrase.getResolution());
        Note note = trigger.getNote(pitch);
        if (note == null)
            return;
        note.update(pitch, beat, beat + gate, velocity, flags);
        fireTriggerChange(TriggerChangeKind.Update, trigger);
    }

    /**
     * Updates all trigger notes with the new pitch.
     * 
     * @param step The trigger step.
     * @param pitch The new trigger pitch.
     */
    public void triggerUpdatePitch(int step, int pitch) {
        Trigger trigger = getTrigger(step);
        for (Note note : trigger.getNotes()) {
            triggerUpdate(step, pitch, note.getGate(), note.getVelocity(), note.getFlags());
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
    // Private API :: Methods
    //--------------------------------------------------------------------------

    protected void firePhraseChange(PhraseChangeKind kind, Note note) {
        phrase.getDispatcher().trigger(new OnPhraseChange(kind, phrase, note));
    }

    protected void fireTriggerChange(TriggerChangeKind kind, Trigger trigger) {
        phrase.getDispatcher().trigger(new OnTriggerChange(kind, phrase, trigger));
    }

    private int endStepInView(int fromStep) {
        Scale scale = phrase.getScale();
        if (scale == Scale.SIXTEENTH)
            return fromStep + indciesInView;
        if (scale == Scale.THIRTYSECOND)
            return fromStep + indciesInView;
        return -1;
    }

    private Trigger createInitTrigger(int step) {
        float beat = Resolution.toBeat(step, phrase.getResolution());
        Trigger trigger = new Trigger(beat);
        trigger.addNote(beat, 60, 0.25f, 1f, 0);
        return trigger;
    }

    public void setSelectedBankPattern(int bank, int pattern) {
        getTone().getPatternSequencer().setSelectedBankPattern(bank, pattern);
    }
}
