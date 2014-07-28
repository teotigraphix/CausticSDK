
package com.teotigraphix.caustk.groove.manifest;

import java.io.File;

import com.teotigraphix.caustk.groove.library.LibraryItemFormat;

public class LibraryProjectManifest extends LibraryItemManifest {

    public LibraryProjectManifest(String displayName, File archiveFile, String relativePath) {
        super(LibraryItemFormat.Project, displayName, archiveFile, relativePath);
    }

}
