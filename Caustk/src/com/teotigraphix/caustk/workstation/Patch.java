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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.EffectRackMessage;
import com.teotigraphix.caustk.core.osc.SynthMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.effect.EffectType;

/**
 * @author Michael Schmalle
 */
public class Patch extends CaustkComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private Machine machine;

    @Tag(101)
    private MachineType machineType;

    @Tag(102)
    private MachinePreset machinePreset;

    @Tag(103)
    private MachineMixer machineMixer;

    @Tag(104)
    private Map<Integer, Effect> effects = new HashMap<Integer, Effect>(2);

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // defaultName
    //----------------------------------

    @Override
    public String getDefaultName() {
        String name = machineType.getType() + "_" + getMachine().getMachineName();
        if (machinePreset.getName() != null) {
            name = machineType.getType() + "_" + machinePreset.getName();
            try {
                // XXX temp until it's figured out in MachinePreset
                UUID.fromString(name.replace(ComponentType.Patch.getExtension(), ""));
                name = getMachineType().getType() + "_" + getMachine().getMachineName();
            } catch (IllegalArgumentException e) {
            }
        }
        return name;
    }

    //----------------------------------
    // machine
    //----------------------------------

    public Machine getMachine() {
        return machine;
    }

    void setMachine(Machine machine) {
        this.machine = machine;
    }

    //----------------------------------
    // machineType
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
    // machinePreset
    //----------------------------------

    public MachinePreset getPreset() {
        return machinePreset;
    }

    //----------------------------------
    // machineMixer
    //----------------------------------

    public MachineMixer getMixer() {
        return machineMixer;
    }

    //----------------------------------
    // effect
    //----------------------------------

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
        setInfo(info);
        this.machineType = machineType;
    }

    Patch(ComponentInfo info, Machine machine) {
        setInfo(info);
        this.machine = machine;
        this.machineType = machine.getMachineType();
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void addEffect(Effect effect) {
        addEffect(effect.getSlot(), effect);
    }

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        switch (phase) {
            case Create:
                machinePreset = new MachinePreset(null, this);
                machineMixer = new MachineMixer(this);
                try {
                    machinePreset.create(context);
                    machineMixer.create(context);
                } catch (CausticException e) {
                    e.printStackTrace();
                }
                break;

            case Load:
                create(context);

                try {
                    loadMachinePreset(context);
                    loadMachineMixer(context);
                } catch (IOException e) {
                    throw new CausticException(e);
                }
                loadEffects(context);
                break;

            case Update:
                machinePreset.update(context);
                machineMixer.update(context);
                for (Effect caustkEffect : effects.values()) {
                    caustkEffect.update(context);
                }
                break;

            case Restore:
                machinePreset.restore();
                machineMixer.restore();
                for (Effect caustkEffect : effects.values()) {
                    caustkEffect.restore();
                }
                break;

            case Connect:
                break;

            case Disconnect:
                machine = null;
                break;
        }
    }

    @Override
    public void onLoad(ICaustkApplicationContext context) {
        machinePreset.onLoad(context);
        machineMixer.onLoad(context);
    }

    @Override
    public void onSave(ICaustkApplicationContext context) {
        machinePreset.onSave(context);
        machineMixer.onSave(context);
    }

    //--------------------------------------------------------------------------
    // Private API :: Methods
    //--------------------------------------------------------------------------

    void loadMachinePreset(ICaustkApplicationContext context) throws IOException {
        final IRack rack = context.getRack();
        // get the preset name if machine has a loaded preset
        String presetName = SynthMessage.QUERY_PRESET.queryString(rack, machine.getMachineIndex());
        // create the preset file
        machinePreset = new MachinePreset(presetName, this);

        // get the bytes of the machine's preset and put them into the preset file
        //machinePreset.restore();
        machinePreset.load(context);
    }

    private void loadMachineMixer(ICaustkApplicationContext context) throws CausticException {
        machineMixer.load(context);
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
            addEffect(0, effect);
            effect.load(context);
        }

        if (effect1 != null) {
            Effect effect = context.getFactory().createEffect(1, effect1, this);
            addEffect(1, effect);
            effect.load(context);
        }
    }

    void addEffect(int slot, Effect effect) {
        if (slot != effect.getSlot())
            throw new IllegalStateException("Changing effect index is not implemented");
        effects.put(slot, effect);
    }

    @Override
    public String toString() {
        return "[Patch(" + machineType.getType() + ", '" + getDefaultName() + "')]";
    }
}
