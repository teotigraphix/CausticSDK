
package com.teotigraphix.caustk.groove.library;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.groove.manifest.LibraryEffectManifest;
import com.teotigraphix.caustk.node.effect.EffectNode;

public class LibraryEffect extends LibraryProductItem {

    @Tag(20)
    private LibraryEffectManifest manifest;

    @Tag(21)
    private Map<Integer, EffectNode> effects = new HashMap<Integer, EffectNode>();

    private transient LibrarySound sound;

    public LibrarySound getSound() {
        return sound;
    }

    public void setSound(LibrarySound sound) {
        this.sound = sound;
    }

    public LibraryEffect(UUID id, UUID productId, LibraryEffectManifest manifest) {
        super(id, productId);
        this.manifest = manifest;
        setFormat(LibraryItemFormat.Effect);
    }

    public EffectNode get(int slot) {
        return effects.get(slot);
    }

    public void add(int slot, EffectNode effectNode) {
        effects.put(slot, effectNode);
    }

}
