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

package com.teotigraphix.caustk.library;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerModel;

/**
 * Serialized - v1.0
 * <ul>
 * <li><code>libraries</code> - A map of {@link UUID} to {@link Library}</li>
 * <li><code>selectedLibraryId</code> - The current {@link Library} {@link UUID}
 * </li>
 * </ul>
 */
public class LibraryManagerModel extends SubControllerModel {

    private transient Library selectedLibrary;
    
    //--------------------------------------------------------------------------
    // Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // libraries
    //----------------------------------

    private transient Map<UUID, Library> libraries = new TreeMap<UUID, Library>();

    Map<UUID, Library> getLibraries() {
        return libraries;
    }

    //----------------------------------
    // selectedLibrary
    //----------------------------------

    private UUID selectedLibraryId;

    public UUID getSelectedLibraryId() {
        return selectedLibraryId;
    }

    public Library getSelectedLibrary() {
        return selectedLibrary;
    }

    public void setSelectedLibrary(Library value) {
        selectedLibrary = value;
        selectedLibraryId = selectedLibrary.getId();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public LibraryManagerModel() {
    }

    public LibraryManagerModel(ICaustkController controller) {
        super(controller);
    }

    //--------------------------------------------------------------------------
    // Method API
    //--------------------------------------------------------------------------

    public Library getLibrary(File reletivePath) {
        for (Library library : libraries.values()) {
            if (library.getName().endsWith(reletivePath.getName()))
                return library;
        }
        return null;
    }

    public void removeLibrary(Library library) throws IOException {
        library.delete();
        libraries.remove(library.getId());
    }

}
