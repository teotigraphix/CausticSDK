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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.groove.utils.LibraryEffectUtils;
import com.teotigraphix.caustk.groove.utils.LibraryInstrumentUtils;
import com.teotigraphix.caustk.groove.utils.LibraryProductUtils;
import com.teotigraphix.caustk.groove.utils.LibrarySoundUtils;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.effect.EffectsChannel;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.utils.SerializeUtils;
import com.teotigraphix.caustk.utils.ZipCompress;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class CausticFileImporter {

    private static final String MANIFEST_JSON = "manifest.json";

    private XStream xstream;

    public CausticFileImporter() {

        xstream = new XStream(new DomDriver());

        // CausticGroup
        xstream.alias("group", CausticGroup.class);
        xstream.addImplicitMap(CausticGroup.class, "sounds", CausticSound.class, "index");
        xstream.useAttributeFor(CausticGroup.class, "path");
        xstream.useAttributeFor(CausticGroup.class, "sourceFile");
        xstream.useAttributeFor(CausticItem.class, "displayName");

        LibrarySoundUtils.configureXStream(xstream);
        LibraryEffectUtils.configureXStream(xstream);
        LibraryInstrumentUtils.configureXStream(xstream);
    }

    public CausticGroup createGroupFromCausticFile(String path, String displayName, File causticFile)
            throws CausticException {
        CausticGroup causticGroup = new CausticGroup(path, displayName, causticFile);
        fillGroup(causticGroup);
        return causticGroup;
    }

    static void fillGroup(CausticGroup causticGroup) throws CausticException {
        File causticFile = causticGroup.getSourceFile();
        if (!causticFile.exists())
            throw new CausticException(".caustic File does not exist ;" + causticFile);

        RackNode rackNode = CaustkRuntime.getInstance().getRack().create(causticFile);

        for (MachineNode machineNode : rackNode.getMachines()) {
            CausticSound causticSound = fillSound(causticGroup, machineNode);
            causticGroup.addSound(causticSound);
        }
    }

    private static CausticSound fillSound(CausticGroup causticGroup, MachineNode machineNode) {
        //String path = null;
        //        CausticSound causticSound = new CausticSound(path, machineNode.getIndex(),
        //                machineNode.getName());
        //
        //        String groupName = causticGroup.getDisplayName();
        //        String name = groupName + "-" + machineNode.getName();
        //        String relativePath = "";
        //        if (causticSound != null) {
        //            name = causticSound.getDisplayName();
        //            relativePath = causticSound.getPath();
        //        }
        //
        //        CausticEffect causticEffect = fillEffect(machineNode.getEffects(), machineNode.getName(),
        //                groupName, causticSound);
        //
        //        //        LibraryInstrument libraryInstrument = fillInstrument(machineNode, product, name, groupName);
        //        //
        //        //        LibrarySound librarySound = getFactory().createLibrarySound(product, name, relativePath);
        //        //
        //        //        librarySound.setEffect(libraryEffect);
        //        //        librarySound.setInstrument(libraryInstrument);
        return null;
    }

    @SuppressWarnings("unused")
    private static CausticEffect fillEffect(EffectsChannel effectsChannel, String machineName,
            String groupName, CausticSound causticSound) {
        String name = machineName + " FX";
        String relativePath = groupName;
        if (causticSound != null) {
            CausticEffect causticEffect = causticSound.getEffect();
            if (causticEffect != null) {
                name = causticEffect.getDisplayName();
                relativePath = causticEffect.getPath();
            }
        }

        //        CausticEffect causticEffect = new CausticEffect(index, displayName);
        //        EffectNode efffect0 = effectsChannel.getEfffect(0);
        //        EffectNode efffect1 = effectsChannel.getEfffect(1);

        //        LibraryEffect libraryEffect = getFactory().createLibraryEffect(product, name, relativePath,
        //                efffect0, efffect1);

        return null;
    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    public LibraryGroup importCausticIntoProduct(LibraryProduct product, File causticFile,
            String groupName) throws IOException, CausticException {
        //        String path = null;
        //        CausticGroup causticGroup = new CausticGroup(path, causticFile, groupName, groupName);
        //        LibraryGroup libraryGroup = causticGroup.create(product);
        //        //product.fillGroup(libraryGroup); called in addToDirectory()
        //        boolean exportAsGroup = true;
        //        addToDirectory(product, causticGroup, exportAsGroup);
        //        return libraryGroup;
        return null;
    }

    /**
     * Imports an XML file containing {@link CausticGroup} serialization.
     * 
     * @param manifestFile the group XML file
     * @throws IOException
     */
    public CausticGroup importFromGroupManifest(File manifestFile) throws IOException {
        String xml = FileUtils.readFileToString(manifestFile);
        CausticGroup causticGroup = (CausticGroup)xstream.fromXML(xml);
        return causticGroup;
    }

    /**
     * Adds the contents of the {@link CausticGroup} to the
     * {@link LibraryProduct#getDirectory()}.
     * <p>
     * As the library items are created, the CausticGroup is used to map names,
     * tags, met data etc into the library items.
     * <p>
     * The <code>.caustic</code> file is loaded during this operation and
     * blank_rack is called, needs to be used in a utility application.
     * 
     * @param product The {@link LibraryProduct} to add library items to.
     * @param causticGroup The {@link CausticGroup} used in mapping existing
     *            items from the .caustic file into the library item.
     * @param exportAsGroup
     * @throws IOException
     * @throws CausticException
     */
    @SuppressWarnings("unused")
    public void addToDirectory(LibraryProduct product, CausticGroup causticGroup,
            boolean exportAsGroup) throws IOException, CausticException {

        File productDirectory = product.getDirectory();

        File groupsDirectory = new File(productDirectory, "Groups");
        File soundsDirectory = new File(productDirectory, "Sounds");
        File instrumentsDirectory = new File(productDirectory, "Instruments");
        File effectsDirectory = new File(productDirectory, "Effects");

        LibraryGroup libraryGroup = causticGroup.create(product);
        product.fillGroup(libraryGroup);

        //if (exportAsGroup) {
        // export Group
        product.addItem(libraryGroup);
        product.saveItem(libraryGroup);
        //} else {

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
        //}
    }

    public LibraryProduct loadProduct(File productArchive) throws CausticException {
        // XXX FIX
        //        String json = ZipUtils.readZipString(productArchive, new File(MANIFEST_JSON));
        //        LibraryProduct product = SerializeUtils.unpack(json, LibraryProduct.class);
        return null;
    }

    /**
     * Export product to the .gprod targetArchive
     * 
     * @param product
     * @param targetArchive
     * @throws IOException
     * @throws CausticException
     */
    public void exportProduct(final LibraryProduct product, File targetArchive) throws IOException,
            CausticException {
        final File productDirectory = product.getDirectory();
        final File manifestFile = new File(productDirectory, MANIFEST_JSON);
        //        final String json = CaustkRuntime.getInstance().getFactory().serialize(product, true);
        //        FileUtils.write(manifestFile, json);
        //        
        SerializeUtils.pack(manifestFile, product);

        final ZipCompress compress = new ZipCompress(productDirectory);
        compress.zip(targetArchive);
    }

    public void packageProduct(final LibraryProduct product, File targetArchive)
            throws IOException, CausticException {
        final File productDirectory = product.getDirectory();

        Collection<File> files = FileUtils.listFiles(productDirectory, new IOFileFilter() {
            // File
            @Override
            public boolean accept(File directory, String filename) {
                System.out.println("File1: " + filename);
                return true;
            }

            @Override
            public boolean accept(File pathname) {
                //System.out.println("File2: " + pathname);
                return true;
            }
        }, new IOFileFilter() {
            // Directory
            @Override
            public boolean accept(File directory, String filename) {
                System.out.println("Directory1: " + filename);
                return true;
            }

            @Override
            public boolean accept(File pathname) {
                System.out.println("Directory2: " + pathname);
                return true;
            }
        });

        for (File archiveFile : files) {
            // adds the manifest of the LibraryItem to the LibraryProduct
            LibraryProductUtils.addLibraryItemArchive(archiveFile, product);
        }

        exportProduct(product, targetArchive);
    }

    public String toXML(Object instance) {
        return xstream.toXML(instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T fromXMLManifest(File manifestFile, Class<T> clazz) throws FileNotFoundException {
        return (T)xstream.fromXML(new FileReader(manifestFile));
    }

}
