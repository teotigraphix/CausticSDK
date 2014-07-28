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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.groove.manifest.LibraryEffectManifest;
import com.teotigraphix.caustk.groove.manifest.LibraryInstrumentManifest;
import com.teotigraphix.caustk.groove.manifest.LibrarySoundManifest;

public class LibrarySound extends LibraryProductItem {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(20)
    private LibrarySoundManifest manifest;

    @Tag(21)
    private int index = -1;

    private LibraryInstrumentManifest instrumentManifest;

    private LibraryEffectManifest effectManifest;

    private transient LibraryGroup group;

    private transient LibraryEffect effect;

    private transient LibraryInstrument instrument;

    @Override
    public LibrarySoundManifest getManifest() {
        return manifest;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public LibraryEffectManifest getEffectManifest() {
        return effectManifest;
    }

    public LibraryInstrumentManifest getInstrumentManifest() {
        return instrumentManifest;
    }

    public LibraryGroup getGroup() {
        return group;
    }

    public void setGroup(LibraryGroup group) {
        this.group = group;
    }

    public LibraryEffect getEffect() {
        return effect;
    }

    public void setEffect(LibraryEffect effect) {
        LibraryEffect oldEffect = this.effect;
        if (oldEffect != null)
            oldEffect.setSound(null);
        this.effect = effect;
        if (effect != null)
            effect.setSound(this);
        effectManifest = effect.getManifest();
    }

    public void setInstrument(LibraryInstrument instrument) {
        LibraryInstrument old = this.instrument;
        if (old != null)
            old.setSound(null);
        this.instrument = instrument;
        if (instrument != null)
            instrument.setSound(this);
        instrumentManifest = instrument.getManifest();
    }

    public LibraryInstrument getInstrument() {
        return instrument;
    }

    public LibrarySound(LibrarySoundManifest manifest) {
        this.manifest = manifest;
    }
}
