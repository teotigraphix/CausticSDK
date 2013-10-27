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
import com.teotigraphix.caustk.core.osc.EffectRackMessage;
import com.teotigraphix.caustk.core.osc.SynthMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.effect.EffectType;

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

    public final MixerPreset getMixerPreset() {
        return mixerPreset;
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
        loadMachinePreset(factory);
        loadMixerPreset(factory);
        loadEffects(factory);
    }

    void loadMixerPreset(CaustkLibraryFactory factory) {
        mixerPreset = new MixerPreset(this);
        mixerPreset.restore();
    }

    void loadMachinePreset(CaustkLibraryFactory factory) throws IOException {
        final IRack rack = factory.getRack();

        // get the preset name if machine has a loaded preset
        String presetName = SynthMessage.QUERY_PRESET.queryString(rack, machine.getIndex());
        // create the preset file
        machinePreset = new MachinePreset(presetName, this);

        // get the bytes of the machine's preset and put them into the preset file
        machinePreset.restore();
    }

    void loadEffects(CaustkLibraryFactory factory) {
        final IRack rack = factory.getRack();

        final CaustkMachine machine = getMachine();
        final int machineIndex = machine.getIndex();

        EffectType effect0 = EffectType.fromInt((int)EffectRackMessage.TYPE.send(rack,
                machineIndex, 0));
        EffectType effect1 = EffectType.fromInt((int)EffectRackMessage.TYPE.send(rack,
                machineIndex, 1));

        if (effect0 != null) {
            CaustkEffect effect = factory.createEffect(0, effect0, this);
            putEffect(0, effect);
            effect.load(factory);
        }

        if (effect1 != null) {
            CaustkEffect effect = factory.createEffect(1, effect1, this);
            putEffect(1, effect);
            effect.load(factory);
        }
    }
}
