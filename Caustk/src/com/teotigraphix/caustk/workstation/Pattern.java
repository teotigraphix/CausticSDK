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
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.utils.PatternUtils;

/**
 * @author Michael Schmalle
 */
public class Pattern extends CaustkComponent {

    private transient Song song;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private PatternSet patternSet;

    // the index the PatternSet assigns
    @Tag(101)
    private int index;

    @Tag(102)
    Map<Integer, Part> parts = new HashMap<Integer, Part>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        return PatternUtils.toString(getBankIndex(), getPatternIndex());
    }

    //---------------------------------- 
    // index
    //----------------------------------

    /**
     * The index of the pattern within the owning {@link PatternSet} (0..63).
     */
    public int getIndex() {
        return index;
    }

    //---------------------------------- 
    // parts
    //----------------------------------

    public Collection<Part> getParts() {
        return parts.values();
    }

    //----------------------------------
    // bankIndex
    //----------------------------------

    /**
     * Returns the native pattern_sequencer bank index calculated from the
     * {@link #getIndex()}.
     */
    public int getBankIndex() {
        return PatternUtils.getBank(index);
    }

    //----------------------------------
    // patternIndex
    //----------------------------------

    /**
     * Returns the native pattern_sequencer pattern index calculated from the
     * {@link #getIndex()}.
     */
    public int getPatternIndex() {
        return PatternUtils.getPattern(index);
    }

    //----------------------------------
    // song
    //----------------------------------

    public Song getSong() {
        return song;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    Pattern() {
    }

    Pattern(ComponentInfo info, PatternSet patternSet, int index) {
        setInfo(info);
        this.patternSet = patternSet;
        this.index = index;
    }

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        switch (phase) {
            case Connect:
                break;
            case Create:
                break;
            case Disconnect:
                break;
            case Load:
                break;
            case Restore:
                break;
            case Update:
                break;
        }
    }
}
