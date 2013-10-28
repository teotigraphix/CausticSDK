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

import java.io.File;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.IRackSerializer;
import com.teotigraphix.caustk.rack.IEffect;
import com.teotigraphix.caustk.rack.effect.EffectFactory;
import com.teotigraphix.caustk.rack.effect.EffectType;

/**
 * @author Michael Schmalle
 */
public class CaustkEffect implements IRackSerializer, ICaustkComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private UUID id;

    @Tag(1)
    private String name;

    @Tag(2)
    private File file;

    @Tag(10)
    private int index = -1;

    @Tag(11)
    private EffectType effectType;

    @Tag(12)
    private CaustkPatch patch;

    @Tag(13)
    private IEffect effect;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // id
    //----------------------------------

    @Override
    public UUID getId() {
        return id;
    }

    //----------------------------------
    // name
    //----------------------------------

    @Override
    public String getName() {
        return name;
    }

    //----------------------------------
    // file
    //----------------------------------

    @Override
    public File getFile() {
        return file;
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

    public IEffect getEffect() {
        return effect;
    }

    //----------------------------------
    // effect
    //----------------------------------

    public CaustkPatch getPatch() {
        return patch;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    CaustkEffect() {
    }

    CaustkEffect(UUID id, EffectType effectType) {
        this.id = id;
        this.effectType = effectType;
    }

    CaustkEffect(UUID id, int index, EffectType effectType) {
        this.id = id;
        this.index = index;
        this.effectType = effectType;
    }

    CaustkEffect(UUID id, int index, EffectType effectType, CaustkPatch caustkPatch) {
        this.id = id;
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
    @Override
    public void load(CaustkLibraryFactory factory) {
        effect = EffectFactory.create(effectType, index, patch.getMachine().getIndex());
    }

    @Override
    public void restore() {
        effect.restore();
    }

    @Override
    public void update() {
    }

}
