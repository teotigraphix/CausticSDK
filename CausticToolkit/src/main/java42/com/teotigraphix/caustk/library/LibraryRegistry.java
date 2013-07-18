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
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class LibraryRegistry {

    private Map<UUID, Library> libraries = new TreeMap<UUID, Library>();

    public Collection<Library> getLibraries() {
        return libraries.values();
    }

    private File directory;

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public LibraryRegistry(File directory) {
        this.directory = directory;
    }

    public void addLibrary(Library library) {
        libraries.put(library.getId(), library);
    }

    public void removeLibrary(Library library) {
        removeLibrary(library.getId());
    }

    public Library removeLibrary(UUID id) {
        return libraries.remove(id);
    }

    public Library getLibrary(UUID id) {
        return libraries.get(id);
    }

    public Library getLibrary(String id) {
        return libraries.get(UUID.fromString(id));
    }
}
