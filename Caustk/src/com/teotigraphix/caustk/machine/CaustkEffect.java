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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.rack.IEffect;
import com.teotigraphix.caustk.rack.effect.EffectType;
import com.teotigraphix.caustk.rack.effect.EffectUtils;

public class CaustkEffect {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private int index = -1;

    @Tag(1)
    private EffectType effectType;

    @Tag(2)
    private CaustkPatch patch;

    @Tag(3)
    private IEffect effect;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

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

    public IEffect getEffect() {
        return effect;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    CaustkEffect() {
    }

    CaustkEffect(int index, EffectType effectType) {
        this.index = index;
        this.effectType = effectType;
    }

    CaustkEffect(int index, EffectType effectType, CaustkPatch caustkPatch) {
        this.index = index;
        this.effectType = effectType;
        this.patch = caustkPatch;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Loads and restores the {@link IEffect} for the machine.
     * 
     * @param factory The library factory.
     */
    public void load(CaustkLibraryFactory factory) {
        effect = EffectUtils.create(effectType, index, patch.getMachine().getIndex());
        effect.restore();
    }
}
