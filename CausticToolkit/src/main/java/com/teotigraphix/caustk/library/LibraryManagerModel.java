
package com.teotigraphix.caustk.library;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerModel;

public class LibraryManagerModel extends SubControllerModel {

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

    private transient Library selectedLibrary;

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

    public LibraryManagerModel() {
    }

    public LibraryManagerModel(ICaustkController controller) {
        super(controller);
    }

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
