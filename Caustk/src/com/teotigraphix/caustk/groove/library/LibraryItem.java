
package com.teotigraphix.caustk.groove.library;

import java.util.UUID;

import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.groove.manifest.LibraryItemManifest;

public abstract class LibraryItem {

    public final UUID getId() {
        return getManifest().getId();
    }

    public abstract LibraryItemManifest getManifest();

    public final LibraryItemFormat getFormat() {
        return getManifest().getFormat();
    }

    public String getFileName() {
        return getManifest().getName() + "." + getManifest().getExtension();
    }

    public String getRelativePath() {
        return getManifest().getRelativePath();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    LibraryItem() {
    }

    @Override
    public String toString() {
        return CaustkRuntime.getInstance().getFactory().serialize(this, true);
    }

}
