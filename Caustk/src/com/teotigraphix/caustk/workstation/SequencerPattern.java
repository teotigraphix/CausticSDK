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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.utils.PatternUtils;

// XXX TODO add to CaustkFactory

/**
 * @author Michael Schmalle
 */
public class SequencerPattern extends CaustkComponent {

    @Tag(100)
    private Machine machine;

    @Tag(101)
    private int bankIndex;

    @Tag(102)
    private int patternIndex;

    @Tag(103)
    private int startBeat;

    @Tag(104)
    private int endBeat;

    public Machine getMachine() {
        return machine;
    }

    public int getBankIndex() {
        return bankIndex;
    }

    public int getPatternIndex() {
        return patternIndex;
    }

    public int getStartBeat() {
        return startBeat;
    }

    public int getEndBeat() {
        return endBeat;
    }

    @Override
    public String getDefaultName() {
        return "TODO"; // machine[A01]
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    SequencerPattern() {
    }

    SequencerPattern(Machine machine) {
        this.machine = machine;
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
        }
    }

    public void setBankPattern(int bankIndex, int patternIndex) {
        this.bankIndex = bankIndex;
        this.patternIndex = patternIndex;
    }

    public void setLocation(int startBeat, int endBeat) {
        this.startBeat = startBeat;
        this.endBeat = endBeat;
    }

    @Override
    public String toString() {
        return "[CaustkSequencerPattern(" + machine.getMachineName() + ", " + startBeat + ":"
                + PatternUtils.toString(bankIndex, patternIndex) + ":" + endBeat + ")]";
    }

}
