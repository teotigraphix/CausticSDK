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

import com.teotigraphix.caustk.groove.utils.LibraryEffectUtils;
import com.teotigraphix.caustk.groove.utils.LibraryGroupUtils;
import com.teotigraphix.caustk.groove.utils.LibraryInstrumentUtils;
import com.teotigraphix.caustk.groove.utils.LibraryPatternBankUtils;
import com.teotigraphix.caustk.groove.utils.LibrarySoundUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CausticFileImporter {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private XStream xstream;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public CausticFileImporter() {
        xstream = new XStream(new DomDriver());

        // CausticItem
        xstream.useAttributeFor(CausticItem.class, "displayName");

        LibraryGroupUtils.configureXStream(xstream);
        LibrarySoundUtils.configureXStream(xstream);
        LibraryEffectUtils.configureXStream(xstream);
        LibraryInstrumentUtils.configureXStream(xstream);
        LibraryPatternBankUtils.configureXStream(xstream);
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    public String toXML(Object instance) {
        return xstream.toXML(instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T fromXMLManifest(File manifestFile, Class<T> clazz) throws FileNotFoundException {
        return (T)xstream.fromXML(new FileReader(manifestFile));
    }

}
