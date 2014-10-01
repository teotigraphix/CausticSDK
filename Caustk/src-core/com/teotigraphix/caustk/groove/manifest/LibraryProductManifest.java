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

package com.teotigraphix.caustk.groove.manifest;

import java.io.File;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.groove.library.LibraryItemFormat;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class LibraryProductManifest extends LibraryItemManifest {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(50)
    private File directory;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    //  directory
    //----------------------------------

    /**
     * Returns the absolute directory of the library product.
     */
    public File getDirectory() {
        return directory;
    }

    /**
     * Whether the product directory exists on disk.
     */
    public boolean exists() {
        return (directory != null && directory.exists());
    }

    //--------------------------------------------------------------------------
    // Overridden Public :: Methods
    //--------------------------------------------------------------------------

    @Override
    public String getRelativePath() {
        throw new UnsupportedOperationException();
    }

    //--------------------------------------------------------------------------
    //  Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization.
     */
    public LibraryProductManifest(String name, File directory) {
        super(LibraryItemFormat.Product, name, null);
        this.directory = directory;
    }
}
