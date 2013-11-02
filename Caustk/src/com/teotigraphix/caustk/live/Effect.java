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
import com.teotigraphix.caustk.controller.IRackContext;
import com.teotigraphix.caustk.controller.IRackSerializer;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.EffectRackMessage;
import com.teotigraphix.caustk.rack.IEffect;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.effect.EffectFactory;
import com.teotigraphix.caustk.rack.effect.EffectType;

/**
 * @author Michael Schmalle
 */
public class Effect implements IRackSerializer, ICaustkComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private ComponentInfo info;

    @Tag(10)
    private int index = -1;

    @Tag(11)
    private EffectType effectType;

    @Tag(12)
    private Patch patch;

    @Tag(13)
    private IEffect effect;

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

    //----------------------------------
    // index
    //----------------------------------

    /**
     * The slot this effect is held in the {@link LivePatch}, -1 if not assigned
     * to a {@link LivePatch}.
     */
    public int getIndex() {
        return index;
    }

    void setIndex(int value) {
        index = value;
    }

    //----------------------------------
    // effectType
    //----------------------------------

    public EffectType getEffectType() {
        return effectType;
    }

    //----------------------------------
    // effect
    //----------------------------------

    @SuppressWarnings("unchecked")
    public <T extends IEffect> T getEffect() {
        return (T)effect;
    }

    //----------------------------------
    // effect
    //----------------------------------

    public Patch getPatch() {
        return patch;
    }

    void setPatch(Patch value) {
        patch = value;
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

    Effect(ComponentInfo info, int index, EffectType effectType) {
        this.info = info;
        this.index = index;
        this.effectType = effectType;
    }

    Effect(ComponentInfo info, int index, EffectType effectType, Patch caustkPatch) {
        this.info = info;
        this.index = index;
        this.effectType = effectType;
        this.patch = caustkPatch;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Creates the {@link IEffect} but assigns -1 to the slot and machine index.
     */
    public void create() {
        effect = createEffect(-1, -1);
    }

    /**
     * Loads and restores the {@link IEffect} for the machine.
     * 
     * @param factory The library factory.
     * @throws CausticException
     */
    @Override
    public void load(IRackContext context) throws CausticException {
        effect = createEffect(index, patch.getMachine().getIndex());
        effect.setEffect(this);
        effect.load(context);
    }

    @Override
    public void restore() {
        effect.restore();
    }

    @Override
    public void update() {
        EffectRackMessage.CREATE.send(getRack(), getPatch().getMachine().getIndex(), index,
                effectType.getValue());
        effect.setEffect(this);
        effect.update();
    }

    IEffect createEffect(int slot, int toneIndex) {
        return EffectFactory.create(effectType, slot, toneIndex);
    }

}
