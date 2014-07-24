
package com.teotigraphix.caustk.groove;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class BrowserImporter {

    private BrowserModel browserModel;

    private Map<BrowserBank, List<File>> locations = new HashMap<BrowserBank, List<File>>();

    public BrowserImporter(BrowserModel browserModel) {
        this.browserModel = browserModel;
    }

    public void addLocation(BrowserBank bank, File location) {

    }

    public void removeLocation(BrowserBank bank, File location) {

    }

    public void rescan() {

    }
}
