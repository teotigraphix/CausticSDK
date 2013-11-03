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

package com.teotigraphix.caustk.rack.tone;

import com.teotigraphix.caustk.live.Machine;
import com.teotigraphix.caustk.live.MachineType;
import com.teotigraphix.caustk.rack.tone.components.MixerChannel;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent;
import com.teotigraphix.caustk.rack.tone.components.SynthComponent;
import com.teotigraphix.caustk.rack.tone.components.VolumeComponent;
import com.teotigraphix.caustk.rack.tone.components.organ.LeslieComponent;

/**
 * The tone impl for the native Organ machine.
 * 
 * @author Michael Schmalle
 */
public class OrganTone extends RackTone {

    public VolumeComponent getVolume() {
        return getComponent(VolumeComponent.class);
    }

    public LeslieComponent getLeslie() {
        return getComponent(LeslieComponent.class);
    }

    OrganTone() {
    }

    public OrganTone(Machine machine, String machineName, int machineIndex) {
        super(machine, MachineType.Organ, machineName, machineIndex);
    }

    @Override
    public void create() {
        addComponent(MixerChannel.class, new MixerChannel());
        addComponent(SynthComponent.class, new SynthComponent());
        addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());
        addComponent(VolumeComponent.class, new VolumeComponent());
        addComponent(LeslieComponent.class, new LeslieComponent());
    }
}
