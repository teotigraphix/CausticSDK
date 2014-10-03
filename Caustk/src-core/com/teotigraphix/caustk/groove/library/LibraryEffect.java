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

package com.teotigraphix.caustk.groove.library;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.groove.manifest.LibraryEffectManifest;
import com.teotigraphix.caustk.node.effect.EffectNode;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class LibraryEffect extends LibraryProductItem {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(50)
    private LibraryEffectManifest manifest;

    @Tag(51)
    private Map<Integer, EffectNode> effects = new HashMap<Integer, EffectNode>();

    //--------------------------------------------------------------------------
    // Transient :: Variables
    //--------------------------------------------------------------------------

    private transient LibrarySound sound;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    //  manifest
    //----------------------------------

    @Override
    public LibraryEffectManifest getManifest() {
        return manifest;
    }

    //----------------------------------
    //  sound
    //----------------------------------

    public LibrarySound getSound() {
        return sound;
    }

    public void setSound(LibrarySound sound) {
        this.sound = sound;
    }

    //--------------------------------------------------------------------------
    //  Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization.
     */
    LibraryEffect() {
    }

    public LibraryEffect(LibraryEffectManifest manifest) {
        this.manifest = manifest;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public EffectNode get(int slot) {
        if (!effects.containsKey(slot))
            return null;
        return effects.get(slot);
    }

    public void add(int slot, EffectNode effectNode) {
        if (effectNode != null)
            effects.put(slot, effectNode);
    }

}
