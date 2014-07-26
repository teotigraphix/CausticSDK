
package com.teotigraphix.caustk.groove.manifest;

import com.teotigraphix.caustk.groove.LibraryBank;

public class LibraryItemManifest {

    private LibraryBank libraryBank;

    private String name;

    public LibraryBank getLibraryBank() {
        return libraryBank;
    }

    public String getName() {
        return name;
    }

    public LibraryItemManifest(String name) {
        this.name = name;
    }

    public LibraryItemManifest(String name, LibraryBank libraryBank) {
        this(name);
        this.libraryBank = libraryBank;
    }

}
