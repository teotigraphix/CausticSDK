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

package com.teotigraphix.caustk.groove.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.groove.importer.CausticGroup;
import com.teotigraphix.caustk.groove.importer.CausticSound;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.utils.SerializeUtils;
import com.teotigraphix.caustk.utils.ZipCompress;
import com.teotigraphix.caustk.utils.ZipUncompress;
import com.thoughtworks.xstream.XStream;

public class LibraryGroupUtils {

    private static final String GROUP_BIN = "group.bin";

    public static void configureXStream(XStream xstream) {
        xstream.alias("group", CausticGroup.class);
        xstream.addImplicitMap(CausticGroup.class, "sounds", CausticSound.class, "index");
        xstream.useAttributeFor(CausticGroup.class, "path");
        xstream.useAttributeFor(CausticGroup.class, "sourceFile");
    }

    public static void serialize(LibraryGroup item, LibraryProduct product, File tempDirectory)
            throws IOException {

        FileUtils.writeStringToFile(new File(tempDirectory, LibraryProductUtils.MANIFEST_XML),
                LibraryProductUtils.toGroupXML(item));

        for (LibrarySound librarySound : item.getSounds()) {
            File tempSoundDir = new File(tempDirectory, "sounds/sound-" + librarySound.getIndex());
            LibrarySoundUtils.serialize(librarySound, product, tempSoundDir);

            ZipCompress compress = new ZipCompress(tempSoundDir);
            File zipFile = new File(tempDirectory, "sounds/sound-" + librarySound.getIndex()
                    + ".gsnd");
            compress.zip(zipFile);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            FileUtils.forceDelete(tempSoundDir);
        }

        SerializeUtils.pack(new File(tempDirectory, GROUP_BIN), item);
    }

    public static LibraryGroup importGroup(File sourceFile, File uncompressDirectory)
            throws CausticException, IOException {
        ZipUncompress uncompress = new ZipUncompress(sourceFile);
        uncompress.unzip(uncompressDirectory);

        File manifest = new File(uncompressDirectory, GROUP_BIN);
        if (!manifest.exists())
            throw new CausticException(GROUP_BIN + " does not exist");

        LibraryGroup libraryGroup = SerializeUtils.unpack(manifest, LibraryGroup.class);

        for (LibrarySound librarySound : libraryGroup.getSounds()) {
            librarySound.setGroup(libraryGroup);
            LibrarySoundUtils.importSoundFromGroupDirectory(librarySound, uncompressDirectory);
        }

        return libraryGroup;
    }
}
