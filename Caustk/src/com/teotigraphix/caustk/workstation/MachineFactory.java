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

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.rack.effect.EffectType;
import com.teotigraphix.caustk.utils.PatternUtils;

public class MachineFactory extends CaustkSubFactoryBase {

    public MachineFactory() {
    }

    //----------------------------------
    // Machine
    //----------------------------------

    public Machine createMachine(ComponentInfo info, int machineIndex, MachineType machineType,
            String machineName) {
        Machine caustkMachine = new Machine(info, machineIndex, machineType, machineName);
        return caustkMachine;
    }

    public Machine createMachine(RackSet rackSet, int index, MachineType machineType,
            String machineName) {
        ComponentInfo info = getFactory().createInfo(ComponentType.Machine);
        Machine caustkMachine = new Machine(info, rackSet, index, machineType, machineName);
        return caustkMachine;
    }

    //----------------------------------
    // Patch
    //----------------------------------

    /**
     * Creates a {@link Patch} with {@link UUID} and {@link MachineType}.
     * 
     * @param toneType The {@link MachineType} of the
     */
    public Patch createPatch(ComponentInfo info, MachineType machineType) {
        Patch causticPatch = new Patch(info, machineType);
        return causticPatch;
    }

    /**
     * Creates a new {@link Patch}, assigns the {@link Machine}.
     * 
     * @param machine A {@link Machine} that does not exist in the native rack.
     */
    public Patch createPatch(Machine machine) {
        ComponentInfo info = getFactory().createInfo(ComponentType.Patch);
        Patch patch = new Patch(info, machine);
        return patch;
    }

    //----------------------------------
    // Phrase
    //----------------------------------

    public Phrase createPhrase(ComponentInfo info, MachineType machineType, int bankIndex,
            int patternIndex) {
        final int index = PatternUtils.getIndex(bankIndex, patternIndex);
        Phrase caustkPhrase = new Phrase(info, index, machineType);
        return caustkPhrase;
    }

    public Phrase createPhrase(Machine caustkMachine, int bankIndex, int patternIndex) {
        ComponentInfo info = getFactory().createInfo(ComponentType.Phrase);
        final int index = PatternUtils.getIndex(bankIndex, patternIndex);
        Phrase caustkPhrase = new Phrase(info, index, caustkMachine);
        return caustkPhrase;
    }

    //----------------------------------
    // Effect
    //----------------------------------

    public Effect createEffect(ComponentInfo info, EffectType effectType) {
        Effect caustkEffect = new Effect(info, effectType);
        // create the internal IEffect instance with slot and toneIndex set to -1
        // XXX Should Effect.create() be called here?
        try {
            caustkEffect.create(null);
        } catch (CausticException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return caustkEffect;
    }

    public Effect createEffect(int slot, EffectType effectType) {
        ComponentInfo info = getFactory().createInfo(ComponentType.Effect);
        Effect caustkEffect = new Effect(info, slot, effectType);
        return caustkEffect;
    }

    public Effect createEffect(int slot, EffectType effectType, Patch caustkPatch) {
        ComponentInfo info = getFactory().createInfo(ComponentType.Effect);
        Effect caustkEffect = new Effect(info, slot, effectType, caustkPatch);
        return caustkEffect;
    }
}
