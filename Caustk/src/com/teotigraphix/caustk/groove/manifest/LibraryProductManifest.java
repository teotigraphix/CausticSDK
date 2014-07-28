
package com.teotigraphix.caustk.groove.manifest;

import java.io.File;

import com.teotigraphix.caustk.groove.library.LibraryItemFormat;

public class LibraryProductManifest extends LibraryItemManifest {

    public LibraryProductManifest(String displayName, File archiveFile, String relativePath) {
        super(LibraryItemFormat.Product, displayName, archiveFile, relativePath);
    }

}
