
package com.teotigraphix.caustk.groove;

public class LibraryItemManifest {

    private LibraryItemBank libraryBank;

    private String name;

    public LibraryItemBank getLibraryBank() {
        return libraryBank;
    }

    public String getName() {
        return name;
    }

    public LibraryItemManifest(LibraryItemBank libraryBank, String name) {
        this.libraryBank = libraryBank;
        this.name = name;
    }

}
