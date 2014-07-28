
package com.teotigraphix.caustk.groove.library;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.groove.manifest.LibraryProjectManifest;

public class LibraryProject extends LibraryProductItem {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(20)
    private LibraryProjectManifest manifest;

    @Override
    public LibraryProjectManifest getManifest() {
        return manifest;
    }

    public LibraryProject(LibraryProjectManifest manifest) {
        this.manifest = manifest;
    }
}
