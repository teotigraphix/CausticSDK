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
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.rack.tone.RackTone;
import com.teotigraphix.caustk.utils.PatternUtils;

/**
 * @author Michael Schmalle
 */
public class PatternSet extends CaustkComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private RackSet rackSet;

    @Tag(101)
    Map<Integer, Part> parts = new TreeMap<Integer, Part>();

    @Tag(102)
    private Map<Integer, Pattern> patterns = new TreeMap<Integer, Pattern>();

    @Tag(103)
    private int selectedPatternIndex = 0;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        return getInfo().getName();
    }

    //---------------------------------- 
    // parts
    //----------------------------------

    public Collection<Part> getParts() {
        return parts.values();
    }

    //----------------------------------
    // patterns
    //----------------------------------

    public Collection<Pattern> getPatterns() {
        return patterns.values();
    }

    //----------------------------------
    // selectedPatternIndex
    //----------------------------------

    public Pattern getSelectedPattern() {
        return getPattern(selectedPatternIndex);
    }

    public int getSelectedPatternIndex() {
        return selectedPatternIndex;
    }

    /**
     * @param value
     * @see OnPatternSetChange
     * @see PatternSetChangeKind#SelectedPatternIndex
     */
    public void setSelectedPatternIndex(int value) {
        if (value == selectedPatternIndex)
            return;
        int oldIndex = selectedPatternIndex;
        selectedPatternIndex = value;
        rackSet.getComponentDispatcher().trigger(
                new OnPatternSetChange(this, PatternSetChangeKind.SelectedPatternIndex,
                        selectedPatternIndex, oldIndex));
    }

    //----------------------------------
    // rackSet
    //----------------------------------

    public RackSet getRackSet() {
        return rackSet;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    PatternSet() {
    }

    PatternSet(ComponentInfo info, RackSet rackSet) {
        setInfo(info);
        this.rackSet = rackSet;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Returns the {@link Part} at the specified index.
     * 
     * @param index The part index (0..13).
     */
    public Part getPart(int index) {
        return parts.get(index);
    }

    /**
     * Creates a new {@link Machine} and wraps it in a {@link Part} instance.
     * <p>
     * The {@link Part} is added to the {@link PatternSet}.
     * <p>
     * Calling this method will implicitly call
     * {@link Machine#create(ICaustkApplicationContext)} through the RackSet's
     * create() and create the {@link RackTone} in the native rack.
     * 
     * @param machineIndex The machine index.
     * @param machineType The {@link MachineType}.
     * @param machineName The native machine name.
     * @return A new {@link Part} instance added tot he {@link PatternSet}.
     * @throws CausticException
     */
    public Part createPart(int machineIndex, MachineType machineType, String machineName)
            throws CausticException {
        ICaustkFactory factory = rackSet.getFactory();
        ICaustkApplicationContext context = factory.createContext();
        // this adds the machine to the rackSet, calls create()
        Machine machine = rackSet.createMachine(machineIndex, machineName, machineType);
        ComponentInfo info = factory.createInfo(ComponentType.Part, machineName);
        Part part = factory.createPart(info, this, machine);
        part.create(context);
        parts.put(machineIndex, part);
        partAdd(part);
        rackSet.getComponentDispatcher().trigger(
                new OnPatternSetChange(this, PatternSetChangeKind.PartAdd, part));
        return part;
    }

    /**
     * Returns the {@link Pattern} at the linear index (0..62).
     * 
     * @param index The linear index.
     */
    public Pattern getPattern(int index) {
        return patterns.get(index);
    }

    /**
     * Returns the {@link Pattern} at the specific bank and pattern index.
     * 
     * @param bankIndex The bank index (0..3).
     * @param patternIndex The pattern index (0..15).
     */
    public Pattern getPattern(int bankIndex, int patternIndex) {
        return patterns.get(PatternUtils.getIndex(bankIndex, patternIndex));
    }

    /**
     * Returns the {@link Pattern} as the specific named position, e.g.
     * <code>A01</code> or <code>C13</code> etc.
     * 
     * @param patternName The named pattern position.
     */
    public Pattern getPattern(String patternName) {
        return patterns.get(PatternUtils.getIndex(PatternUtils.toBank(patternName),
                PatternUtils.toPattern(patternName)));
    }

    /**
     * Increments and returns the next pattern (0..63), when 64 is reached, the
     * index wraps around to 0.
     * 
     * @return The new selected {@link Pattern}.
     */
    public Pattern incrementPatternIndex() {
        int index = selectedPatternIndex + 1;
        if (index > 63)
            index = 0;
        setSelectedPatternIndex(index);
        return getSelectedPattern();
    }

    /**
     * Decrements and returns the next pattern (0..63), when 0 is reached, the
     * index wraps around to 63.
     * 
     * @return The new selected {@link Pattern}.
     */
    public Pattern decrementPatternIndex() {
        int index = selectedPatternIndex - 1;
        if (index < 0)
            index = 63;
        setSelectedPatternIndex(index);
        return getSelectedPattern();
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onSave() {
        super.onSave();

        rackSet.onSave();
    }

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        switch (phase) {
            case Connect:
                break;
            case Create:
                final ICaustkFactory factory = context.getFactory();
                // create all 64 Pattern instances for the initial set
                // no Parts are defined until createPart is called
                for (int i = 0; i < 64; i++) {
                    String name = PatternUtils.toString(i);
                    ComponentInfo info = factory.createInfo(ComponentType.Pattern, name);
                    Pattern pattern = factory.createPattern(info, this, i);
                    addPattern(pattern);
                }
                break;

            case Load:
                break;
            case Update:
                // send BLANKRACK message
                context.getRack().setRackSet(rackSet);
                for (Part part : parts.values()) {
                    part.update(context);
                }
                for (Pattern pattern : patterns.values()) {
                    pattern.update(context);
                }
                break;
            case Restore:
                break;
            case Disconnect:
                break;
        }
    }

    void addPattern(Pattern pattern) {
        patterns.put(pattern.getIndex(), pattern);
        patternAdd(pattern);
    }

    private void patternAdd(Pattern pattern) {
        // TODO Auto-generated method stub

    }

    private void partAdd(Part part) {
        // TODO Auto-generated method stub

    }

    //--------------------------------------------------------------------------
    // Event API
    //--------------------------------------------------------------------------

    public enum PatternSetChangeKind {
        PatternAdd,

        PatternRemove,

        PatternReplace,

        PartAdd,

        PartRemove,

        PartReplace,

        SelectedPatternIndex,
    }

    /**
     * @author Michael Schmalle
     * @see RackSet#getComponentDispatcher()
     */
    public static class OnPatternSetChange {

        private PatternSet patternSet;

        private PatternSetChangeKind kind;

        private int index;

        private int oldIndex;

        private Part part;

        public PatternSet getPatternSet() {
            return patternSet;
        }

        public PatternSetChangeKind getKind() {
            return kind;
        }

        /**
         * @see PatternSetChangeKind#SelectedPatternIndex
         */
        public int getIndex() {
            return index;
        }

        /**
         * @see PatternSetChangeKind#SelectedPatternIndex
         */
        public int getOldIndex() {
            return oldIndex;
        }

        /**
         * @see PatternSetChangeKind#PartAdd
         */
        public Part getPart() {
            return part;
        }

        public OnPatternSetChange(PatternSet patternSet, PatternSetChangeKind kind) {
            this.patternSet = patternSet;
            this.kind = kind;
        }

        public OnPatternSetChange(PatternSet patternSet, PatternSetChangeKind kind, int index,
                int oldIndex) {
            this.patternSet = patternSet;
            this.kind = kind;
            this.index = index;
            this.oldIndex = oldIndex;
        }

        public OnPatternSetChange(PatternSet patternSet, PatternSetChangeKind kind, Part part) {
            this.patternSet = patternSet;
            this.kind = kind;
            this.part = part;
        }
    }

}
