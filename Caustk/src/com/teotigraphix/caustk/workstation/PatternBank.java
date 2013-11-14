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
import com.teotigraphix.caustk.utils.PatternUtils;

/**
 * @author Michael Schmalle
 */
public class PatternBank extends CaustkComponent {

    private transient RackSet rackSet;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(101)
    private String patternTypeId;

    @Tag(102)
    private GrooveBox grooveBox;

    @Tag(103)
    private Map<Integer, Pattern> patterns = new TreeMap<Integer, Pattern>();

    @Tag(104)
    private int selectedIndex = 0;

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
    // rackSet
    //----------------------------------

    /**
     * The {@link RackSet} is a transient instance held on the
     * {@link PatternBank}. Each type of "pattern" will have it's own pattern
     * bank.
     * <p>
     * Suppose you have a machine that holds 2 parts, 2 bassline tones. That
     * machine will hold its own pattern bank, and the bank will operate on the
     * current {@link RackSet}.
     */
    public RackSet getRackSet() {
        return rackSet;
    }

    //----------------------------------
    // patternTypeId
    //----------------------------------

    /**
     * Returns the type of {@link Pattern} this bank holds.
     * <p>
     * A {@link Pattern} can only hold one type of {@link Part} structure, this
     * type is used when loading {@link PatternBank}s into top level machines,
     * if the pattern type does not match the top level machine's pattern type,
     * the {@link PatternBank} is incompatible and will not be able to be used
     * to load patterns into the top level machine.
     * <p>
     * The pattern type is a 4 character unique identifier used when the naming
     * the {@link Part}s initially created for the {@link Pattern}.
     */
    public String getPatternTypeId() {
        return patternTypeId;
    }

    //---------------------------------- 
    // grooveBox
    //----------------------------------

    public GrooveBox getGrooveBox() {
        return grooveBox;
    }

    //---------------------------------- 
    // parts
    //----------------------------------

    public Collection<Part> getParts() {
        return grooveBox.getParts();
    }

    //----------------------------------
    // patterns
    //----------------------------------

    public Collection<Pattern> getPatterns() {
        return patterns.values();
    }

    //----------------------------------
    // selectedIndex
    //----------------------------------

    public Pattern getSelectedPattern() {
        return getPattern(selectedIndex);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * @param value
     * @see OnPatternBankChange
     * @see PatternBankChangeKind#SelectedIndex
     */
    public void setSelectedIndex(int value) {
        if (value == selectedIndex)
            return;
        int oldIndex = selectedIndex;
        selectedIndex = value;
        rackSet.getComponentDispatcher().trigger(
                new OnPatternBankChange(this, PatternBankChangeKind.SelectedIndex, selectedIndex,
                        oldIndex));
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    PatternBank() {
    }

    PatternBank(ComponentInfo info, GrooveBox grooveBox) {
        setInfo(info);
        this.grooveBox = grooveBox;
        this.patternTypeId = grooveBox.getPatternTypeId();
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
        return grooveBox.getPart(index);
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
     * @see #setSelectedIndex(int)
     */
    public Pattern incrementIndex() {
        int index = selectedIndex + 1;
        if (index > 63)
            index = 0;
        setSelectedIndex(index);
        return getSelectedPattern();
    }

    /**
     * Decrements and returns the next pattern (0..63), when 0 is reached, the
     * index wraps around to 63.
     * 
     * @return The new selected {@link Pattern}.
     * @see #setSelectedIndex(int)
     */
    public Pattern decrementIndex() {
        int index = selectedIndex - 1;
        if (index < 0)
            index = 63;
        setSelectedIndex(index);
        return getSelectedPattern();
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void onLoad(ICaustkApplicationContext context) {
        super.onLoad(context);
    }

    @Override
    public void onSave(ICaustkApplicationContext context) {
        super.onSave(context);

        rackSet.onSave(context);
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
                //                for (Part part : parts.values()) {
                //                    part.update(context);
                //                }
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

    //--------------------------------------------------------------------------
    // Event API
    //--------------------------------------------------------------------------

    public enum PatternBankChangeKind {
        PatternAdd,

        PatternRemove,

        PatternReplace,

        SelectedIndex,
    }

    /**
     * @author Michael Schmalle
     * @see RackSet#getComponentDispatcher()
     */
    public static class OnPatternBankChange {

        private PatternBank patternBank;

        private PatternBankChangeKind kind;

        private int index;

        private int oldIndex;

        public PatternBank getPatternBank() {
            return patternBank;
        }

        public PatternBankChangeKind getKind() {
            return kind;
        }

        /**
         * @see PatternBankChangeKind#SelectedIndex
         */
        public int getIndex() {
            return index;
        }

        /**
         * @see PatternBankChangeKind#SelectedIndex
         */
        public int getOldIndex() {
            return oldIndex;
        }

        public OnPatternBankChange(PatternBank patternBank, PatternBankChangeKind kind) {
            this.patternBank = patternBank;
            this.kind = kind;
        }

        public OnPatternBankChange(PatternBank patternBank, PatternBankChangeKind kind, int index,
                int oldIndex) {
            this.patternBank = patternBank;
            this.kind = kind;
            this.index = index;
            this.oldIndex = oldIndex;
        }
    }
}
