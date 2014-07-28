
package com.teotigraphix.caustk.groove.library;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.groove.manifest.LibraryGroupManifest;

public class LibraryGroup extends LibraryProductItem {

    @Tag(20)
    private LibraryGroupManifest manifest;

    @Tag(21)
    private Map<Integer, LibrarySound> sounds = new TreeMap<Integer, LibrarySound>();

    @Override
    public LibraryGroupManifest getManifest() {
        return manifest;
    }

    public LibrarySound getSound(int index) {
        return sounds.get(index);
    }

    public Collection<LibrarySound> getSounds() {
        return sounds.values();
    }

    public LibraryGroup(UUID productId, LibraryGroupManifest manifest) {
        super(productId);
        this.manifest = manifest;
    }

    public void addSound(int index, LibrarySound sound) throws CausticException {
        if (sounds.containsKey(index))
            throw new CausticException("Slot not empty, use replace ");
        sound.setIndex(index);
        sounds.put(index, sound);
        sound.setGroup(this);
    }
}
