////////////////////////////////////////////////////////////////////////////////
//Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0 
//
//Unless required by applicable law or agreed to in writing, software 
//distributed under the License is distributed on an "AS IS" BASIS, 
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and 
//limitations under the License
//
//Author: Michael Schmalle, Principal Architect
//mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.node.machine.sequencer;

import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.utils.MathUtils;

// XXX mutators should only be called from PatternNode

/**
 * The {@link NoteNode} wraps a single note trigger in a {@link PatternNode}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class NoteNode extends NodeBase {

    private static final String SPACE = " ";

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    transient private int pitch;

    transient private float start;

    transient private float end;

    transient private float velocity;

    transient private int flags;

    @SuppressWarnings("unused")
    private String noteData;

    private boolean selected;

    private Object data; // has to contain primitive value objects

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
     * The end beat of the note.
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
    public final int getStep(NoteResolution resolution) {
        return NoteResolution.toStep(start, resolution);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public NoteNode() {
    }

    /**
     * Creates a new note using the Caustic Core's serialization.
     * 
     * @param data The serialized note data for one note.
     * @see PatternSequencerMessage#QUERY_NOTE_DATA
     * @see NoteFlag
     */
    public NoteNode(String data) {
        String[] split = data.split(SPACE);
        this.start = Float.valueOf(split[0]);
        this.pitch = Float.valueOf(split[1]).intValue();
        this.velocity = Float.valueOf(split[2]);
        this.end = Float.valueOf(split[3]);
        this.flags = Float.valueOf(split[4]).intValue();
        onNoteDataInvalid();
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
    public NoteNode(int pitch, float start, float end, float velocity, int flags) {
        set(pitch, start, end, velocity, flags);
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
    public void set(int pitch, float start, float end, float velocity, int flags) {
        this.pitch = pitch;
        this.start = start;
        this.end = end;
        this.velocity = velocity;
        this.flags = flags;
        onNoteDataInvalid();
    }

    /**
     * Serializes the note data into the Caustic Core note string.
     * <p>
     * Defined as "<code>[start] [pitch] [velocity] [end] [flags]</code>".
     */
    public String toNoteData() {
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
    protected void createComponents() {
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void updateComponents() {
    }

    @Override
    protected void restoreComponents() {
    }

    private void onNoteDataInvalid() {
        noteData = toNoteData();
    }

    @Override
    public String toString() {
        return "[Note(" + start + ", " + pitch + ", " + velocity + ", " + end + ", " + flags + ")]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(end);
        result = prime * result + flags;
        result = prime * result + pitch;
        result = prime * result + (selected ? 1231 : 1237);
        result = prime * result + Float.floatToIntBits(start);
        result = prime * result + Float.floatToIntBits(velocity);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NoteNode other = (NoteNode)obj;
        if (Float.floatToIntBits(end) != Float.floatToIntBits(other.end))
            return false;
        if (flags != other.flags)
            return false;
        if (pitch != other.pitch)
            return false;
        if (selected != other.selected)
            return false;
        if (Float.floatToIntBits(start) != Float.floatToIntBits(other.start))
            return false;
        if (Float.floatToIntBits(velocity) != Float.floatToIntBits(other.velocity))
            return false;
        return true;
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

    /**
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum NoteResolution {

        /**
         * A whole note.
         */
        WHOLE(1f), // 1

        /**
         * A half note.
         */
        HALF(0.5f), // 2

        /**
         * A quarter note.
         */
        QUATER(0.25f), // 4

        /**
         * An eighth note.
         */
        EIGHTH(0.125f), // 8

        /**
         * A sixteenth note.
         */
        SIXTEENTH(0.0625f), // 16

        /**
         * A thirtysecond note.
         */
        THIRTYSECOND(0.03125f), // 32

        /**
         * A sixtyfourth note.
         */
        SIXTYFOURTH(0.015625f); // 1 / 0.015625f = 64

        NoteResolution(float value) {
            mValue = value;
        }

        private float mValue;

        /**
         * Returns the amount of steps in a measure for the given phrase
         * resolution.
         * 
         * @param resolution The note resolution.
         * @return The number of steps in a measure for the given phrase
         *         resolution.
         */
        public final static int toSteps(NoteResolution resolution) {
            return (int)(1 / resolution.getValue());
        }

        public float getValue() {
            return mValue;
        }

        private static int beatsInMeasure = 4;

        public static int toStep(float beat, NoteResolution resolution) {
            // (beat(5) / 0.0625) / 4
            return (int)(beat / resolution.getValue()) / beatsInMeasure;
        }

        public static float toBeat(int step, NoteResolution resolution) {
            return (step * resolution.getValue()) * beatsInMeasure;
        }
    }
}
