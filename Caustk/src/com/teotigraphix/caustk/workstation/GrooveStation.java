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

import com.teotigraphix.caustk.core.CausticException;

/**
 * The toplevel model when using {@link GrooveSet}, {@link GrooveBox},
 * {@link PatternBank} and {@link SongBank}.
 * <p>
 * This class is not serialized, it loads and unloads {@link GrooveSet}s which
 * intern hold {@link GrooveBox} configurations.
 * <p>
 * All {@link GrooveBox} creation and loading occurs through this class's API.
 * 
 * @author Michael Schmalle
 */
public class GrooveStation {

    private ICaustkFactory factory;

    private GrooveSet grooveSet;

    public GrooveSet getGrooveSet() {
        return grooveSet;
    }

    public void setGrooveSet(GrooveSet value) {
        grooveSet = value;
        factory.getRack().setRackSet(grooveSet.getRackSet());
    }

    public GrooveStation(ICaustkFactory factory) {
        this.factory = factory;
    }

    public GrooveSet createGrooveSet(String name) {
        // Create the internal RackSet
        RackSet rackSet = factory.createRackSet();
        rackSet.setInternal();

        // Create empty GrooveSet that will hold GrooveMachines
        ComponentInfo info = factory.createInfo(ComponentType.GrooveSet, name);
        GrooveSet grooveSet = factory.createGrooveSet(info, rackSet);
        return grooveSet;
    }

    public GrooveBox createGrooveBox(GrooveBoxType grooveBoxType) throws CausticException {
        if (grooveSet == null)
            throw new IllegalStateException("grooveSet is null");

        // Create the GrooveBox
        GrooveBox grooveBox = factory.createGrooveBox(grooveSet, grooveBoxType);
        grooveSet.addGrooveBox(grooveBox);

        // Create the PatternBank for the grooveBox
        PatternBank patternBank = factory.createPatternBank(grooveBox);
        grooveBox.setPatternBank(patternBank);
        patternBank.create(grooveSet.getRackSet().getFactory().createContext());

        return grooveBox;
    }
}
