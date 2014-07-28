
package com.teotigraphix.caustk.groove.library;

import java.util.UUID;

import com.teotigraphix.caustk.groove.manifest.LibraryItemManifest;

public class LibrarySample extends LibraryProductItem {

    @Override
    public LibraryItemManifest getManifest() {
        return null;
    }

    public LibrarySample(UUID productId) {
        super(productId);
    }
}
