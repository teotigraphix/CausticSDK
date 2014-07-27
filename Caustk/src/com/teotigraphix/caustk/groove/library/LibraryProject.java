
package com.teotigraphix.caustk.groove.library;

import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.groove.FileInfo;
import com.teotigraphix.caustk.groove.manifest.LibraryProjectManifest;

public class LibraryProject extends LibraryProductItem {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(20)
    private LibraryProjectManifest manifest;

    public LibraryProject(UUID id, UUID productId, FileInfo fileInfo,
            LibraryProjectManifest manifest) {
        super(id, productId, fileInfo);
        this.manifest = manifest;
        setFormat(LibraryItemFormat.Project);
    }
}
