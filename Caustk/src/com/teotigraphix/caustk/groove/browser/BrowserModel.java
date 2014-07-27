
package com.teotigraphix.caustk.groove.browser;

import java.util.Collection;

import com.teotigraphix.caustk.groove.importer.LibraryProductImporter;
import com.teotigraphix.caustk.groove.library.GrooveLibrary;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryProject;
import com.teotigraphix.caustk.groove.library.LibrarySample;
import com.teotigraphix.caustk.groove.library.LibrarySound;

@SuppressWarnings("unused")
public class BrowserModel {

    private GrooveLibrary library;

    private LibraryProductImporter importer;

    private BrowserBank bank = BrowserBank.Factory;

    public BrowserModel(GrooveLibrary library, LibraryProductImporter importer) {
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
