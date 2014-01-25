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

import com.teotigraphix.caustk.node.NodeBase;

/**
 * A {@link TrackEntryNode} is an entry in a {@link TrackNode}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class TrackEntryNode extends NodeBase {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    private String patternName;

    private int startMeasure;

    private int endMeasure;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // pattern
    //----------------------------------

    /**
     * Returns the String representation of the pattern, <strong>A1</strong> for
     * example.
     */
    public String getPatternName() {
        return patternName;
    }

    /**
     * Returns the bank index (0..3) based on the {@link #getPatternName()}.
     */
    public int getBankIndex() {
        return PatternUtils.toBank(patternName);
    }

    /**
     * Returns the pattern index (0..15) based on the {@link #getPatternName()}.
     */
    public int getPatternIndex() {
        return PatternUtils.toPattern(patternName);
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
    public int getNumMeasureSpan() {
        return endMeasure - startMeasure;
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
        int numMeasures = PatternUtils.getNumMeasures(getRack(), index, getBankIndex(),
                getPatternIndex());
        return PatternUtils.getNumLoops(numMeasures, getNumMeasureSpan());
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public TrackEntryNode() {
    }

    public TrackEntryNode(int machineIndex, String patternName, int startMeasure, int endMeasure) {
        this.index = machineIndex;
        this.patternName = patternName;
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
}
