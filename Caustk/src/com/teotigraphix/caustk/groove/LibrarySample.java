
package com.teotigraphix.caustk.groove;

import java.util.UUID;

public class LibrarySample extends LibraryProductItem {

    public LibrarySample(UUID id, UUID productId, FileInfo fileInfo, LibraryItemManifest manifest) {
        super(id, productId, fileInfo, manifest);
        setFormat(LibraryItemFormat.Sample);
    }
}
