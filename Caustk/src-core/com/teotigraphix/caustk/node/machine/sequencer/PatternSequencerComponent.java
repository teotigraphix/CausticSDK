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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage.PatternSequencerControl;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.NodeBaseEvents.NodeEvent;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link PatternSequencerComponent} manages the {@link PatternNode}s in a native
 * machine's pattern sequencer.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class PatternSequencerComponent extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private Integer currentBankIndex;

    private Integer currentPatternIndex;

    private HashMap<String, PatternNode> patterns = new HashMap<String, PatternNode>();

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // patterns
    //----------------------------------

    /**
     * Returns the current {@link PatternNode} based on the local bank and
     * pattern index.
     */
    public PatternNode getCurrentPattern() {
        return getPattern(currentBankIndex, currentPatternIndex);
    }

    PatternNode queryCurrentPattern() {
        return getPattern(queryCurrentBankIndex(), queryCurrentPatternIndex());
    }

    /**
     * Sets the current {@link PatternNode}, internally sets
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
     * @see PatternSequencerNodeBankEvent
     * @see PatternSequencerNodePatternEvent
     */
    public void setBankPatternIndex(int bankIndex, int patternIndex) {
        currentBankIndex = bankIndex;
        currentPatternIndex = patternIndex;
        PatternSequencerMessage.BANK.send(getRack(), machineIndex, currentBankIndex);
        PatternSequencerMessage.PATTERN.send(getRack(), machineIndex, currentPatternIndex);
        post(new PatternSequencerNodeBankEvent(this, PatternSequencerControl.Bank, currentBankIndex));
        post(new PatternSequencerNodePatternEvent(this, PatternSequencerControl.Pattern,
                currentPatternIndex));
    }

    /**
     * Returns the local bank index.
     */
    public Integer getCurrentBankIndex() {
        return currentBankIndex;
    }

    /**
     * Returns the local pattern index.
     */
    public Integer getCurrentPatternIndex() {
        return currentPatternIndex;
    }

    int queryCurrentBankIndex() {
        return (int)PatternSequencerMessage.BANK.query(getRack(), machineIndex);
    }

    int queryCurrentPatternIndex() {
        return (int)PatternSequencerMessage.PATTERN.query(getRack(), machineIndex);
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
            pattern = new PatternNode(name, machineIndex);
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
    public PatternSequencerComponent() {
    }

    public PatternSequencerComponent(int machineIndex) {
        this.machineIndex = machineIndex;
        // init these here since the sequencer is being created explicitly
        // not through a restore, here we KNOW that bank and patter are 0 in native
        currentBankIndex = 0;
        currentPatternIndex = 0;
    }

    public PatternSequencerComponent(MachineNode machineNode) {
        this(machineNode.getIndex());
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
     * @see PatternSequencerNodeClearEvent
     */
    public final PatternNode clearPattern(int bankIndex, int patternIndex) {
        final int index = PatternUtils.getIndex(bankIndex, patternIndex);
        PatternNode pattern = patterns.remove(index);
        if (pattern == null)
            throw new IllegalStateException("Pattern does not exists:"
                    + PatternUtils.toString(bankIndex, patternIndex));

        PatternSequencerMessage.CLEAR_PATTERN.send(getRack(), machineIndex, patternIndex);

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
                machineIndex);
        if (result == null)
            return;
        for (String name : result.split(" ")) {
            PatternNode patternNode = new PatternNode(name, machineIndex);
            patternNode.setMachineType(machineType);
            patterns.put(patternNode.getName(), patternNode);
            PatternSequencerMessage.BANK.send(getRack(), machineIndex, patternNode.getBankIndex());
            PatternSequencerMessage.PATTERN.send(getRack(), machineIndex,
                    patternNode.getPatternIndex());
            patternNode.restore();
        }
    }

    @Override
    protected void updateComponents() {
        int oBank = currentBankIndex;
        int oPattern = currentPatternIndex;
        // pushing patterns back into native machine pattern_sequencer
        for (PatternNode patternNode : patterns.values()) {
            // XXX TEMP until Rej gets this fixed
            int oldBank = getCurrentBankIndex();
            int oldPattern = getCurrentPatternIndex();
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
}
