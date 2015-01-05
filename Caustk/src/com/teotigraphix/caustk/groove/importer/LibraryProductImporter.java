////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.groove.importer;

import com.teotigraphix.caustk.groove.browser.BrowserBank;
import com.teotigraphix.caustk.groove.browser.BrowserModel;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
