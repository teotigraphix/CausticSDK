
package com.teotigraphix.caustk.groove.manifest;

import com.teotigraphix.caustk.groove.LibraryBank;

public class LibraryGroupManifest extends LibraryItemManifest {

    public LibraryGroupManifest(String name) {
        super(name);
    }

    public LibraryGroupManifest(String name, LibraryBank libraryBank) {
        super(name, libraryBank);
    }

}
