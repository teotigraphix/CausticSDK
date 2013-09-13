////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.library.item;

import java.util.UUID;

import com.teotigraphix.caustk.library.core.Library;
import com.teotigraphix.caustk.library.core.MetadataInfo;

public abstract class LibraryItem { //extends MemorySlotItem {

    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private transient Library library;

    /**
     * Returns the runtime reference of the Library that owns this item.
     */
    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    private UUID id;

    /**
     * A unique identifier for each library item using the {@link UUID} utility.
     */
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    private MetadataInfo metadataInfo;

    public MetadataInfo getMetadataInfo() {
        return metadataInfo;
    }

    public void setMetadataInfo(MetadataInfo metadataInfo) {
        this.metadataInfo = metadataInfo;
    }

    public LibraryItem() {
    }

    public boolean hasTag(String tag) {
        return metadataInfo.getTags().contains(tag);
    }

    public boolean hasTagStartsWith(String search) {
        for (String item : metadataInfo.getTags()) {
            if (item.startsWith(search))
                return true;
        }
        return false;
    }
}
