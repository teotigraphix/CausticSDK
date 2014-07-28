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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class CausticFileImporter {

    private XStream xstream;

    public CausticFileImporter() {

        xstream = new XStream(new DomDriver());
        xstream.alias("group", CausticGroup.class);
        xstream.useAttributeFor(CausticGroup.class, "path");
        xstream.useAttributeFor(CausticGroup.class, "sourceFile");
        xstream.useAttributeFor(CausticGroup.class, "displayName");

        xstream.alias("sound", CausticSound.class);
        xstream.addImplicitMap(CausticGroup.class, "sounds", CausticSound.class, "index");

        xstream.useAttributeFor(CausticSound.class, "path");
        xstream.useAttributeFor(CausticSound.class, "index");
        xstream.useAttributeFor(CausticSound.class, "displayName");

        xstream.useAttributeFor(CausticEffect.class, "path");
        xstream.useAttributeFor(CausticEffect.class, "displayName");

        //String xml = xstream.toXML(causticGroup);

    }

    public CausticGroup importCaustic(File targetFile) throws IOException {
        String xml = FileUtils.readFileToString(targetFile);
        CausticGroup causticGroup = (CausticGroup)xstream.fromXML(xml);
        return causticGroup;
    }

    @SuppressWarnings("unused")
    public void addToDirectory(LibraryProduct product, CausticGroup causticGroup)
            throws IOException, CausticException {

        File productDirectory = product.getDirectory();

        File groupsDirectory = new File(productDirectory, "Groups");
        File soundsDirectory = new File(productDirectory, "Sounds");
        File instrumentsDirectory = new File(productDirectory, "Instruments");
        File effectsDirectory = new File(productDirectory, "Effects");

        LibraryGroup libraryGroup = causticGroup.create(product);
        product.fillGroup(libraryGroup);

        // export Group
        product.addItem(libraryGroup);
        product.saveItem(libraryGroup);

        // export Sounds
        for (LibrarySound librarySound : libraryGroup.getSounds()) {
            product.addItem(librarySound);
            product.saveItem(librarySound);
        }

        // export Effect
        for (LibrarySound librarySound : libraryGroup.getSounds()) {
            LibraryEffect libraryEffect = librarySound.getEffect();
            product.addItem(libraryEffect);
            product.saveItem(libraryEffect);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // export Instrument
        for (LibrarySound librarySound : libraryGroup.getSounds()) {
            LibraryInstrument libraryInstrument = librarySound.getInstrument();
            product.addItem(libraryInstrument);
            product.saveItem(libraryInstrument);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
