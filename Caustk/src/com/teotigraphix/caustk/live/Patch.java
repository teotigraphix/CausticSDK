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
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.controller.IRackSerializer;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.EffectRackMessage;
import com.teotigraphix.caustk.core.osc.SynthMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.effect.EffectType;
import com.teotigraphix.caustk.rack.effect.RackEffect;

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
public class Patch implements ICaustkComponent, IRackSerializer {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private ComponentInfo info;

    @Tag(1)
    private MachineType type;

    @Tag(2)
    private Machine machine;

    @Tag(3)
    private MachinePreset preset;

    @Tag(4)
    private MachineMixer mixer;

    @Tag(5)
    private Map<Integer, Effect> effects = new HashMap<Integer, Effect>(2);

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // info
    //----------------------------------

    @Override
    public ComponentInfo getInfo() {
        return info;
    }

    @Override
    public String getDefaultName() {
        String name = type.getType() + ":TODO";
        if (preset.getName() != null)
            name = preset.getName();
        return name;
    }

    //----------------------------------
    // toneType
    //----------------------------------

    /**
     * Returns the type of {@link Machine} this patch can be assigned to.
     * <p>
     * <strong>Assigned only at construction.</strong>
     */
    public MachineType getMachineType() {
        return type;
    }

    //----------------------------------
    // machine
    //----------------------------------

    public Machine getMachine() {
        return machine;
    }

    //----------------------------------
    // machinePreset
    //----------------------------------

    public MachinePreset getPreset() {
        return preset;
    }

    //----------------------------------
    // machineMixer
    //----------------------------------

    public MachineMixer getMixer() {
        return mixer;
    }

    /**
     * Adds and returns an effect without sending a message to the core.
     * <p>
     * Using this method will call
     * {@link Effect#create(ICaustkApplicationContext)} to create the
     * {@link RackEffect} instance in the {@link Effect}.
     * 
     * @param factory The library factory.
     * @param slot The effect slot.
     * @param effectType The {@link EffectType}.
     */
    public Effect createEffect(ICaustkFactory factory, int slot, EffectType effectType) {
        Effect effect = factory.createEffect(0, effectType, this);
        // since we are creating the effect from the outside, we create
        // the internal effect here
        effect.create(null);
        effects.put(slot, effect);
        return effect;
    }

    void putEffect(int slot, Effect effect) {
        if (slot != effect.getIndex())
            throw new IllegalStateException("Changing effect index is not implemented");
        //effect.setIndex(slot);
        effects.put(slot, effect);
    }

    public void replaceEffect(int slot, Effect effect) {
        //        // 
        //        effect.setPatch(this);
        //        effect.update();
    }

    //
    //    Effect removeEffect(int slot) {
    //        Effect effect = effects.remove(slot);
    //        if (effect == null)
    //            return null;
    //        effect.setIndex(-1);
    //        return effect;
    //    }

    public Effect getEffect(int slot) {
        return effects.get(slot);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    Patch() {
    }

    Patch(ComponentInfo info, MachineType machineType) {
        this.info = info;
        this.type = machineType;
    }

    Patch(ComponentInfo info, Machine machine) {
        this.info = info;
        this.machine = machine;
        this.type = machine.getMachineType();
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void create(ICaustkApplicationContext context) {
        preset = new MachinePreset(null, this);
        mixer = new MachineMixer(this);
        try {
            preset.create(context);
            mixer.create(context);
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(ICaustkApplicationContext context) {
        preset.update(context);
        mixer.update(context);
        for (Effect caustkEffect : effects.values()) {
            caustkEffect.update(context);
        }
    }

    @Override
    public void load(ICaustkApplicationContext context) throws CausticException {
        try {
            loadMachinePreset(context);
            loadMachineMixer(context);
        } catch (IOException e) {
            throw new CausticException(e);
        }
        loadEffects(context);
    }

    void loadMachinePreset(ICaustkApplicationContext context) throws IOException {
        final IRack rack = context.getRack();
        // get the preset name if machine has a loaded preset
        String presetName = SynthMessage.QUERY_PRESET.queryString(rack, machine.getMachineIndex());
        // create the preset file
        preset = new MachinePreset(presetName, this);

        // get the bytes of the machine's preset and put them into the preset file
        //machinePreset.restore();
        preset.load(context);
    }

    private void loadMachineMixer(ICaustkApplicationContext context) throws CausticException {
        mixer.load(context);
    }

    void loadEffects(ICaustkApplicationContext context) throws CausticException {
        final IRack rack = context.getRack();

        final Machine machine = getMachine();
        final int machineIndex = machine.getMachineIndex();

        EffectType effect0 = EffectType.fromInt((int)EffectRackMessage.TYPE.send(rack,
                machineIndex, 0));
        EffectType effect1 = EffectType.fromInt((int)EffectRackMessage.TYPE.send(rack,
                machineIndex, 1));

        if (effect0 != null) {
            Effect effect = context.getFactory().createEffect(0, effect0, this);
            putEffect(0, effect);
            effect.load(context);
        }

        if (effect1 != null) {
            Effect effect = context.getFactory().createEffect(1, effect1, this);
            putEffect(1, effect);
            effect.load(context);
        }
    }

    @Override
    public void restore() {
    }

    @Override
    public void onLoad() {
        preset.onLoad();
        mixer.onLoad();
    }

    @Override
    public void onSave() {
        preset.onSave();
        mixer.onSave();
    }

    @Override
    public String toString() {
        return "[Patch(" + type.getType() + ", '" + getDefaultName() + "')]";
    }
}
