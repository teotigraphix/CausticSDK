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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.controller.IRackSerializer;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.EffectRackMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.effect.EffectFactory;
import com.teotigraphix.caustk.rack.effect.EffectType;
import com.teotigraphix.caustk.rack.effect.RackEffect;

/**
 * @author Michael Schmalle
 */
public class Effect implements IRackSerializer, ICaustkComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private ComponentInfo info;

    @Tag(1)
    private Patch patch;

    @Tag(2)
    private int slot = -1;

    @Tag(3)
    private EffectType effectType;

    @Tag(4)
    private RackEffect rackEffect;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // rack
    //----------------------------------

    public IRack getRack() {
        return patch.getMachine().getRack();
    }

    //----------------------------------
    // info
    //----------------------------------

    @Override
    public final ComponentInfo getInfo() {
        return info;
    }

    @Override
    public String getDefaultName() {
        String name = "";
        if (info.getFile() != null)
            name = info.getRelativePath();
        else if (patch != null)
            name = patch.getMachine().getMachineName();
        return name;
    }

    //----------------------------------
    // effect
    //----------------------------------

    /**
     * Returns the parent {@link Patch}.
     * <p>
     * May be <code>null</code> if the effect is an unattached component in a
     * {@link Library}.
     */
    public Patch getPatch() {
        return patch;
    }

    void setPatch(Patch value) {
        patch = value;
    }

    //----------------------------------
    // slot
    //----------------------------------

    /**
     * The slot this effect is held in the {@link Patch}, -1 if not assigned to
     * a {@link Patch}.
     */
    public int getSlot() {
        return slot;
    }

    //----------------------------------
    // effectType
    //----------------------------------

    /**
     * Returns the {@link EffectType} of this effect.
     * <p>
     * Set in the constructor only.
     */
    public EffectType getEffectType() {
        return effectType;
    }

    //----------------------------------
    // effect
    //----------------------------------

    /**
     * Returns the native rack effect.
     */
    public RackEffect getEffect() {
        return rackEffect;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    Effect() {
    }

    Effect(ComponentInfo info, EffectType effectType) {
        this.info = info;
        this.effectType = effectType;
    }

    Effect(ComponentInfo info, int slot, EffectType effectType) {
        this.info = info;
        this.slot = slot;
        this.effectType = effectType;
    }

    Effect(ComponentInfo info, int slot, EffectType effectType, Patch patch) {
        this.info = info;
        this.slot = slot;
        this.effectType = effectType;
        this.patch = patch;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void create(ICaustkApplicationContext context) {
        rackEffect = createEffect(-1, -1);
    }

    @Override
    public void load(ICaustkApplicationContext context) throws CausticException {
        rackEffect = createEffect(slot, patch.getMachine().getMachineIndex());
        rackEffect.setEffect(this);
        rackEffect.load(context);
    }

    @Override
    public void update(ICaustkApplicationContext context) {
        EffectRackMessage.CREATE.send(getRack(), getPatch().getMachine().getMachineIndex(), slot,
                effectType.getValue());
        rackEffect.setEffect(this);
        rackEffect.update(context);
    }

    @Override
    public void restore() {
        rackEffect.restore();
    }

    @Override
    public void disconnect() {
        patch = null;
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onSave() {
    }

    private RackEffect createEffect(int slot, int toneIndex) {
        return EffectFactory.create(effectType, slot, toneIndex);
    }

    @Override
    public String toString() {
        return "[Effect(" + effectType.name() + ", '" + getDefaultName() + "')]";
    }

}
