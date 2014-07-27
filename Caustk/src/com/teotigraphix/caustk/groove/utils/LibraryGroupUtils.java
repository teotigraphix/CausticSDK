
package com.teotigraphix.caustk.groove.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.groove.importer.CausticGroup;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.groove.manifest.LibraryGroupManifest;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.utils.ZipCompress;
import com.teotigraphix.caustk.utils.ZipUncompress;

public class LibraryGroupUtils {

    public static final File DIR_TEMP_GROUP = new File("C:\\Users\\Teoti\\Desktop\\__Group__");

    public static LibraryGroup exportGroup(LibraryProduct product, File causticFile,
            LibraryGroupManifest manifest, CausticGroup causticGroup) throws IOException,
            CausticException {

        RackNode rackNode = CaustkRuntime.getInstance().getRack().create(causticFile);

        File tempGroupDirectory = DIR_TEMP_GROUP;
        tempGroupDirectory.mkdirs();

        //------------------------------

        String groupDisplayName = manifest.getDisplayName();
        File groupArchive = manifest.getArchiveFile(); // .ggrp
        @SuppressWarnings("unused")
        String relativePath = "";

        //------------------------------

        // create Group
        LibraryGroup group = new LibraryGroup(UUID.randomUUID(), product.getId(), manifest);

        for (MachineNode machineNode : rackNode.getMachines()) {
            int index = machineNode.getIndex();
            File tempSoundsDir = new File(tempGroupDirectory, "sounds");
            // create Sound 
            String soundName = "sound-" + Integer.toString(machineNode.getIndex()) + ".gsnd";
            LibrarySound sound = LibrarySoundUtils.createSound(product, soundName,
                    groupDisplayName, machineNode, tempSoundsDir,
                    causticGroup.getSounds().get(index));
            group.addSound(index, sound);
        }

        saveGroup(group, tempGroupDirectory, groupArchive);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FileUtils.deleteDirectory(tempGroupDirectory);

        return group;
    }

    public static void saveGroup(LibraryGroup group, File sourceDirectory, File zipFile)
            throws IOException {
        String json = CaustkRuntime.getInstance().getFactory().serialize(group, true);
        FileUtils.write(new File(sourceDirectory, "manifest.json"), json);
        ZipCompress compress = new ZipCompress(sourceDirectory);
        compress.zip(zipFile);

    }

    public static LibraryGroup importGroup(File sourceFile) throws CausticException, IOException {
        File tempDirectory = LibraryGroupUtils.DIR_TEMP_GROUP;
        tempDirectory.mkdirs();

        ZipUncompress uncompress = new ZipUncompress(sourceFile);
        uncompress.unzip(tempDirectory);

        File manifest = new File(tempDirectory, "manifest.json");
        if (!manifest.exists())
            throw new CausticException("manifest does not exist");

        String json = FileUtils.readFileToString(manifest);
        LibraryGroup libraryGroup = CaustkRuntime.getInstance().getFactory()
                ._deserialize(json, LibraryGroup.class);

        for (LibrarySound librarySound : libraryGroup.getSounds()) {
            librarySound.setGroup(libraryGroup);
            LibrarySoundUtils.importSound(librarySound, tempDirectory);
        }

        return libraryGroup;
    }
}
