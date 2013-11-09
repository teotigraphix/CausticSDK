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

import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;

/**
 * @author Michael Schmalle
 */
public class SongSet extends CaustkComponent {

    private transient PatternSet patternSet;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private UUID patternSetId;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        return null;
    }

    //----------------------------------
    // patternSetId
    //----------------------------------

    /**
     * Returns the {@link PatternSet} id that holds the {@link Pattern}s this
     * song references.
     */
    public UUID getPatternSetId() {
        return patternSetId;
    }

    //----------------------------------
    // patternSet
    //----------------------------------

    /**
     * Returns the active {@link PatternSet} instance for this song set.
     * <p>
     * The {@link PatternSet}'s id is the same as the {@link #getPatternSetId()}.
     */
    public PatternSet getPatternSet() {
        return patternSet;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    SongSet() {
    }

    SongSet(ComponentInfo info, UUID patternSetId) {
        setInfo(info);
        this.patternSetId = patternSetId;
    }

    SongSet(ComponentInfo info, PatternSet patternSet) {
        setInfo(info);
        this.patternSet = patternSet;
        this.patternSetId = patternSet.getInfo().getId();
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
