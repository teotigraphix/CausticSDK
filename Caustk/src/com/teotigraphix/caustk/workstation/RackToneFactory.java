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

import java.util.Map;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.tone.BasslineTone;
import com.teotigraphix.caustk.rack.tone.BeatboxTone;
import com.teotigraphix.caustk.rack.tone.EightBitSynthTone;
import com.teotigraphix.caustk.rack.tone.FMSynthTone;
import com.teotigraphix.caustk.rack.tone.ModularTone;
import com.teotigraphix.caustk.rack.tone.OrganTone;
import com.teotigraphix.caustk.rack.tone.PCMSynthTone;
import com.teotigraphix.caustk.rack.tone.PadSynthTone;
import com.teotigraphix.caustk.rack.tone.RackTone;
import com.teotigraphix.caustk.rack.tone.SubSynthTone;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;
import com.teotigraphix.caustk.rack.tone.VocoderTone;

/**
 * @author Michael Schmalle
 */
public class RackToneFactory extends CaustkSubFactoryBase {

    private static final int NUM_MACHINES = 14;

    public RackToneFactory() {
    }

    public RackTone createRackTone(ToneDescriptor descriptor) throws CausticException {
        return createRackTone(descriptor.getMachineName(), descriptor.getMachineType(),
                descriptor.getMachineIndex());
    }

    public RackTone createRackTone(Machine machine, ToneDescriptor descriptor)
            throws CausticException {
        RackTone rackTone = createRackTone(descriptor.getMachineName(),
                descriptor.getMachineType(), descriptor.getMachineIndex());
        rackTone.setMachine(machine);
        return rackTone;
    }

    public RackTone createRackTone(String machineName, MachineType machineType, int machineIndex)
            throws CausticException {
        final IRack rack = getFactory().getRack();

        if (machineIndex > NUM_MACHINES - 1)
            throw new CausticException("Only 14 machines allowed in a rack");

        RackMessage.CREATE.send(rack, machineType.getType(), machineName, machineIndex);

        RackTone rackTone = null;
        switch (machineType) {
            case Bassline:
                rackTone = new BasslineTone(machineName, machineIndex);
                break;
            case Beatbox:
                rackTone = new BeatboxTone(machineName, machineIndex);
                break;
            case PCMSynth:
                rackTone = new PCMSynthTone(machineName, machineIndex);
                break;
            case SubSynth:
                rackTone = new SubSynthTone(machineName, machineIndex);
                break;
            case PadSynth:
                rackTone = new PadSynthTone(machineName, machineIndex);
                break;
            case Organ:
                rackTone = new OrganTone(machineName, machineIndex);
                break;
            case Vocoder:
                rackTone = new VocoderTone(machineName, machineIndex);
                break;
            case EightBitSynth:
                rackTone = new EightBitSynthTone(machineName, machineIndex);
                break;
            case Modular:
                rackTone = new ModularTone(machineName, machineIndex);
                break;
            case FMSynth:
                rackTone = new FMSynthTone(machineName, machineIndex);
                break;
            default:
                break;
        }

        return rackTone;
    }

    public static int nextIndex(Map<Integer, Machine> machines) {
        int index = 0;
        for (index = 0; index < NUM_MACHINES; index++) {
            if (!machines.containsKey(index))
                break;
        }
        return index;
    }

}
