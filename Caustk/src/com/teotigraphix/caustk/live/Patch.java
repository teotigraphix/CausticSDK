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
import com.teotigraphix.caustk.controller.ICaustkFactory;
import com.teotigraphix.caustk.controller.IRackContext;
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

    // private IRack rack;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private ComponentInfo info;

    @Tag(1)
    private MachineType machineType;

    @Tag(2)
    private Machine machine;

    @Tag(3)
    private MachinePreset machinePreset;

    @Tag(4)
    private MixerPreset mixerPreset;

    @Tag(5)
    private Map<Integer, Effect> effects = new HashMap<Integer, Effect>(2);

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // rack
    //----------------------------------

    //    @Override
    //    public IRack getRack() {
    //        return rack;
    //    }
    //
    //    @Override
    //    public void setRack(IRack value) {
    //        rack = value;
    //    }

    //----------------------------------
    // info
    //----------------------------------

    @Override
    public ComponentInfo getInfo() {
        return info;
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
        return machineType;
    }

    //----------------------------------
    // machine
    //----------------------------------

    public Machine getMachine() {
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

    /**
     * Adds and returns an effect without sending a message to the core.
     * <p>
     * Using this method will call {@link Effect#create()} to create the
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
        effect.create();
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
        // 
        effect.setPatch(this);
        effect.update();
    }

    Effect removeEffect(int slot) {
        Effect effect = effects.remove(slot);
        if (effect == null)
            return null;
        effect.setIndex(-1);
        return effect;
    }

    public Effect getEffect(int slot) {
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
    Patch() {
    }

    Patch(ComponentInfo info, MachineType machineType) {
        this.info = info;
        this.machineType = machineType;
    }

    Patch(ComponentInfo info, Machine machine) {
        this.info = info;
        this.machine = machine;
        this.machineType = machine.getMachineType();
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void create() {
        machinePreset = new MachinePreset(null, this);
        mixerPreset = new MixerPreset(this);
    }

    @Override
    public void update() {
        //machinePreset.setRack(rack);
        //mixerPreset.setRack(rack);

        machinePreset.update();
        mixerPreset.update();

        for (Effect caustkEffect : effects.values()) {
            //caustkEffect.setRack(rack);
            caustkEffect.update();
        }
    }

    @Override
    public void load(IRackContext context) throws CausticException {
        try {
            loadMachinePreset(context);
        } catch (IOException e) {
            throw new CausticException(e);
        }
        loadMixerPreset(context);
        loadEffects(context);
    }

    void loadMixerPreset(IRackContext context) throws CausticException {
        mixerPreset = new MixerPreset(this);
        //mixerPreset.restore();
        mixerPreset.load(context);
    }

    void loadMachinePreset(IRackContext context) throws IOException {
        final IRack rack = context.getRack();

        // get the preset name if machine has a loaded preset
        String presetName = SynthMessage.QUERY_PRESET.queryString(rack, machine.getIndex());
        // create the preset file
        machinePreset = new MachinePreset(presetName, this);

        // get the bytes of the machine's preset and put them into the preset file
        //machinePreset.restore();
        machinePreset.load(context);
    }

    void loadEffects(IRackContext context) throws CausticException {
        final IRack rack = context.getRack();

        final Machine machine = getMachine();
        final int machineIndex = machine.getIndex();

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
        // TODO Auto-generated method stub

    }

}
