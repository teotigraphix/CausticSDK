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

package com.teotigraphix.caustk.groove.browser;

import com.teotigraphix.caustk.groove.importer.LibraryProductImporter;
import com.teotigraphix.caustk.groove.library.GrooveLibrary;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryProject;
import com.teotigraphix.caustk.groove.library.LibrarySample;
import com.teotigraphix.caustk.groove.library.LibrarySound;

import java.util.Collection;

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
