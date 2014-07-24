
package com.teotigraphix.caustk.groove;

import java.util.Collection;

@SuppressWarnings("unused")
public class BrowserModel {

    private GrooveLibrary library;

    private BrowserImporter importer;

    private BrowserBank bank = BrowserBank.Factory;

    public BrowserModel(GrooveLibrary library, BrowserImporter importer) {
        this.library = library;
        this.importer = importer;
    }

    public Collection<LibraryProject> getAllProjects() {
        return library.getProjects();
    }

    public Collection<LibraryGroup> getAllGroups() {
        return library.getGroups();
    }

    public Collection<LibrarySound> getAllSounds() {
        return library.getSounds();
    }

    public Collection<LibraryInstrument> getAllInstruments() {
        return library.getInstruments();
    }

    public Collection<LibraryEffect> getAllEffects() {
        return library.getEffects();
    }

    public Collection<LibrarySample> getAllSamples() {
        return library.getSamples();
    }
}
