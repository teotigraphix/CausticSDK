
package com.teotigraphix.caustk.library;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerModel;

public class LibraryManagerModel extends SubControllerModel {

    private transient Map<UUID, Library> libraries = new TreeMap<UUID, Library>();

    Map<UUID, Library> getLibraies() {
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

}
