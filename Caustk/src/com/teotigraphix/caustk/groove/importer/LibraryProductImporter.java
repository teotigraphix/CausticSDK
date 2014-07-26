
package com.teotigraphix.caustk.groove.importer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teotigraphix.caustk.groove.BrowserBank;
import com.teotigraphix.caustk.groove.BrowserModel;

@SuppressWarnings("unused")
public class LibraryProductImporter {

    private BrowserModel browserModel;

    private Map<BrowserBank, List<File>> locations = new HashMap<BrowserBank, List<File>>();

    public LibraryProductImporter(BrowserModel browserModel) {
        this.browserModel = browserModel;
    }

    public void addLocation(BrowserBank bank, File location) {

    }

    public void removeLocation(BrowserBank bank, File location) {

    }

    public void rescan() {

    }
}
