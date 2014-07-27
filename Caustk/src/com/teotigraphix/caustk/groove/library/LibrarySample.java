
package com.teotigraphix.caustk.groove.library;

import java.util.UUID;

import com.teotigraphix.caustk.groove.FileInfo;

public class LibrarySample extends LibraryProductItem {

    public LibrarySample(UUID id, UUID productId, FileInfo fileInfo) {
        super(id, productId, fileInfo);
        // TODO create a manifest lcass
        setFormat(LibraryItemFormat.Sample);
    }
}
