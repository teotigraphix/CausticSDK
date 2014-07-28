
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
