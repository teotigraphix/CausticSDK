
package com.teotigraphix.caustk.groove.library;

import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.groove.manifest.LibraryItemManifest;

public class LibraryItemDescriptor {

    @Tag(0)
    private UUID id;

    @Tag(1)
    private UUID productId;

    @Tag(2)
    private LibraryItemFormat format;

    // XXX Need converter, that uses format to create the correct manifest
    @Tag(3)
    private LibraryItemManifest manifest;

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public LibraryItemFormat getFormat() {
        return format;
    }

    public LibraryItemManifest getManifest() {
        return manifest;
    }

    public LibraryItemDescriptor(LibraryProductItem item) {
        initialize(item);
    }

    private void initialize(LibraryProductItem item) {
        id = item.getId();
        productId = item.getProductId();
        format = item.getFormat();
        manifest = item.getManifest();
    }

    @Override
    public String toString() {
        return "\n[" + format.toString() + "] - " + manifest.getDisplayName();
    }

}
