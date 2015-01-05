////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.groove.library;

import com.teotigraphix.caustk.groove.utils.LibraryProductUtils;

import java.io.File;
import java.util.UUID;

public abstract class LibraryProductItem extends LibraryItem {

    public UUID getProductId() {
        return getManifest().getProductId();
    }

    /**
     * Returns the manifet's name.
     */
    public String getName() {
        return getManifest().getName();
    }

    /**
     * Returns the manifet's display name, if null returns the name.
     */
    public String getDisplayName() {
        if (!getManifest().hasDisplayName())
            return getManifest().getName();
        return getManifest().getDisplayName();
    }

    /**
     * Returns the relative path from within the item's type IE
     * <code>Effects</code> would be the item's type and
     * <code>Delay/PingPong</code> would be the relative path.
     * <p>
     * The return value would then be <code>Delay/PingPong</code>.
     */
    public String getRawRelativePath() {
        return getManifest().getRelativePath();
    }

    public String getCalculatedPath() {
        return getManifest().getCalculatedPath();
    }

    /**
     * Returns the relative base of the file within the product.
     * <p>
     * E.g <code>/Groups/ALLEY 01.ggrp</code>
     */
    public File getProductPath() {
        final String formatDirectoryName = LibraryProductUtils.toItemBaseDirectoryName(this);
        final File base = getCalculatedPath() != null ? new File(formatDirectoryName,
                getCalculatedPath()) : new File(formatDirectoryName);
        final File productPath = new File(base, getFileName());
        return productPath;
    }

    LibraryProductItem() {
    }

}
