
package com.teotigraphix.caustk.groove.library;

import java.io.File;
import java.util.UUID;

import com.teotigraphix.caustk.groove.utils.LibraryProductUtils;

public abstract class LibraryProductItem extends LibraryItem {

    public UUID getProductId() {
        return getManifest().getProductId();
    }

    /**
     * Returns the relative base of the file within the product.
     * <p>
     * E.g <code>/Groups/ALLEY 01.ggrp</code>
     */
    public File getProductPath() {
        final String formatDirectoryName = LibraryProductUtils.toItemBaseDirectoryName(this);
        final File base = new File(formatDirectoryName, getRelativePath());
        final File productPath = new File(base, getFileName());
        return productPath;
    }

    LibraryProductItem() {
    }

}
