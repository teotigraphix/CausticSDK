
package com.teotigraphix.caustk.groove.manifest;

import java.io.File;

import com.teotigraphix.caustk.groove.library.LibraryItemFormat;

public class LibrarySoundManifest extends LibraryItemManifest {

    public LibrarySoundManifest(String displayName, File archiveFile, String relativePath) {
        super(LibraryItemFormat.Sound, displayName, archiveFile, relativePath);
    }

}
