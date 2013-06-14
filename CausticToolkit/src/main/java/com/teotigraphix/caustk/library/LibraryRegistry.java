
package com.teotigraphix.caustk.library;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class LibraryRegistry {
    
    private Map<String, Library> libraries = new TreeMap<String, Library>();

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
        libraries.put(library.getId().toString(), library);
    }

    public void removeLibrary(Library library) {
        removeLibrary(library.getId().toString());
    }

    public Library removeLibrary(String id) {
        return libraries.remove(id);
    }

}
