
package com.teotigraphix.caustk.groove;

import java.util.UUID;

public class LibraryProject extends LibraryProductItem {

    public LibraryProject(UUID id, UUID productId, FileInfo fileInfo, LibraryItemManifest manifest) {
        super(id, productId, fileInfo, manifest);
        setFormat(LibraryItemFormat.Project);
    }
}
