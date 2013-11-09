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
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.rack.IRack;

/**
 * @author Michael Schmalle
 */
public class MasterSequencer extends CaustkComponent {

    private transient IRack rack;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private RackSet rackSet;

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        return null;
    }

    //----------------------------------
    // rackSet
    //----------------------------------

    public RackSet getRackSet() {
        return rackSet;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    MasterSequencer() {
    }

    MasterSequencer(RackSet rackSet) {
        this.rackSet = rackSet;
    }

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        switch (phase) {
            case Create:
                break;

            case Load:
                rack = context.getRack();
                restore();
                break;

            case Update:
                rack = context.getRack();
                for (Machine caustkMachine : rackSet.getMachines()) {
                    updateMachine(caustkMachine);
                }
                break;

            case Restore:
                String patterns = rack.getSystemSequencer().getPatterns();
                if (patterns != null) {
                    loadPatterns(patterns);
                }
                break;

            case Connect:
                break;

            case Disconnect:
                break;
        }
    }

    private void loadPatterns(String patterns) {
        // [machin_index] [start_measure] [bank] [pattern] [end_measure]
        String[] split = patterns.split("\\|");
        for (String group : split) {
            String[] parts = group.split(" ");
            int index = Integer.valueOf(parts[0]);
            int start = Integer.valueOf(parts[1]);
            int bank = Integer.valueOf(parts[2]);
            int pattern = Integer.valueOf(parts[3]);
            int end = Integer.valueOf(parts[4]);

            Machine machine = rackSet.getMachine(index);
            machine.addPattern(bank, pattern, start, end);
        }
    }

    void updateMachine(Machine caustkMachine) {
        for (SequencerPattern caustkSequencerPattern : caustkMachine.getPatterns().values()) {
            SequencerMessage.PATTERN_EVENT.send(rack, caustkMachine.getMachineIndex(),
                    caustkSequencerPattern.getStartBeat(), caustkSequencerPattern.getBankIndex(),
                    caustkSequencerPattern.getPatternIndex(), caustkSequencerPattern.getEndBeat());
        }
    }

}
