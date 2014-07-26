
package com.teotigraphix.caustk.groove;

import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.groove.manifest.LibraryItemManifest;

public abstract class LibraryItem {

    @Tag(0)
    private UUID id;

    @Tag(1)
    private FileInfo fileInfo;

    @Tag(2)
    private LibraryItemFormat format;

    public UUID getId() {
        return id;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
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

    public LibraryItem(UUID id, FileInfo fileInfo) {
        this.id = id;
        this.fileInfo = fileInfo;
    }

}
