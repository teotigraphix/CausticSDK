////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.node.machine.sequencer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.machine.sequencer.NoteNode.NoteFlag;

/**
 * The {@link PatternNode} holds a bank and pattern position in a
 * {@link PatternSequencerNode}.
 * <p>
 * Measure length can be adjusted, notes added/removed, shuffle set.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class PatternNode extends NodeBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private Integer machineIndex;

    private String name;

    private int numMeasures = 1;

    private ShuffleMode shuffleMode = ShuffleMode.Default;

    private float shuffleAmount;

    private List<NoteNode> notes = new ArrayList<NoteNode>();

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // machineIndex
    //----------------------------------

    /**
     * Returns the machine's index for which this pattern is located
     * within(0..13).
     * <p>
     * Could be null of the pattern is a value object with no parent pattern
     * sequencer.
     */
    public int getMachineIndex() {
        return machineIndex;
    }

    //----------------------------------
    // name
    //----------------------------------

    /**
     * Returns the pattern name such as <strong>A1</strong> or
     * <strong>C14</strong>.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the index of the pattern based on its bank and pattern (0..63).
     */
    public final int getIndex() {
        return PatternUtils.getIndex(getBankIndex(), getPatternIndex());
    }

    /**
     * Returns the bank index calculated from the {@link #getName()} (0..3).
     */
    public final int getBankIndex() {
        return PatternUtils.toBank(name);
    }

    /**
     * Returns the pattern index calculated from the {@link #getName()} (0..15).
     */
    public final int getPatternIndex() {
        return PatternUtils.toPattern(name);
    }

    //----------------------------------
    // numMeasures
    //----------------------------------

    /**
     * Returns the number of measures held in the pattern (1, 2, 4, 8).
     * 
     * @see PatternSequencerMessage#NUM_MEASURES
     */
    public int getNumMeasures() {
        return numMeasures;
    }

    int getNumMeasures(boolean restore) {
        return (int)PatternSequencerMessage.NUM_MEASURES.query(getRack(), machineIndex);
    }

    /**
     * Update the number of measures.
     * <p>
     * Updates the native pattern.
     * 
     * @param numMeasures Number of measures (1, 2, 4, 8).
     * @see PatternSequencerMessage#NUM_MEASURES
     */
    public void setNumMeasures(Integer numMeasures) {
        if (numMeasures == this.numMeasures)
            return;
        if (numMeasures < 1 || numMeasures > 8)
            throw newRangeException("num_measures", "1,2,4,8", numMeasures);
        this.numMeasures = numMeasures;
        // XXX impl new PatternSq
        PatternSequencerMessage.NUM_MEASURES.send(getRack(), machineIndex, numMeasures);
    }

    //----------------------------------
    // notes
    //----------------------------------

    /**
     * Returns an unmodifiable collection of {@link NoteNode}s.
     */
    public Collection<NoteNode> getNotes() {
        return Collections.unmodifiableCollection(notes);
    }

    //----------------------------------
    // notes
    //----------------------------------

    /**
     * Return a {@link NoteNode} at a specified beat and pitch.
     * <p>
     * To be considered a match, the start and pitch must match and existing
     * {@link NoteNode} found in this pattern.
     * 
     * @param beat The start beat.
     * @param pitch The MIDI pitch.
     * @return A {@link NoteNode} or <code>null</code> if not found.
     */
    public NoteNode getNote(float beat, int pitch) {
        for (NoteNode note : notes) {
            if (note.getStart() == beat && note.getPitch() == pitch)
                return note;
        }
        return null;
    }

    /**
     * Returns a collection of {@link NoteNode}s that have the start beat equal
     * to the beat argument.
     * 
     * @param beat The start beat to match.
     * @return A collection of {@link NoteNode}s or empty collection.
     */
    public Collection<NoteNode> getNotes(float beat) {
        Collection<NoteNode> result = new ArrayList<NoteNode>();
        for (NoteNode note : notes) {
            // XXX Might be a Float precision error condition
            if (beat == note.getStart())
                result.add(note);
        }
        return result;
    }

    /**
     * Returns a collection of {@link NoteNode}s based on a time selection.
     * 
     * @param startBeat The start beat of the selection.
     * @param endBeat The end beat of the selection.
     * @return A collection of {@link NoteNode}s or empty collection.
     */
    public Collection<NoteNode> getNotes(float startBeat, float endBeat) {
        List<NoteNode> result = new ArrayList<NoteNode>();
        for (NoteNode note : notes) {
            if (startBeat >= note.getStart() && endBeat <= note.getEnd())
                result.add(note);
        }
        return result;
    }

    /**
     * Returns a collection of {@link NoteNode}s based on a lasso selection.
     * 
     * @param startBeat The start beat of the selection.
     * @param endBeat The end beat of the selection.
     * @return A collection of {@link NoteNode}s or empty collection.
     */
    public Collection<NoteNode> getNotes(float startBeat, float endBeat, int startPitch,
            int endPitch) {
        List<NoteNode> result = new ArrayList<NoteNode>();
        for (NoteNode note : notes) {
            if (startBeat >= note.getStart() && endBeat <= note.getEnd()) {
                if (note.getPitch() >= startPitch && note.getPitch() <= endPitch) {
                    result.add(note);
                }
            }
        }
        return result;
    }

    //----------------------------------
    // shuffleMode
    //----------------------------------

    /**
     * @see PatternSequencerMessage#SHUFFLE_MODE
     */
    public ShuffleMode getShuffleMode() {
        return shuffleMode;
    }

    public ShuffleMode getShuffleMode(boolean restore) {
        return ShuffleMode.fromInt((int)PatternSequencerMessage.SHUFFLE_MODE.query(getRack(),
                getMachineIndex()));
    }

    /**
     * @param shuffleMode {@link ShuffleMode}
     * @see PatternSequencerMessage#SHUFFLE_MODE
     */
    public void setShuffleMode(ShuffleMode shuffleMode) {
        if (shuffleMode == this.shuffleMode)
            return;
        this.shuffleMode = shuffleMode;
        PatternSequencerMessage.SHUFFLE_MODE.send(getRack(), getMachineIndex(),
                shuffleMode.getValue());
    }

    //----------------------------------
    // shuffleAmount
    //----------------------------------

    /**
     * @see PatternSequencerMessage#SHUFFLE_AMOUNT
     */
    public float getShuffleAmount() {
        return shuffleAmount;
    }

    public float getShuffleAmount(boolean restore) {
        return PatternSequencerMessage.SHUFFLE_AMOUNT.query(getRack(), getMachineIndex());
    }

    /**
     * @param shuffleAmount (0..1.0)
     * @see PatternSequencerMessage#SHUFFLE_AMOUNT
     */
    public void setShuffleAmount(float shuffleAmount) {
        if (shuffleAmount == this.shuffleAmount)
            return;
        this.shuffleAmount = shuffleAmount;
        if (shuffleAmount < 0f || shuffleAmount > 1f)
            throw newRangeException("shuffle_amount", "0..1", shuffleAmount);
        PatternSequencerMessage.SHUFFLE_AMOUNT.send(getRack(), getMachineIndex(), shuffleAmount);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public PatternNode() {
    }

    /**
     * Creates a value object pattern with no parent pattern sequencer.
     * 
     * @param name The name of the pattern.
     */
    public PatternNode(String name) {
        this.name = name;
    }

    /**
     * Creates a value object pattern with no parent pattern sequencer.
     * 
     * @param bankIndex The bank index (0..3).
     * @param patternIndex The pattern index (0..15).
     */
    public PatternNode(int bankIndex, int patternIndex) {
        this.name = PatternUtils.toString(bankIndex, patternIndex);
    }

    /**
     * Creates a standard pattern with machine/pattern sequencer parent.
     * 
     * @param name The name of the pattern.
     * @param machineIndex The machine's rack index.
     */
    public PatternNode(String name, int machineIndex) {
        this(name);
        this.machineIndex = machineIndex;
    }

    /**
     * Creates a value object pattern with no parent pattern sequencer.
     * 
     * @param bankIndex The bank index (0..3).
     * @param patternIndex The pattern index (0..15).
     * @param machineIndex The machine's rack index.
     */
    public PatternNode(int bankIndex, int patternIndex, int machineIndex) {
        this.name = PatternUtils.toString(bankIndex, patternIndex);
        this.machineIndex = machineIndex;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Creates and adds a {@link NoteNode} to the pattern.
     * 
     * @param startBeat The start beat.
     * @param pitch The MIDI pitch.
     * @param endBeat The end beat.
     * @param velocity The velocity (0..1)
     * @param flags The note flags; {@link NoteFlag}.
     * @return A new {@link NoteNode} added to the pattern.
     * @throws IllegalStateException Note exists at beat
     */
    public NoteNode createNote(float startBeat, int pitch, float endBeat, float velocity, int flags) {
        NoteNode note = getNote(startBeat, pitch);
        if (note != null)
            throw new IllegalStateException("Note exists at beat:" + startBeat + " and pitch:"
                    + pitch);

        // Message: /caustic/[machine_index]/pattern_sequencer/note_data [start] [pitch] [velocity] [end] [flags] 
        PatternSequencerMessage.NOTE_DATA.send(getRack(), getMachineIndex(), startBeat, pitch,
                velocity, endBeat, flags);
        note = new NoteNode(pitch, startBeat, endBeat, velocity, flags);
        addNote(note);
        return note;
    }

    /**
     * Destroys a {@link NoteNode} based on the start beat and pitch.
     * 
     * @param startBeat The start beat.
     * @param pitch The MIDI pitch
     * @return The destroyed {@link NoteNode}, <code>null</code> if no note was
     *         destroyed.
     */
    public NoteNode destroyNote(float startBeat, int pitch) {
        NoteNode noteNode = getNote(startBeat, pitch);
        if (noteNode == null)
            return null;
        destroyNote(noteNode);
        return noteNode;
    }

    /**
     * Clears all notes from the pattern.
     * 
     * @throws IllegalStateException Pattern was not cleared
     */
    public void clear() {
        List<NoteNode> copy = new ArrayList<NoteNode>(notes);
        for (NoteNode noteNode : copy) {
            destroyNote(noteNode);
        }
        if (notes.size() != 0)
            throw new IllegalStateException("Pattern was not cleared");
    }

    //    public void setPosition(/*TOD*/) {
    //
    //    }
    //
    //    public void setVelocity(NoteNode note, float velocity) {
    //
    //    }
    //
    //    public void snapNote(NoteNode note, GridQuantize gridQuantize) {
    //
    //    }
    //
    //    // XX is end inclusive or exclusive?
    //    public List<NoteNode> selectNotes(float start, float end) {
    //        List<NoteNode> result = new ArrayList<NoteNode>();
    //        // XX loop through all notes within range and set their selected property
    //        return result;
    //    }
    //
    //    private List<NoteNode> copyBuffer;
    //
    //    public List<NoteNode> getCopyBuffer() {
    //        return copyBuffer;
    //    }
    //
    //    // notes would most likly be the selection range
    //    public void copyNotes(List<NoteNode> notes) {
    //
    //    }
    //
    //    public void pasteNotes(List<NoteNode> notes) {
    //        // XX pastes the copy buffer into
    //    }

    @Override
    public String toString() {
        return "[PatternNode(" + name + ")]";
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    private NoteNode addNote(NoteNode note) {
        notes.add(note);
        return note;
    }

    private NoteNode removeNote(NoteNode note) {
        notes.remove(note);
        return note;
    }

    private NoteNode destroyNote(NoteNode noteNode) {
        // Message: /caustic/[machine_index]/pattern_sequencer/note_data [start] [pitch] 
        PatternSequencerMessage.NOTE_DATA_REMOVE.send(getRack(), getMachineIndex(),
                noteNode.getStart(), noteNode.getPitch());
        removeNote(noteNode);
        return noteNode;
    }

    private void assignNoteData(String data) {
        // push the notes into the machines sequencer
        String[] notes = data.split("\\|");
        for (String noteData : notes) {
            String[] split = noteData.split(" ");

            float startBeat = Float.valueOf(split[0]);
            int pitch = Float.valueOf(split[1]).intValue();
            float velocity = Float.valueOf(split[2]);
            float endBeat = Float.valueOf(split[3]);
            int flags = Float.valueOf(split[4]).intValue();
            NoteNode note = new NoteNode(pitch, startBeat, endBeat, velocity, flags);
            addNote(note);
        }
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void restoreComponents() {
        // create NoteNodes for each note
        String result = PatternSequencerMessage.QUERY_NOTE_DATA
                .queryString(getRack(), machineIndex);
        if (result == null)
            return;

        setNumMeasures(getNumMeasures(true));
        assignNoteData(result);
        setShuffleMode(getShuffleMode(true));
        setShuffleAmount(getShuffleAmount(true));
    }

    @Override
    protected void updateComponents() {
        // when this call happens, the parent PatternNode has already
        // set the correct bank/pattern
        PatternSequencerMessage.NUM_MEASURES.send(getRack(), machineIndex, numMeasures);
        // we already have the NoteNodes, just need to update the machine's pattern_sequencer
        for (NoteNode noteNode : notes) {
            PatternSequencerMessage.NOTE_DATA.send(getRack(), machineIndex, noteNode.getStart(),
                    noteNode.getPitch(), noteNode.getVelocity(), noteNode.getEnd(),
                    noteNode.getFlags());
        }
        PatternSequencerMessage.SHUFFLE_MODE.send(getRack(), machineIndex, shuffleMode.getValue());
        PatternSequencerMessage.SHUFFLE_AMOUNT.send(getRack(), machineIndex, shuffleAmount);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((notes == null) ? 0 : notes.hashCode());
        result = prime * result + numMeasures;
        result = prime * result + Float.floatToIntBits(shuffleAmount);
        result = prime * result + ((shuffleMode == null) ? 0 : shuffleMode.hashCode());
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
        PatternNode other = (PatternNode)obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (notes == null) {
            if (other.notes != null)
                return false;
        } else if (!notes.equals(other.notes))
            return false;
        if (numMeasures != other.numMeasures)
            return false;
        if (Float.floatToIntBits(shuffleAmount) != Float.floatToIntBits(other.shuffleAmount))
            return false;
        if (shuffleMode != other.shuffleMode)
            return false;
        return true;
    }

    public enum ShuffleMode {

        Default(0),

        Eigth(1),

        Sixteenth(2);

        private final int value;

        ShuffleMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ShuffleMode fromInt(int value) {
            for (ShuffleMode mode : values()) {
                if (mode.getValue() == value)
                    return mode;
            }
            return null;
        }
    }
}
