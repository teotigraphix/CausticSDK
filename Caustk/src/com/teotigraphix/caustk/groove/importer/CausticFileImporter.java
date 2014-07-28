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
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.groove.utils.LibraryGroupUtils;
import com.teotigraphix.caustk.groove.utils.LibrarySoundUtils;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class CausticFileImporter {

    private XStream xstream;

    public CausticFileImporter() {

        xstream = new XStream(new DomDriver());
        xstream.alias("group", CausticGroup.class);
        xstream.useAttributeFor(CausticGroup.class, "sourceFile");
        xstream.useAttributeFor(CausticGroup.class, "displayName");

        xstream.alias("sound", CausticSound.class);
        xstream.addImplicitMap(CausticGroup.class, "sounds", CausticSound.class, "index");

        xstream.useAttributeFor(CausticSound.class, "index");
        xstream.useAttributeFor(CausticSound.class, "displayName");

        xstream.useAttributeFor(CausticEffect.class, "displayName");

        //String xml = xstream.toXML(causticGroup);

    }

    public CausticGroup importCaustic(File targetFile) throws IOException {
        String xml = FileUtils.readFileToString(targetFile);
        CausticGroup causticGroup = (CausticGroup)xstream.fromXML(xml);
        return causticGroup;
    }

    public void addToDirectory(LibraryProduct product, CausticGroup causticGroup)
            throws IOException, CausticException {

        File productDirectory = product.getDirectory();

        File groupsDirectory = new File(productDirectory, "Groups");
        File soundsDirectory = new File(productDirectory, "Sounds");
        //        File instrumentsDirectory = new File(productDirectory, "Instruments");
        //        File effectsDirectory = new File(productDirectory, "Effects");

        // export Group
        exportGroup(product, new File(groupsDirectory, causticGroup.getDisplayName() + ".ggrp"),
                causticGroup);

        // export Sounds
        exportSounds(product, causticGroup, soundsDirectory);

        // export Instrument

        // export Effect

    }

    private void exportSounds(LibraryProduct product, CausticGroup causticGroup,
            File soundsDirectory) throws IOException {

        for (CausticSound causticSound : causticGroup.getSounds().values()) {
            MachineNode machineNode = CaustkRuntime.getInstance().getRack()
                    .getMachine(causticSound.getIndex());
            int index = machineNode.getIndex();

            File sourceDirectory = new File(soundsDirectory, "__sound__" + index);
            //            File zipFile = new File(soundsDirectory, causticSound.getDisplayName() + ".gsnd");

            String soundName = causticSound.getDisplayName() + ".gsnd";

            // create Sound 
            @SuppressWarnings("unused")
            LibrarySound sound = LibrarySoundUtils.createSound(product, soundName, causticGroup
                    .getDisplayName(), machineNode, soundsDirectory,
                    causticGroup.getSounds().get(index));

            //group.addSound(index, sound);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            FileUtils.deleteDirectory(sourceDirectory);
        }
    }

    private void exportGroup(LibraryProduct product, File targetFile, CausticGroup causticGroup)
            throws IOException, CausticException {
        File causticFile = causticGroup.getSourceFile();

        @SuppressWarnings("unused")
        LibraryGroup libraryGroup = LibraryGroupUtils.exportGroup(product, causticFile, targetFile,
                causticGroup);
    }
}
