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

package com.teotigraphix.caustic.internal.part;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.internal.rack.RackFactory;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.machine.MachineType;

public class PartRackFactory extends RackFactory {

    @Override
    public IMachine create(String machineId, MachineType machineType) throws CausticException {
        IMachine machine = null;

        if (machineType == MachineType.SUBSYNTH)
            machine = new SubSynthTone(machineId);
        else if (machineType == MachineType.PCMSYNTH)
            machine = new PCMSynthTone(machineId);
        else if (machineType == MachineType.BEATBOX)
            machine = new BeatboxTone(machineId);
        else if (machineType == MachineType.BASSLINE)
            machine = new BasslineTone(machineId);

        return machine;
    }
}
