
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
