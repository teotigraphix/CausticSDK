
package com.teotigraphix.caustk.groove.manifest;

import com.teotigraphix.caustk.groove.LibraryBank;

public class LibraryProductManifest extends LibraryItemManifest {

    public LibraryProductManifest(String name) {
        super(name);
    }

    public LibraryProductManifest(String name, LibraryBank libraryBank) {
        super(name, libraryBank);
    }

}
