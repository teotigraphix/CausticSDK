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

import com.teotigraphix.caustk.core.CausticError;
import com.teotigraphix.caustk.node.NodeBase;

/**
 * A {@link TrackEntryNode} is an entry in a {@link TrackComponent}.
 * <p>
 * The track entry is a read-only API, if start and end measures are adjusted,
 * the operation is handled in the {@link TrackComponent}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class TrackEntryNode extends NodeBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private int machineIndex = -1;

    private String pattern;

    private int numMeasures;

    private int startMeasure;

    private int endMeasure;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // machineIndex
    //----------------------------------

    /**
     * The owning {@link TrackComponent}'s machine index.
     */
    public int getMachineIndex() {
        return machineIndex;
    }

    /**
     * Sets the machine index.
     * 
     * @param machineIndex The machine index (0..13).
     */
    public void setMachineIndex(int machineIndex) {
        this.machineIndex = machineIndex;
    }

    //----------------------------------
    // pattern
    //----------------------------------

    /**
     * Returns the String representation of the pattern, <strong>A1</strong> for
     * example.
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Returns the bank index (0..3) based on the {@link #getPattern()}.
     */
    public int getBankIndex() {
        return PatternUtils.toBank(pattern);
    }

    /**
     * Returns the pattern index (0..15) based on the {@link #getPattern()}.
     */
    public int getPatternIndex() {
        return PatternUtils.toPattern(pattern);
    }

    //----------------------------------
    // numMeasures
    //----------------------------------

    public int getNumMeasures() {
        return numMeasures;
    }

    public void setNumMeasures(int numMeasures) {
        this.numMeasures = numMeasures;
    }

    //----------------------------------
    // startMeasure
    //----------------------------------

    /**
     * Returns the start measure of the pattern span.
     */
    public int getStartMeasure() {
        return startMeasure;
    }

    //----------------------------------
    // endMeasure
    //----------------------------------

    /**
     * Returns the end measure of the pattern span.
     */
    public int getEndMeasure() {
        return endMeasure;
    }

    /**
     * Returns the number of measures this entry spans, start to end.
     */
    public int getMeasureSpan() {
        return endMeasure - startMeasure;
    }

    /**
     * Returns the next measure from start plus measure span.
     */
    public int getNextMeasure() {
        return startMeasure + getMeasureSpan();
    }

    /**
     * Returns the number of loops this entry spans, can be fractional.
     * <p>
     * The calculation is based off the number of measures in the assigned
     * pattern in relation to the measure span of this entry.
     * <p>
     * An entry of an 8 measure pattern with a measure span of 9 will have a
     * loop value of <strong>1.125f</strong>.
     * 
     * @return
     */
    public float getNumLoops() {
        return PatternUtils.getNumLoops(numMeasures, getMeasureSpan());
    }

    /**
     * Returns <code>true</code> when the measure conflicts with this entry's
     * start measure or located within the start measure + measure span.
     * <p>
     * If the measure equals the end measure, this check will return
     * <code>false</code>, the measure is not contained within this entry.
     * 
     * @param measure The measure to test for containment within.
     */
    public boolean isContained(int measure) {
        return measure >= startMeasure && measure < endMeasure;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public TrackEntryNode() {
    }

    public TrackEntryNode(int machineIndex, String patternName, int numMeasures, int startMeasure,
            int endMeasure) {
        this.machineIndex = machineIndex;
        this.pattern = patternName;
        this.numMeasures = numMeasures;
        this.startMeasure = startMeasure;
        this.endMeasure = endMeasure;
    }

    //--------------------------------------------------------------------------
    // Internal :: Methods
    //--------------------------------------------------------------------------

    /**
     * Moves the clip to the new start measure, keeping the same length.
     * <p>
     * It's up to the client to adjust the native song sequencer value.
     * 
     * @param startMeasure The new start measure.
     */
    public void move(int startMeasure) {
        int length = getMeasureSpan();
        setPosition(startMeasure, startMeasure + length);
    }

    /**
     * Sets the new start and end measure positions.
     * <p>
     * Note: Do not use in normal move operations, this is used with dnd
     * operations where the entry does not represent a real track entry in the
     * {@link TrackComponent}.
     * 
     * @param startMeasure The new start measure.
     * @param endMeasure The new end measure.
     * @throws CausticError start and end measures are the same
     * @throws CausticError end measure is less than start measure
     */
    public void setPosition(int startMeasure, int endMeasure) {
        if (endMeasure == startMeasure)
            throw new CausticError("start and end mesaures are the same");
        if (endMeasure < startMeasure)
            throw new CausticError("end measure is less than start measure");
        this.startMeasure = startMeasure;
        this.endMeasure = endMeasure;
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
    protected void updateComponents() {
    }

    @Override
    protected void restoreComponents() {
    }

    @Override
    public String toString() {
        return "TrackEntryNode [machineIndex=" + machineIndex + ", pattern=" + pattern
                + ", startMeasure=" + startMeasure + ", endMeasure=" + endMeasure + "]";
    }

}
