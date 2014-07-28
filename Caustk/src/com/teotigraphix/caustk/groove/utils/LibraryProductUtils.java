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
import org.apache.commons.io.FilenameUtils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryItemFormat;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibraryProductItem;
import com.teotigraphix.caustk.groove.library.LibraryProject;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.caustk.utils.SerializeUtils;
import com.teotigraphix.caustk.utils.ZipCompress;
import com.teotigraphix.caustk.utils.ZipUtils;

public class LibraryProductUtils {

    private static final String MANIFEST_JSON = "manifest.json";

    public static File addArchiveToProduct(LibraryProductItem item, LibraryProduct product)
            throws IOException {
        // save the manifest in the correct folder of the product, no zip archive
        File tempDirectory = RuntimeUtils.getApplicationTempDirectory();
        String name = item.getFormat().name();
        tempDirectory = new File(tempDirectory, "__" + name + "__");
        if (tempDirectory.exists())
            FileUtils.cleanDirectory(tempDirectory);

        switch (item.getFormat()) {
            case Effect:
                LibraryEffectUtils.saveEffect(item, product, tempDirectory);
                break;
            case Group:
                LibraryGroupUtils.saveGroup((LibraryGroup)item, product, tempDirectory);
                break;
            case Instrument:
                LibraryInstrumentUtils.saveInstrument(item, product, tempDirectory);
                break;
            case Product:
                break;
            case Project:
                break;
            case Sample:
                break;
            case Sound:
                LibrarySoundUtils.saveSound((LibrarySound)item, product, tempDirectory);
                break;
        }

        File zipFile = product.resolveInternalArchive(item);
        ZipCompress compress = new ZipCompress(tempDirectory);
        compress.zip(zipFile);
        return zipFile;
    }

    public static void addToProduct(File archiveFile, LibraryProduct product, Class<?> clazz)
            throws CausticException {
        final String json = ZipUtils.readZipString(archiveFile, new File(MANIFEST_JSON));
        final LibraryProductItem libraryItem = SerializeUtils.unpack(json, clazz);
        product.addItem(libraryItem);
    }

    public static String toItemBaseDirectoryName(LibraryProductItem item) {
        String name = item.getFormat().name();
        return name + "s";
    }

    //--------------------------------------------------------------------------

    public static void parse(File file, LibraryProduct product) throws IOException,
            CausticException {
        String extension = FilenameUtils.getExtension(file.getName());

        LibraryItemFormat format = LibraryItemFormat.fromString(extension);
        if (format == null) {
            System.err.println(extension);
            return;
        }

        switch (format) {
            case Effect:
                parseEffect(file, product);
                break;
            case Group:
                parseGroup(file, product);
                break;
            case Instrument:
                parseInstrument(file, product);
                break;
            case Product:
                break;
            case Project:
                parseProject(file, product);
                break;
            case Sample:
                break;
            case Sound:
                parseSound(file, product);
                break;
        }
    }

    private static void parseGroup(File file, LibraryProduct product) throws CausticException {
        addToProduct(file, product, LibraryGroup.class);
    }

    private static void parseInstrument(File file, LibraryProduct product) throws CausticException {
        addToProduct(file, product, LibraryInstrument.class);
    }

    private static void parseProject(File file, LibraryProduct product) throws CausticException {
        addToProduct(file, product, LibraryProject.class);
    }

    private static void parseSound(File file, LibraryProduct product) throws CausticException {
        addToProduct(file, product, LibrarySound.class);
    }

    private static void parseEffect(File file, LibraryProduct product) throws IOException,
            CausticException {
        addToProduct(file, product, LibraryEffect.class);
    }

}
