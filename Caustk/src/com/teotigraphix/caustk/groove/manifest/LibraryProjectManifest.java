
package com.teotigraphix.caustk.groove.manifest;

import com.teotigraphix.caustk.groove.LibraryBank;

public class LibraryProjectManifest extends LibraryItemManifest {

    public LibraryProjectManifest(String name) {
        super(name);
    }

    public LibraryProjectManifest(String name, LibraryBank libraryBank) {
        super(name, libraryBank);
    }

}
