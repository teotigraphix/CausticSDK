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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage.PatternSequencerControl;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.NodeBaseEvents.NodeEvent;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * The {@link PatternSequencerComponent} manages the {@link PatternNode}s in a
 * native machine's pattern sequencer.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class PatternSequencerComponent extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private Integer selectedBankIndex;

    @Tag(101)
    private Integer selectedPatternIndex;

    @Tag(102)
    private HashMap<String, PatternNode> patterns = new HashMap<String, PatternNode>();

    @Tag(103)
    private HashMap<Integer, Integer> bankMemory = new HashMap<Integer, Integer>();

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private int pendingBankIndex = -1;

    private int pendingPatternIndex = -1;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // patterns
    //----------------------------------

    /**
     * Returns the selected {@link PatternNode} based on the local bank and
     * pattern index.
     */
    public PatternNode getSelectedPattern() {
        return getPattern(selectedBankIndex, selectedPatternIndex);
    }

    PatternNode querySelectedPattern() {
        return getPattern(querySelectedBankIndex(), querySelectedPatternIndex());
    }

    /**
     * Sets the selected {@link PatternNode}, internally sets
     * {@link #setBankPatternIndex(int, int)}.
     * 
     * @param patternNode The current {@link PatternNode}.
     */
    public void setCurrentPattern(PatternNode patternNode) {
        setBankPatternIndex(patternNode.getBankIndex(), patternNode.getPatternIndex());
    }

    /**
     * Sets the current {@link PatternNode} using bank and index values.
     * 
     * @param bankIndex The bank index (0..3).
     * @param patternIndex The pattern index (0..15).
     * @see com.teotigraphix.caustk.node.machine.sequencer.PatternSequencerComponent.PatternSequencerNodeBankEvent
     * @see com.teotigraphix.caustk.node.machine.sequencer.PatternSequencerComponent.PatternSequencerNodePatternEvent
     */
    public void setBankPatternIndex(int bankIndex, int patternIndex) {
        selectedBankIndex = bankIndex;
        selectedPatternIndex = patternIndex;

        bankMemory.put(selectedBankIndex, selectedPatternIndex);

        PatternSequencerMessage.BANK.send(getRack(), getMachineIndex(), selectedBankIndex);
        PatternSequencerMessage.PATTERN.send(getRack(), getMachineIndex(), selectedPatternIndex);
        post(new PatternSequencerNodeBankEvent(this, PatternSequencerControl.Bank,
                selectedBankIndex));
        post(new PatternSequencerNodePatternEvent(this, PatternSequencerControl.Pattern,
                selectedPatternIndex));
    }

    public void setBankIndex(int bankIndex, boolean noEvent) {
        if (selectedBankIndex == bankIndex)
            return;
        selectedBankIndex = bankIndex;

        selectedPatternIndex = bankMemory.get(selectedBankIndex);
        PatternSequencerMessage.BANK.send(getRack(), getMachineIndex(), selectedBankIndex);
        PatternSequencerMessage.PATTERN.send(getRack(), getMachineIndex(), selectedPatternIndex);

        if (!noEvent) {
            post(new PatternSequencerNodeBankEvent(this, PatternSequencerControl.Bank,
                    selectedBankIndex));
            post(new PatternSequencerNodePatternEvent(this, PatternSequencerControl.Pattern,
                    selectedPatternIndex));
        }
    }

    public void setPatternIndex(int patternIndex, boolean noEvent) {
        if (selectedPatternIndex == patternIndex)
            return;
        selectedPatternIndex = patternIndex;
        bankMemory.put(selectedBankIndex, selectedPatternIndex);
        PatternSequencerMessage.PATTERN.send(getRack(), getMachineIndex(), selectedPatternIndex);
        if (!noEvent) {
            post(new PatternSequencerNodePatternEvent(this, PatternSequencerControl.Pattern,
                    selectedPatternIndex));
        }
    }

    /**
     * Returns the local bank index.
     */
    public Integer getSelectedBankIndex() {
        return selectedBankIndex;
    }

    /**
     * Returns the local pattern index.
     */
    public Integer getSelectedPatternIndex() {
        return selectedPatternIndex;
    }

    int querySelectedBankIndex() {
        return (int)PatternSequencerMessage.BANK.query(getRack(), getMachineIndex());
    }

    int querySelectedPatternIndex() {
        return (int)PatternSequencerMessage.PATTERN.query(getRack(), getMachineIndex());
    }

    /**
     * Returns an unmodifiable collection of existing {@link PatternNode}s with
     * pattern data.
     */
    public Collection<PatternNode> getPatterns() {
        return Collections.unmodifiableCollection(patterns.values());
    }

    /**
     * Returns whether this pattern sequencer contains the pattern by name eg.
     * 'A12'.
     * 
     * @param name The String parameter name ('A1', 'C14', etc).
     */
    public boolean hasPattern(String name) {
        return patterns.containsKey(name);
    }

    /**
     * Returns whether this pattern sequencer contains the pattern by bank and
     * pattern index.
     * 
     * @param bankIndex The bank index (0..3).
     * @param patternIndex The pattern index (0..15).
     */
    public boolean hasPattern(int bankIndex, int patternIndex) {
        return patterns.containsKey(PatternUtils.toString(bankIndex, patternIndex));
    }

    /**
     * Returns whether the sequencer contains notes within the bank index.
     * 
     * @param bankIndex The bank index.
     */
    public final boolean hasBankNotes(int bankIndex) {
        for (PatternNode patternNode : patterns.values()) {
            if (patternNode.getBankIndex() == bankIndex) {
                if (patternNode.hasNotes())
                    return true;
            }
        }
        return false;
    }

    /**
     * Returns whether the sequencer contains notes within the bank's pattern
     * index.
     * 
     * @param bankIndex The bank index.
     * @param patternIndex The pattern index.
     */
    public boolean hasPatternNotes(int bankIndex, int patternIndex) {
        if (!hasBankNotes(bankIndex))
            return false;
        for (PatternNode patternNode : patterns.values()) {
            if (patternNode.getBankIndex() == bankIndex
                    && patternNode.getPatternIndex() == patternIndex) {
                if (patternNode.hasNotes())
                    return true;
            }
        }
        return false;
    }

    /**
     * Attempts to find a {@link PatternNode} based on name, if the pattern is
     * not found, <code>null</code> is returned.
     * 
     * @param name The String parameter name ('A1', 'C14', etc).
     */
    public PatternNode findPattern(String name) {
        return patterns.get(name);
    }

    /**
     * Attempts to find a {@link PatternNode} based on bank and pattern index,
     * if the pattern is not found, <code>null</code> is returned.
     * 
     * @param bankIndex The bank index (0..3).
     * @param patternIndex The pattern index (0..15).
     */
    public PatternNode findPattern(int bankIndex, int patternIndex) {
        return patterns.get(PatternUtils.toString(bankIndex, patternIndex));
    }

    /**
     * Returns a {@link PatternNode} by name, if the {@link PatternNode} does
     * not exists, it will be created and added to the sequencer's
     * {@link #getPatterns()} collection.
     * 
     * @param name The String parameter name ('A1', 'C14', etc).
     */
    public PatternNode getPattern(String name) {
        PatternNode pattern = findPattern(name);
        if (pattern == null) {
            pattern = new PatternNode(getMachineNode(), name);
            patterns.put(name, pattern);
        }
        return pattern;
    }

    /**
     * Returns a {@link PatternNode} by bank and pattern index, if the
     * {@link PatternNode} does not exists, it will be created and added to the
     * sequencer's {@link #getPatterns()} collection.
     * 
     * @param bankIndex The bank index (0..3).
     * @param patternIndex The pattern index (0..15).
     */
    public PatternNode getPattern(int bankIndex, int patternIndex) {
        return getPattern(PatternUtils.toString(bankIndex, patternIndex));
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    protected PatternSequencerComponent() {
    }

    public PatternSequencerComponent(MachineNode machineNode) {
        super(machineNode);
        // init these here since the sequencer is being created explicitly
        // not through a restore, here we KNOW that bank and patter are 0 in native
        selectedBankIndex = 0;
        selectedPatternIndex = 0;

        bankMemory.put(0, 0);
        bankMemory.put(1, 0);
        bankMemory.put(2, 0);
        bankMemory.put(3, 0);
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Clears the pattern sequencer of all {@link PatternNode}s.
     * <p>
     * Each pattern's {@link PatternNode#clear()} is called which will remove
     * it's notes from the native pattern.
     * 
     * @param bankIndex The bank index (0..3).
     * @param patternIndex The pattern index (0..15).
     * @see com.teotigraphix.caustk.node.machine.sequencer.PatternSequencerComponent.PatternSequencerNodeClearEvent
     */
    public final PatternNode clearPattern(int bankIndex, int patternIndex) {
        final int index = PatternUtils.getIndex(bankIndex, patternIndex);
        PatternNode pattern = patterns.remove(index);
        if (pattern == null)
            throw new IllegalStateException("Pattern does not exists:"
                    + PatternUtils.toString(bankIndex, patternIndex));

        PatternSequencerMessage.CLEAR_PATTERN.send(getRack(), getMachineIndex(), patternIndex);

        // XXX remove all not data from the pattern sequencer
        // XXX remove all automation from the pattern sequencer
        pattern.clear();

        post(new PatternSequencerNodeClearEvent(this, PatternSequencerControl.ClearPattern));

        return pattern;
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
        String result = PatternSequencerMessage.QUERY_PATTERNS_WITH_DATA.queryString(getRack(),
                getMachineIndex());
        if (result == null)
            return;
        for (String name : result.split(" ")) {
            PatternNode patternNode = new PatternNode(getMachineNode(), name);
            patterns.put(patternNode.getName(), patternNode);
            PatternSequencerMessage.BANK.send(getRack(), getMachineIndex(),
                    patternNode.getBankIndex());
            PatternSequencerMessage.PATTERN.send(getRack(), getMachineIndex(),
                    patternNode.getPatternIndex());
            patternNode.restore();
        }
    }

    @Override
    protected void updateComponents() {
        int oBank = selectedBankIndex;
        int oPattern = selectedPatternIndex;
        // pushing patterns back into native machine pattern_sequencer
        for (PatternNode patternNode : patterns.values()) {
            // XXX TEMP until Rej gets this fixed
            int oldBank = getSelectedBankIndex();
            int oldPattern = getSelectedPatternIndex();
            setCurrentPattern(patternNode);
            patternNode.update();
            setBankPatternIndex(oldBank, oldPattern);
        }
        setBankPatternIndex(oBank, oPattern);
    }

    public enum EditMode {
        Note,

        Automation;
    }

    public enum GridQuantize {
        Whole,

        Half,

        Quater,

        Eighth,

        EightTriplet,

        Sixteenth,

        SixteenthTriplet,

        ThirtySecond,

        SixtyForth;
    }

    //--------------------------------------------------------------------------
    // Events
    //--------------------------------------------------------------------------

    /**
     * Base event for the {@link PatternSequencerComponent}.
     * 
     * @author Michael Schmalle
     * @since 1.0
     */
    public static class PatternSequencerNodeEvent extends NodeEvent {
        public PatternSequencerNodeEvent(NodeBase target, PatternSequencerControl control) {
            super(target, control);
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see PatternSequencerComponent#setBankPatternIndex(int, int)
     */
    public static class PatternSequencerNodeBankEvent extends NodeEvent {
        private int bank;

        public int getBank() {
            return bank;
        }

        public PatternSequencerNodeBankEvent(NodeBase target, PatternSequencerControl control,
                int bank) {
            super(target, control);
            this.bank = bank;
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see PatternSequencerComponent#setBankPatternIndex(int, int)
     */
    public static class PatternSequencerNodePatternEvent extends NodeEvent {
        private int pattern;

        public int getPattern() {
            return pattern;
        }

        public PatternSequencerNodePatternEvent(NodeBase target, PatternSequencerControl control,
                int pattern) {
            super(target, control);
            this.pattern = pattern;
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see PatternSequencerComponent#clearPattern(int, int)
     */
    public static class PatternSequencerNodeClearEvent extends NodeEvent {
        public PatternSequencerNodeClearEvent(NodeBase target, PatternSequencerControl control) {
            super(target, control);
        }
    }

    /**
     * Until OSC gets changed for pattern events, this method will store the
     * current bank and pattern for calls that need to change properties of the
     * non selected Pattern.
     * 
     * @param save true saves pending values, false resets the sequencer's
     *            current pattern.
     */
    public void store(boolean save, PatternNode patternNode) {
        int bank;
        int pattern;
        if (save) {
            pendingBankIndex = selectedBankIndex;
            pendingPatternIndex = selectedPatternIndex;
            bank = patternNode.getBankIndex();
            pattern = patternNode.getPatternIndex();
        } else {
            bank = pendingBankIndex;
            pattern = pendingPatternIndex;
            pendingBankIndex = -1;
            pendingPatternIndex = -1;
        }
        PatternSequencerMessage.BANK.send(getRack(), getMachineIndex(), bank);
        PatternSequencerMessage.PATTERN.send(getRack(), getMachineIndex(), pattern);
    }

    public void addPattern(PatternNode patternNode) {
        PatternNode newPattern = getPattern(patternNode.getName());
        newPattern.setNumMeasures(patternNode.getNumMeasures());
        //String data = patternNode.serialize();
        //newPattern.assignNoteData(data, true);
        PatternUtils.setNoteData(getRack(), getMachineIndex(), newPattern, new ArrayList<NoteNode>(
                patternNode.getNotes()));
    }

}
