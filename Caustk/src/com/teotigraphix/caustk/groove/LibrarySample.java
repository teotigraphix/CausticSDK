
package com.teotigraphix.caustk.groove;

import java.util.UUID;

public class LibrarySample extends LibraryProductItem {

    public LibrarySample(UUID id, UUID productId, FileInfo fileInfo) {
        super(id, productId, fileInfo);
        // TODO create a manifest lcass
        setFormat(LibraryItemFormat.Sample);
    }
}
