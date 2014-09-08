////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.groove.manifest;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.groove.library.LibraryItemFormat;
import com.teotigraphix.caustk.node.effect.EffectNode;
import com.teotigraphix.caustk.node.effect.EffectType;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class LibraryEffectManifest extends LibraryItemManifest {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(20)
    private Map<Integer, EffectType> effectTypes = new HashMap<Integer, EffectType>();

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    //  type1
    //----------------------------------

    /**
     * Returns the {@link EffectType} of effect in slot 1.
     */
    public EffectType getType1() {
        return get(0);
    }

    //----------------------------------
    //  type2
    //----------------------------------

    /**
     * Returns the {@link EffectType} of effect in slot 2.
     */
    public EffectType getType2() {
        return get(1);
    }

    /**
     * Whether the effect contains an effect at the slot index.
     * 
     * @param slot The effect slot index.
     */
    public final boolean contains(int slot) {
        return effectTypes.containsKey(slot);
    }

    /**
     * Returns and effect at the slot index if exists.
     * 
     * @param slot The effect slot index.
     */
    public final EffectType get(int slot) {
        return effectTypes.get(1);
    }

    //--------------------------------------------------------------------------
    //  Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization.
     */
    LibraryEffectManifest() {
    }

    public LibraryEffectManifest(String name, String relativePath, EffectNode efffect1,
            EffectNode efffect2) {
        super(LibraryItemFormat.Effect, name, relativePath);

        if (efffect1 != null)
            effectTypes.put(0, efffect1.getType());
        if (efffect2 != null)
            effectTypes.put(1, efffect2.getType());
    }
}
