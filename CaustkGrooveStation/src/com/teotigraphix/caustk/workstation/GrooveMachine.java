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
public class GrooveMachine extends CaustkComponent {

    private transient Pattern pattern;

    private transient PatternBank patternBank;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private GrooveSet grooveSet;

    @Tag(101)
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
    // grooveSet
    //----------------------------------

    public GrooveSet getGrooveSet() {
        return grooveSet;
    }

    //----------------------------------
    // patternSetId
    //----------------------------------

    public UUID getPatternSetId() {
        return patternSetId;
    }

    //----------------------------------
    // patternSet
    //----------------------------------

    public PatternBank getPatternSet() {
        return patternBank;
    }

    public void setPatternSet(PatternBank value) {
        patternBank = value;
    }

    //----------------------------------
    // selectedPart
    //----------------------------------

    public Part getSelectedPart() {
        return pattern.getSelectedPart();
    }

    //----------------------------------
    // selectedPhrase
    //----------------------------------

    public Phrase getSelectedPhrase() {
        return getSelectedPart().getPhrase();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    GrooveMachine() {
    }

    GrooveMachine(ComponentInfo info) {
        setInfo(info);
    }

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        switch (phase) {
            case Create:
                break;
            case Load:
                break;
            case Update:
                break;
            case Restore:
                break;
            case Disconnect:
                break;
            case Connect:
                break;
        }
    }
}
