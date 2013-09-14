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
import com.teotigraphix.caustk.gs.pattern.Part;
import com.teotigraphix.caustk.gs.pattern.Patch;
import com.teotigraphix.caustk.gs.pattern.Pattern;
import com.teotigraphix.caustk.gs.pattern.Phrase;

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

    protected Memory getCurrentMemory() {
        return getMachine().getMemoryManager().getSelectedMemoryBank();
    }

    public TemporaryMemory(GrooveMachine machine) {
        super(machine, Type.TEMPORARY);
    }

    @Override
    public Pattern copyPattern(int index) {
        pendingPattern = getMachine().getMemoryManager().getSelectedMemoryBank().copyPattern(index);
        return pendingPattern;
    }

    @Override
    public Phrase copyPhrase(Part part, int index) {
        Phrase phrase = getMachine().getMemoryManager().getSelectedMemoryBank()
                .copyPhrase(part, index);
        return phrase;
    }

    @Override
    public Patch copyPatch(Part part, int index) {
        Patch patch = getMachine().getMemoryManager().getSelectedMemoryBank()
                .copyPatch(part, index);
        return patch;
    }

    public void commit() {
        // commits all pending pattern and patch data to the 
        // Tone's sound and sequencer data on the CORE
        // XXX This is the final stop for an IMachine getting updated
        // bank and pattern changes here and patch audio/mixer/effect settings update

        // SoundSource soundSource = controller.getSoundSource();

        // the raw juice here, this should be the ONLY PLACE THIS SHIT is messed with
        //Collection<Tone> tones = soundSource.getTones();

        // what the fuck happens now?

        // - All Tones are created when the application starts in v1
        //   so we need either update the controls and pattern_sequencer
        //   of the machine, or find a new empty pattern and insert the
        //   new data instead of removing it all
        // - If we decide to just keep adding, then if the pattern data already
        //   exists, we just switch bank and pattern in the pattern_sequencer
        // 

        // XXX releasePattern(pattern);

        getMachine().getMachineSequencer().commit(pendingPattern);

        previousPattern = currentPattern;
        currentPattern = pendingPattern;

        pendingPattern = null;
    }
}
