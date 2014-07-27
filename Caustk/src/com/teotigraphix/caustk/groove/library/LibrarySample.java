
package com.teotigraphix.caustk.groove.library;

import java.util.UUID;

public class LibrarySample extends LibraryProductItem {

    public LibrarySample(UUID id, UUID productId) {
        super(id, productId);
        // TODO create a manifest lcass
        setFormat(LibraryItemFormat.Sample);
    }
}
