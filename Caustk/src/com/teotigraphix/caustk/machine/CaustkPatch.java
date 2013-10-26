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

package com.teotigraphix.caustk.machine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.rack.IRack;

/*
 * - LivePatch can be copied to another LiveMachine of the same toneType
 *   in the LiveScene
 *   
 * - LivePatch can be copied to a LiveLibrary in the 
 *   /CausticLive/libraries/MyLib/patches/[toneType] directory
 * 
 * - 
 */

/**
 * @author Michael Schmalle
 */
public class CaustkPatch {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private UUID id;

    @Tag(1)
    private MachineType machineType;

    @Tag(2)
    private CaustkMachine machine;

    @Tag(3)
    private MachinePreset machinePreset;

    @Tag(4)
    private MixerPreset mixerPreset;

    @Tag(5)
    private Map<Integer, CaustkEffect> effects = new HashMap<Integer, CaustkEffect>(2);

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // id
    //----------------------------------

    /**
     * Returns the unique id for this {@link CaustkPatch}.
     * <p>
     * <strong>Assigned only at construction.</strong>
     */
    public UUID getId() {
        return id;
    }

    //----------------------------------
    // toneType
    //----------------------------------

    /**
     * Returns the type of {@link CaustkMachine} this patch can be assigned to.
     * <p>
     * <strong>Assigned only at construction.</strong>
     */
    public MachineType getMachineType() {
        return machineType;
    }

    //----------------------------------
    // machine
    //----------------------------------

    public CaustkMachine getMachine() {
        return machine;
    }

    //    void setMachine(CaustkMachine value) {
    //        machine = value;
    //    }

    //----------------------------------
    // machinePreset
    //----------------------------------

    public MachinePreset getMachinePreset() {
        return machinePreset;
    }

    void setMachinePreset(MachinePreset value) {
        machinePreset = value;
    }

    void putEffect(int slot, CaustkEffect effect) {
        if (slot != effect.getIndex())
            throw new IllegalStateException("Changing effect index is not implemented");
        //effect.setIndex(slot);
        effects.put(slot, effect);
    }

    CaustkEffect removeEffect(int slot) {
        CaustkEffect effect = effects.remove(slot);
        if (effect == null)
            return null;
        effect.setIndex(-1);
        return effect;
    }

    public CaustkEffect getEffect(int slot) {
        return effects.get(slot);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    CaustkPatch() {
    }

    CaustkPatch(UUID id, MachineType machineType) {
        this.id = id;
        this.machineType = machineType;
    }

    CaustkPatch(UUID id, CaustkMachine machine) {
        this.id = id;
        this.machine = machine;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void load(CaustkLibraryFactory factory) throws IOException, CausticException {
        final IRack rack = factory.getRack();

        CaustkLibraryUtils.assignAndUpdatePresetFile(machine, this, rack);
        CaustkLibraryUtils.assignEffects(machine, this, rack);
    }
}
