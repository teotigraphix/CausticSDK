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

package com.teotigraphix.caustk.live;

import java.io.IOException;
import java.util.UUID;

import com.teotigraphix.caustk.rack.effect.EffectType;
import com.teotigraphix.caustk.rack.effect.RackEffect;

public class PatchFactory extends CaustkSubFactoryBase {

    public PatchFactory() {
    }

    /**
     * Creates a {@link Patch} with {@link UUID} and {@link MachineType}.
     * 
     * @param toneType The {@link MachineType} of the
     */
    public Patch createPatch(ComponentInfo info, MachineType machineType) {
        Patch livePatch = new Patch(info, machineType);
        livePatch.create();
        return livePatch;
    }

    /**
     * Creates a new {@link Patch}, assigns the {@link Machine}.
     * 
     * @param machine A {@link Machine} that does not exist in the native rack.
     */
    public Patch createPatch(Machine machine) {
        ComponentInfo info = getFactory().createInfo(ComponentType.Patch);
        Patch livePatch = new Patch(info, machine);
        return livePatch;
    }

    /**
     * Activates the patch, creating the {@link MachinePreset} and
     * <p>
     * - Creates and assigns the bytes for the {@link MachinePreset}.
     * <p>
     * - Creates and assigns the {@link Patch} which will then create 0-2
     * {@link Effect}s. When the {@link Effect} is created, only the
     * {@link EffectType} is saved and slot index. The {@link RackEffect}
     * instance is not restored at this point.
     * 
     * @param livePatch
     * @throws IOException
     */
    public void _activatePatch(Patch caustkPatch) throws IOException {
        //CaustkLibraryUtils.assignAndUpdatePresetFile(caustkPatch.getMachine(), caustkPatch, rack);
        //CaustkLibraryUtils.assignEffects(caustkPatch.getMachine(), caustkPatch, rack);
    }

}
