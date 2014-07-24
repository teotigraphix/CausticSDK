
package com.teotigraphix.caustk.groove;

import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public abstract class LibraryItem {

    @Tag(0)
    private UUID id;

    @Tag(1)
    private FileInfo fileInfo;

    @Tag(2)
    private LibraryItemManifest manifest;

    public UUID getId() {
        return id;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public LibraryItemManifest getManifest() {
        return manifest;
    }

    public LibraryItemFormat getFormat() {
        return fileInfo.getFormat();
    }

    public LibraryItem(UUID id, FileInfo fileInfo, LibraryItemManifest manifest) {
        this.id = id;
        this.fileInfo = fileInfo;
        this.manifest = manifest;
    }

}
