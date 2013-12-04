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

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent.Resolution;

/**
 * The {@link Trigger} manages a list of {@link Note} instances.
 * <p>
 * All step calculations from the beat are "pull", meaning the step could be
 * calculated from 16th or 32nd resolution etc.
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
    private Map<Integer, Note> notes = new TreeMap<Integer, Note>();

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
     * <p>
     * All {@link Note} instances are updated with this selection value.
     * 
     * @param value Selected or unselected.
     */
    public void setSelected(boolean value) {
        selected = value;
        for (Note note : notes.values()) {
            note.setSelected(selected);
        }
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
        return notes.values();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Serialization.
     */
    Trigger() {
    }

    Trigger(float beat) {
        this.beat = beat;
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
    public final boolean hasNote(int pitch) {
        return notes.containsKey(pitch);
    }

    /**
     * Returns a {@link Note} at the specified pitch.
     * 
     * @param pitch The note MIDI pitch.
     */
    public final Note getNote(int pitch) {
        return notes.get(pitch);
    }

    /**
     * Add a note to the collection.
     * 
     * @param pitch The note MIDI pitch
     * @param startBeat The start float beat.
     * @param endBeat The end float beat.
     * @param velocity The velocity. (0..1)
     * @param flags The note flags (0, 1, 2) bit shift.
     * @param selected Whether the not is selected.
     * @throws IllegalStateException note already exists
     */
    public final Note addNote(int pitch, float startBeat, float endBeat, float velocity, int flags,
            boolean selected) {
        if (hasNote(pitch)) {
            throw new IllegalStateException("Note exists: " + beat + ", pitch: " + pitch);
        }
        Note note = new Note(pitch, startBeat, endBeat, velocity, flags);
        note.setSelected(selected);
        notes.put(pitch, note);
        return note;
    }

    /**
     * Add a note to the collection.
     * <p>
     * Note: The {@link Note} returned is not selected.
     * 
     * @param pitch The note MIDI pitch
     * @param startBeat The start float beat.
     * @param endBeat The end float beat.
     * @param velocity The velocity. (0..1)
     * @param flags The note flags (0, 1, 2) bit shift.
     * @throws IllegalStateException note already exists
     */
    public Note addNote(int pitch, float startBeat, float endBeat, float velocity, int flags) {
        return addNote(pitch, startBeat, endBeat, velocity, flags, false);
    }

    /**
     * Adds a note to the collection.
     * 
     * @param note The note to add.
     */
    public void addNote(Note note) {
        if (hasNote(note.getPitch())) {
            throw new IllegalStateException("Note exists:" + beat + ", pitch:" + note.getPitch());
        }
        notes.put(note.getPitch(), note);
    }

    /**
     * Removes a note at the specified pitch.
     * 
     * @param pitch The note pitch
     */
    public Note removeNote(int pitch) {
        Note note = notes.remove(pitch);
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
        return "[Trigger(" + selected + ", " + beat + ", " + notes + ")]";
    }
}
