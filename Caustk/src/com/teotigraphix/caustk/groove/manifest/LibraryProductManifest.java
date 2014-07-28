
package com.teotigraphix.caustk.groove.manifest;

import java.io.File;

import com.teotigraphix.caustk.groove.library.LibraryItemFormat;

public class LibraryProductManifest extends LibraryItemManifest {

    private File directory;

    public boolean exists() {
        return (directory != null && directory.exists());
    }

    public File getDirectory() {
        return directory;
    }

    @Override
    public String getRelativePath() {
        throw new UnsupportedOperationException();
    }

    public LibraryProductManifest(String name, File directory) {
        super(LibraryItemFormat.Product, name, null);
        this.directory = directory;
    }
}
