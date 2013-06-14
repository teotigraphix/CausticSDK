
package com.teotigraphix.caustk.library;

import java.util.UUID;

import com.teotigraphix.caustk.library.vo.MetadataInfo;

public abstract class LibraryItem {

    private UUID id;

    /**
     * A unique identifier for each library item using the {@link UUID} utility.
     */
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    private MetadataInfo metadataInfo;

    public MetadataInfo getMetadataInfo() {
        return metadataInfo;
    }

    public void setMetadataInfo(MetadataInfo metadataInfo) {
        this.metadataInfo = metadataInfo;
    }

    public LibraryItem() {
    }

}
