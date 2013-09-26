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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.teotigraphix.caustk.tone.components.PatternSequencerComponent.Resolution;

public class Trigger implements Serializable {

    private static final long serialVersionUID = 5159381498973786204L;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // beat
    //----------------------------------

    private float beat;

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
    // selected
    //----------------------------------

    private boolean selected = false;

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

    //----------------------------------
    // notes
    //----------------------------------

    private List<Note> notes;

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
            throw new IllegalStateException("Note exists");
        }
        Note note = new Note(pitch, beat, beat + gate, velocity, flags);
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
