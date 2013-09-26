
package com.teotigraphix.caustk.sequencer.track;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.teotigraphix.caustk.sequencer.ITrackSequencer.OnPhraseChange;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.PhraseChangeKind;
import com.teotigraphix.caustk.sequencer.track.Phrase.Scale;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.components.PatternSequencerComponent;
import com.teotigraphix.caustk.tone.components.PatternSequencerComponent.Resolution;

public class TriggerMap implements Serializable {

    private static final long serialVersionUID = -7413118378233405467L;

    // <beat, Trigger<List<Note>>
    private Map<Float, Trigger> map = new TreeMap<Float, Trigger>();

    private Phrase trackPhrase;

    protected Tone getTone() {
        return trackPhrase.getTone();
    }

    public PatternSequencerComponent getPatternSequencer() {
        return getTone().getPatternSequencer();
    }

    public TriggerMap(Phrase trackPhrase) {
        this.trackPhrase = trackPhrase;
    }

    /**
     * Returns the {@link Trigger} at the specified beat.
     * <p>
     * Will create a {@link Trigger} based on the {@link Resolution} if it
     * doesn't exist.
     * 
     * @param beat The start beat of the trigger.
     */
    public final Trigger getTrigger(float beat) {
        return map.get(beat);
    }

    public final Trigger getTrigger(int step) {
        return getTrigger(Resolution.toBeat(step, trackPhrase.getResolution()));
    }

    /**
     * Returns all the steps in the phrase, no view calculations(position).
     * <p>
     * A copy of the collection is returned.
     */
    public Collection<Trigger> getTriggers() {
        return new ArrayList<Trigger>(map.values());
    }

    public List<Trigger> getViewTriggers() {
        ArrayList<Trigger> result = new ArrayList<Trigger>();
        // find the start (position - 1) * resolution
        int fromStep = (trackPhrase.getPosition() - 1) * indciesInView;
        // find the end fromIndex + scale
        int toStep = endStepInView(fromStep);
        for (Trigger trigger : map.values()) {
            int step = trigger.getStep(trackPhrase.getResolution());
            if (step >= fromStep && step < toStep)
                result.add(trigger);
        }
        //return new ArrayList<Trigger>(map.values()).subList(fromStep, toStep);
        return result;
    }

    public Map<Integer, Trigger> getViewTriggerMap() {
        Map<Integer, Trigger> result = new HashMap<Integer, Trigger>();
        for (Trigger trigger : getViewTriggers()) {
            final int step = trigger.getStep(trackPhrase.getResolution());
            result.put(step, trigger);
        }
        return result;
    }

    //--------------------------------------------------------------------------
    // Note
    //--------------------------------------------------------------------------

    public boolean hasNote(int pitch, float beat) {
        Trigger trigger = getTrigger(beat);
        if (trigger == null)
            return false;
        return trigger.hasNote(pitch);
    }

    /**
     * Returns the note at the trigger beat.
     * 
     * @param pitch
     * @param beat
     */
    public Note getNote(int pitch, float beat) {
        Trigger trigger = getTrigger(beat);
        if (trigger == null)
            return null;
        return trigger.getNote(pitch);
    }

    public Collection<Note> getNotes() {
        Collection<Note> result = new ArrayList<Note>();
        for (Trigger trigger : getTriggers()) {
            for (Note note : trigger.getNotes()) {
                result.add(note);
            }
        }
        return result;
    }

    public Collection<Note> getNotes(int measure) {
        Collection<Note> result = new ArrayList<Note>();
        for (Trigger trigger : getTriggers()) {
            for (Note note : trigger.getNotes()) {
                int beat = (int)Math.floor(note.getStart());
                int startBeat = (measure * 4);
                if (beat >= startBeat && beat < startBeat + 4) {
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
     * 
     * @param pitch The MIDI pitch value.
     * @param beat The start beat.
     * @param gate The gate length.
     * @param velocity The note velocity.
     * @param flags The accent, slide flags.
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
        getPatternSequencer().addNote(pitch, beat, beat = gate, velocity, flags);
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
     * @return
     */
    public Note addNote(int pitch, int step, float gate, float velocity, int flags) {
        float beat = Resolution.toBeat(step, trackPhrase.getResolution());
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

    public Note removeNote(float beat, int pitch) {
        Trigger trigger = getTrigger(beat);
        if (trigger == null)
            return null;
        Note note = trigger.getNote(pitch);
        if (note != null) {
            getPatternSequencer().removeNote(pitch, beat);
        }
        return note;
    }

    public Note removeNote(int step, int pitch) {
        float beat = Resolution.toBeat(step, trackPhrase.getResolution());
        return removeNote(beat, pitch);
    }

    public Note removeNote(Note note) {
        return removeNote(note.getStart(), note.getPitch());
    }

    //--------------------------------------------------------------------------
    // Triggers
    //--------------------------------------------------------------------------

    public final boolean containsTrigger(float beat) {
        return map.containsKey(beat);
    }

    public final boolean containsTrigger(int step) {
        float beat = Resolution.toBeat(step, trackPhrase.getResolution());
        return containsTrigger(beat);
    }

    /**
     * Triggers a note at the specified step and will not calculate the view
     * step.
     * <p>
     * This method assumes the step passed is NOT relative or the view step.
     * 
     * @param step
     * @param pitch
     * @param gate
     * @param velocity
     * @param flags
     */
    public void triggerOn(int step, int pitch, float gate, float velocity, int flags) {
        float beat = Resolution.toBeat(step, trackPhrase.getResolution());

        Trigger trigger = getTrigger(step);
        if (trigger == null) {
            trigger = new Trigger(beat);
            map.put(beat, trigger);
        }

        Note note = trigger.getNote(pitch);
        if (note == null) {
            note = trigger.addNote(beat, pitch, gate, velocity, flags);
        }

        note.update(pitch, beat, beat + gate, velocity, flags);

        trigger.setSelected(true);

        getTone().getPatternSequencer().addNote(pitch, beat, beat + gate, velocity, flags);

        //        fireTriggerChange(TriggerChangeKind.Selected, trigger);
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
            map.put(Resolution.toBeat(step, trackPhrase.getResolution()), trigger);
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
                    Resolution.toBeat(step, trackPhrase.getResolution()));
        }
        trigger.setSelected(false);
        //        fireTriggerChange(TriggerChangeKind.Selected, trigger);
    }

    public void triggerUpdate(int step, int pitch, float gate, float velocity, int flags) {
        triggerOff(step);
        triggerOn(step, pitch, gate, velocity, flags);
    }

    public void triggerUpdatePitch(int step, int pitch) {
        Trigger trigger = getTrigger(step);
        for (Note note : trigger.getNotes()) {
            triggerUpdate(step, pitch, note.getGate(), note.getVelocity(), note.getFlags());
        }
        //        fireTriggerChange(TriggerChangeKind.Pitch, trigger);
    }

    public void triggerUpdateGate(int step, float gate) {
        Trigger trigger = getTrigger(step);
        for (Note note : trigger.getNotes()) {
            triggerUpdate(step, note.getPitch(), gate, note.getVelocity(), note.getFlags());
        }
        //        fireTriggerChange(TriggerChangeKind.Gate, trigger);
    }

    public void triggerUpdateVelocity(int step, float velocity) {
        Trigger trigger = getTrigger(step);
        for (Note note : trigger.getNotes()) {
            triggerUpdate(step, note.getPitch(), note.getGate(), velocity, note.getFlags());
        }
        //        fireTriggerChange(TriggerChangeKind.Velocity, trigger);
    }

    public void triggerUpdateFlags(int step, int flags) {
        Trigger trigger = getTrigger(step);
        for (Note note : trigger.getNotes()) {
            triggerUpdate(step, note.getPitch(), note.getGate(), note.getVelocity(), flags);
        }
        //        fireTriggerChange(TriggerChangeKind.Flags, trigger);
    }

    protected void fireChange(PhraseChangeKind kind, Note phraseNote) {
        trackPhrase.getDispatcher().trigger(new OnPhraseChange(kind, trackPhrase, phraseNote));
    }

    private int indciesInView = 16;

    private int endStepInView(int fromStep) {
        Scale scale = trackPhrase.getScale();
        if (scale == Scale.SIXTEENTH)
            return fromStep + indciesInView;
        if (scale == Scale.THIRTYSECOND)
            return fromStep + indciesInView;
        return -1;
    }

    private Trigger createInitTrigger(int step) {
        float beat = Resolution.toBeat(step, trackPhrase.getResolution());
        Trigger trigger = new Trigger(beat);
        trigger.addNote(beat, 60, 0.25f, 1f, 0);
        return trigger;
    }

    public void setSelectedBankPattern(int bank, int pattern) {
        getTone().getPatternSequencer().setSelectedBankPattern(bank, pattern);
    }

}
