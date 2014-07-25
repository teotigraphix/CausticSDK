
package com.teotigraphix.caustk.groove;

import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public class LibraryItemDescriptor {

    @Tag(0)
    private UUID id;

    @Tag(1)
    private UUID productId;

    @Tag(2)
    private LibraryItemFormat format;

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
        return "\n[" + format.toString() + "] - " + manifest.getName();
    }

}
