
package com.teotigraphix.caustk.groove;

import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public class LibrarySound extends LibraryProductItem {

    @Tag(10)
    private int index = -1;

    private transient LibraryGroup group;

    private transient LibraryEffect effect;

    private transient LibraryInstrument instrument;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
    }

    public void setInstrument(LibraryInstrument instrument) {
        LibraryInstrument old = this.instrument;
        if (old != null)
            old.setSound(null);
        this.instrument = instrument;
        if (instrument != null)
            instrument.setSound(this);
    }

    public LibraryInstrument getInstrument() {
        return instrument;
    }

    public LibrarySound(UUID id, UUID productId, FileInfo fileInfo, LibraryItemManifest manifest) {
        super(id, productId, fileInfo, manifest);
        setFormat(LibraryItemFormat.Sound);
    }
}
