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
import java.util.List;

import com.teotigraphix.caustk.tone.components.PatternSequencerComponent.Resolution;

public class Trigger {

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // beat
    //----------------------------------

    private float beat;

    public float getBeat() {
        return beat;
    }

    public int getStep(Resolution resolution) {
        return Resolution.toStep(beat, resolution);
    }

    //----------------------------------
    // selected
    //----------------------------------

    private boolean selected = false;

    public void setSelected(boolean value) {
        selected = value;
    }

    public boolean isSelected() {
        return selected;
    }

    //----------------------------------
    // notes
    //----------------------------------

    private List<Note> notes;

    public List<Note> getNotes() {
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
    // Method API
    //--------------------------------------------------------------------------

    public boolean hasNote(int pitch) {
        return getNote(pitch) != null;
    }

    public Note getNote(int pitch) {
        for (Note note : notes) {
            if (note.getPitch() == pitch)
                return note;
        }
        return null;
    }

    public Note addNote(float beat, int pitch, float gate, float velocity, int flags) {
        if (hasNote(pitch)) {
            throw new IllegalStateException("Note exists");
        }
        Note note = new Note(pitch, beat, beat + gate, velocity, flags);
        addNote(note);
        return note;
    }

    public void addNote(Note note) {
        if (hasNote(note.getPitch()))
            return;
        getNotes().add(note);
    }

    public Note removeNote(int pitch) {
        Note note = getNote(pitch);
        if (note != null) {
            notes.remove(note);
        }
        return note;
    }

}
