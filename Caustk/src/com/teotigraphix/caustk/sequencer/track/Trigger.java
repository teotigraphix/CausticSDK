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

package com.teotigraphix.caustk.sequencer.track;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.tone.components.PatternSequencerComponent.Resolution;

/**
 * The {@link Trigger} manages a list of {@link Note} instances.
 */
public class Trigger {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private float beat;

    @Tag(1)
    private Object data;

    @Tag(2)
    private boolean selected = false;

    @Tag(3)
    private List<Note> notes;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // beat
    //----------------------------------

    /**
     * The beat marker of this trigger.
     */
    public float getBeat() {
        return beat;
    }

    /**
     * Convert the beat into a step based on the passed {@link Resolution}.
     * 
     * @param resolution The {@link Resolution} used to calculate the step.
     */
    public int getStep(Resolution resolution) {
        return Resolution.toStep(beat, resolution);
    }

    //----------------------------------
    // data
    //----------------------------------

    /**
     * Returns the trigger's abstract data if any.
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets the data for the trigger's.
     * <p>
     * Clients of the trigger's can use this property to tack on additional data
     * that relates to the trigger's in the context of their application.
     * <p>
     * Works with serialization.
     * 
     * @param value The data associated with the trigger's and client
     *            application.
     */
    public void setData(Object value) {
        data = value;
    }

    //----------------------------------
    // selected
    //----------------------------------

    /**
     * Whether the trigger is selected.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Set the trigger selected, may mean different things in different
     * applications.
     * 
     * @param value Selected or unselected.
     */
    public void setSelected(boolean value) {
        selected = value;
    }

    /**
     * Returns whether a {@link Note} has been selected.
     * 
     * @param pitch The pitch to test for selection.
     */
    public boolean isSelected(int pitch) {
        Note note = getNote(pitch);
        if (note == null)
            return false;
        return note.isSelected();
    }

    //----------------------------------
    // notes
    //----------------------------------

    /**
     * The collections of notes the trigger holds.
     * <p>
     * Notes are added and removed with
     * {@link #addNote(float, int, float, float, int)} and
     * {@link #removeNote(int)}.
     */
    public Collection<Note> getNotes() {
        return notes;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Trigger() {
    }

    public Trigger(float beat) {
        this.beat = beat;
        notes = new ArrayList<Note>();
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    /**
     * Returns whether a note exists within the collection at the specified MIDI
     * pitch.
     * 
     * @param pitch The note MIDI pitch.
     */
    public boolean hasNote(int pitch) {
        return getNote(pitch) != null;
    }

    /**
     * Returns a {@link Note} at the specified pitch.
     * 
     * @param pitch The note MIDI pitch.
     */
    public Note getNote(int pitch) {
        for (Note note : notes) {
            if (note.getPitch() == pitch)
                return note;
        }
        return null;
    }

    /**
     * Add a note to the collection.
     * 
     * @param beat The start float beat.
     * @param pitch The note MIDI pitch
     * @param gate The gate length. (0..1)
     * @param velocity The velocity. (0..1)
     * @param flags The note flags (0, 1, 2) bit shift.
     * @throws IllegalStateException note already exists
     */
    public Note addNote(float beat, int pitch, float gate, float velocity, int flags) {
        if (hasNote(pitch)) {
            throw new IllegalStateException("Note exists:" + beat + ", pitch:" + pitch);
        }
        Note note = new Note(pitch, beat, beat + gate, velocity, flags);
        note.setSelected(true);
        addNote(note);
        return note;
    }

    /**
     * Adds a note to the collection.
     * 
     * @param note The note to add.
     */
    public void addNote(Note note) {
        if (hasNote(note.getPitch()))
            return;
        getNotes().add(note);
    }

    /**
     * Removes a note at the specified pitch.
     * 
     * @param pitch The note pitch
     */
    public Note removeNote(int pitch) {
        Note note = getNote(pitch);
        if (note != null) {
            note.setSelected(false);
            notes.remove(note);
        }
        return note;
    }

    /**
     * Removes a note using it's pitch as the key.
     * 
     * @param note The note to remove, must exist in the collection.
     */
    public void removeNote(Note note) {
        removeNote(note.getPitch());
    }

    @Override
    public String toString() {
        return "[Trigger(" + getStep(Resolution.SIXTEENTH) + ", " + notes + ")]";
    }

}
