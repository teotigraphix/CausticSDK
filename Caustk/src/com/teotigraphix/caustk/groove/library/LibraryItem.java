
package com.teotigraphix.caustk.groove.library;

import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.groove.manifest.LibraryItemManifest;

public abstract class LibraryItem {

    @Tag(0)
    private UUID id;

    @Tag(1)
    private LibraryItemFormat format;

    public UUID getId() {
        return id;
    }

    public LibraryItemManifest getManifest() {
        return null;
    }

    public final LibraryItemFormat getFormat() {
        return format;
    }

    protected void setFormat(LibraryItemFormat format) {
        this.format = format;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    LibraryItem() {
    }

    public LibraryItem(UUID id) {
        this.id = id;
    }

}
