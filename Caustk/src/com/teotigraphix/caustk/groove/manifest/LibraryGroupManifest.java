
package com.teotigraphix.caustk.groove.manifest;

import java.io.File;

import com.teotigraphix.caustk.groove.library.LibraryItemFormat;

public class LibraryGroupManifest extends LibraryItemManifest {

    public LibraryGroupManifest(String displayName, File archiveFile, String relativePath) {
        super(LibraryItemFormat.Group, displayName, archiveFile, relativePath);
    }

}
