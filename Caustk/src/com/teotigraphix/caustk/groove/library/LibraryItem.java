
package com.teotigraphix.caustk.groove.library;

import java.util.UUID;

import com.teotigraphix.caustk.groove.manifest.LibraryItemManifest;

public abstract class LibraryItem {

    public final UUID getId() {
        return getManifest().getId();
    }

    public abstract LibraryItemManifest getManifest();

    public final LibraryItemFormat getFormat() {
        return getManifest().getFormat();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    LibraryItem() {
    }

}
