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

package com.teotigraphix.caustk.gs.memory;

import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.machine.part.MachineSequencer;
import com.teotigraphix.caustk.gs.machine.part.sound.MachinePatch;
import com.teotigraphix.caustk.gs.pattern.Part;
import com.teotigraphix.caustk.gs.pattern.Pattern;
import com.teotigraphix.caustk.workstation.Phrase;

public class TemporaryMemory extends Memory {

    @Override
    public int getPatternCount() {
        return 0;
    }

    //----------------------------------
    // previousPattern
    //----------------------------------

    /**
     * last played Pattern after switch
     */
    private Pattern previousPattern;

    public Pattern getPreviousPattern() {
        return previousPattern;
    }

    //----------------------------------
    // pendingPattern
    //----------------------------------

    /**
     * Queued pattern by user waiting for the end of the last measure
     */
    private Pattern pendingPattern;

    public Pattern getPendingPattern() {
        return pendingPattern;
    }

    //----------------------------------
    // currentPattern
    //----------------------------------

    /**
     * the playing Pattern data in the A03 pattern sequencer slot of the
     * existing parts
     */
    private Pattern currentPattern;

    public Pattern getCurrentPattern() {
        return currentPattern;
    }

    //----------------------------------
    // nextPattern
    //----------------------------------

    /**
     * A queued Pattern AFTER the pending Patter. This is still theoretical.
     */
    private Pattern nextPattern;

    public Pattern getNextPattern() {
        return nextPattern;
    }

    //----------------------------------
    // 
    //----------------------------------

    /**
     * Returns the currently selected {@link MemoryBank} in the machine's
     * {@link MemoryManager}.
     */
    protected final Memory getCurrentMemory() {
        return getMachine().getMemory().getSelectedMemoryBank();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public TemporaryMemory(GrooveMachine machine) {
        super(machine, Type.TEMPORARY);
    }

    //--------------------------------------------------------------------------
    // Method :: API
    //--------------------------------------------------------------------------

    @Override
    public Pattern copyPattern(int index) {
        pendingPattern = getCurrentMemory().copyPattern(index);
        return pendingPattern;
    }

    @Override
    public Phrase copyPhrase(Part part, int index) {
        Phrase phrase = getCurrentMemory().copyPhrase(part, index);
        return phrase;
    }

    @Override
    public MachinePatch copyPatch(Part part, int index) {
        MachinePatch machinePatch = getCurrentMemory().copyPatch(part, index);
        return machinePatch;
    }

    /**
     * @see MachineSequencer#playNextPattern()
     */
    public void commit() {

        getMachine().getSequencer().commit(pendingPattern);

        previousPattern = currentPattern;
        currentPattern = pendingPattern;

        pendingPattern = null;
    }
}
