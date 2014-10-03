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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode.Resolution;
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

    @Tag(50)
    private int pitch;

    @Tag(51)
    private float start;

    @Tag(52)
    private float end;

    @Tag(53)
    private float velocity;

    @Tag(54)
    private int flags;

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
     * Updates the note using new note values.
     * 
     * @param pitch The MIDI pitch.
     * @param start The start beat.
     * @param end The end beat.
     * @param velocity The velocity (0..1).
     * @param flags The bitmasked flags.
     * @see NoteFlag
     */
    void set(int pitch, float start, float end, float velocity, int flags) {
        this.pitch = pitch;
        this.start = start;
        this.end = end;
        this.velocity = velocity;
        this.flags = flags;
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
        result = prime * result + (isSelected() ? 1231 : 1237);
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
        if (isSelected() != other.isSelected())
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
}
