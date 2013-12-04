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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent.Resolution;
import com.teotigraphix.caustk.utils.MathUtils;

/**
 * A single note held within a {@link TriggerMap}.
 * 
 * @see NoteFlag
 */
public class Note {

    private static final String SPACE = " ";

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private int pitch;

    @Tag(1)
    private float start;

    @Tag(2)
    private float end;

    @Tag(3)
    private float velocity;

    @Tag(4)
    private int flags;

    @Tag(5)
    private boolean selected;

    @Tag(6)
    private Object data;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // pitch
    //----------------------------------

    /**
     * The MIDI pitch of the note.
     */
    public int getPitch() {
        return pitch;
    }

    //----------------------------------
    // start
    //----------------------------------

    /**
     * The start beat of the note.
     */
    public float getStart() {
        return start;
    }

    //----------------------------------
    // end
    //----------------------------------

    /**
     * Teh end beat of the note.
     */
    public float getEnd() {
        return end;
    }

    //----------------------------------
    // gate
    //----------------------------------

    /**
     * The gate time of the note, the length of time between the start and end.
     */
    public float getGate() {
        return MathUtils.rounded(end - start, 0.00001f);
    }

    //----------------------------------
    // velocity
    //----------------------------------

    /**
     * The velocity of the note (0..1).
     */
    public float getVelocity() {
        return velocity;
    }

    //----------------------------------
    // flags
    //----------------------------------

    /**
     * The flags bitmasked, (0 none), (1 silde), (2 accent).
     * 
     * @see NoteFlag#None
     * @see NoteFlag#Slide
     * @see NoteFlag#Accent
     */
    public int getFlags() {
        return flags;
    }

    /**
     * Returns whether the note is a slide note.
     * 
     * @see NoteFlag#Slide
     */
    public boolean isSlide() {
        return (flags & NoteFlag.Slide.getValue()) != 0;
    }

    /**
     * Returns whether the note is an accent note.
     * 
     * @see NoteFlag#Accent
     */
    public boolean isAccent() {
        return (flags & NoteFlag.Accent.getValue()) != 0;
    }

    //----------------------------------
    // selected
    //----------------------------------

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean value) {
        selected = value;
    }

    //----------------------------------
    // data
    //----------------------------------

    /**
     * Returns the note's abstract data if any.
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets the data for the note.
     * <p>
     * Clients of the note can use this property to tack on additional data that
     * relates to the note in the context of their application.
     * <p>
     * Works with serialization.
     * 
     * @param value The data associated with the note and client application.
     */
    public void setData(Object value) {
        data = value;
    }

    //----------------------------------
    // step
    //----------------------------------

    /**
     * Returns a calculated step based on the passed {@link Resolution}.
     * 
     * @param resolution The resolution of the calculation.
     */
    public final int getStep(Resolution resolution) {
        return Resolution.toStep(start, resolution);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    Note() {
    }

    /**
     * Creates a new note using the Caustic Core's serialization.
     * 
     * @param data The serialized note data for one note.
     * @see PatternSequencerMessage#QUERY_NOTE_DATA
     * @see NoteFlag
     */
    Note(String data) {
        String[] split = data.split(SPACE);
        this.start = Float.valueOf(split[0]);
        this.pitch = Float.valueOf(split[1]).intValue();
        this.velocity = Float.valueOf(split[2]);
        this.end = Float.valueOf(split[3]);
        this.flags = Float.valueOf(split[4]).intValue();
    }

    /**
     * Creates a new note using note values.
     * 
     * @param pitch The MIDI pitch.
     * @param start The start beat.
     * @param end The end beat.
     * @param velocity The velocity (0..1).
     * @param flags The bitmasked flags.
     * @see NoteFlag
     */
    Note(int pitch, float start, float end, float velocity, int flags) {
        update(pitch, start, end, velocity, flags);
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    /**
     * Updates thew note using new note values.
     * 
     * @param pitch The MIDI pitch.
     * @param start The start beat.
     * @param end The end beat.
     * @param velocity The velocity (0..1).
     * @param flags The bitmasked flags.
     * @see NoteFlag
     */
    public void update(int pitch, float start, float end, float velocity, int flags) {
        this.pitch = pitch;
        this.start = start;
        this.end = end;
        this.velocity = velocity;
        this.flags = flags;
    }

    /**
     * Serializes the note data into the Caustic Core note string.
     */
    public String getNoteData() {
        // [start] [pitch] [velocity] [end] [flags]
        final StringBuilder sb = new StringBuilder();
        sb.append(start);
        sb.append(SPACE);
        sb.append(pitch);
        sb.append(SPACE);
        sb.append(velocity);
        sb.append(SPACE);
        sb.append(end);
        sb.append(SPACE);
        sb.append(flags);
        return sb.toString();
    }

    @Override
    public String toString() {
        return "[Note(" + start + ", " + pitch + ", " + velocity + ", " + end + ", " + flags + ")]";
    }

    /**
     * A note flag, accent or slide.
     * 
     * @author Michael Schmalle
     */
    public enum NoteFlag {

        /**
         * No flags applied to the note.
         */
        None(0),

        /**
         * Whether the note slides to the next.
         */
        Slide(1),

        /**
         * Whether the note is accented.
         */
        Accent(2);

        private final int value;

        /**
         * Returns the integer value.
         */
        public final int getValue() {
            return value;
        }

        NoteFlag(int value) {
            this.value = value;
        }
    }
}
