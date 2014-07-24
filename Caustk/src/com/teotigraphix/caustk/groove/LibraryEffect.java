
package com.teotigraphix.caustk.groove;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.node.effect.EffectNode;

public class LibraryEffect extends LibraryItem {

    private transient LibrarySound sound;

    public LibrarySound getSound() {
        return sound;
    }

    public void setSound(LibrarySound sound) {
        this.sound = sound;
    }

    @Tag(10)
    private Map<Integer, EffectNode> effects = new HashMap<Integer, EffectNode>();

    public LibraryEffect(UUID id, FileInfo fileInfo, LibraryItemManifest manifest) {
        super(id, fileInfo, manifest);
    }

    public EffectNode get(int slot) {
        return effects.get(slot);
    }

    public void add(int slot, EffectNode effectNode) {
        effects.put(slot, effectNode);
    }
}
