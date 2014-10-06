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

import java.util.UUID;

import com.teotigraphix.caustk.groove.manifest.LibraryItemManifest;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public abstract class LibraryItem {

    /**
     * The library item's descriptor, defines all the unique properties of this
     * item.
     * 
     * @return Each subclass of {@link LibraryItem} will return its own manifest
     *         class type.
     */
    public abstract LibraryItemManifest getManifest();

    //--------------------------------------------------------------------------
    // Manifest Proxy :: Properties
    //--------------------------------------------------------------------------

    /**
     * Returns the {@link UUID} that was assigned when this library item was
     * created.
     */
    public final UUID getId() {
        return getManifest().getId();
    }

    /**
     * Returns the {@link LibraryItemFormat} of this library item.
     */
    public final LibraryItemFormat getFormat() {
        return getManifest().getFormat();
    }

    /**
     * The file name of the library item using the
     * {@link LibraryItemManifest#getName()} and
     * {@link LibraryItemManifest#getExtension()}
     * 
     * @return E.g <code>MySound.gsnd</code>
     */
    public String getFileName() {
        return getManifest().getName() + "." + getManifest().getExtension();
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
        return "[LibraryItem]";
    }
}
