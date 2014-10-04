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
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.core.ICaustkSerializer;
import com.teotigraphix.caustk.groove.importer.CausticEffect;
import com.teotigraphix.caustk.groove.importer.CausticInstrument;
import com.teotigraphix.caustk.groove.importer.CausticSound;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryItemFormat;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibraryProductItem;
import com.teotigraphix.caustk.groove.library.LibraryProject;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.groove.manifest.LibraryItemManifest;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.caustk.utils.ZipCompress;
import com.teotigraphix.caustk.utils.ZipUncompress;

public class LibraryProductUtils {

    static final String MANIFEST_XML = "manifest.xml";

    static final String EFFECT_BIN = "effect.bin";

    static final String SOUND_EFFECT_DIR = "effect/";

    static final String SOUND_EFFECT_ARCHIVE = "effect.gfx";

    private static ICaustkSerializer getSerializer() {
        return CaustkRuntime.getInstance().getRack().getSerializer();
    }

    public static File addArchiveToProduct(LibraryProductItem item, LibraryProduct product)
            throws IOException {
        // save the manifest in the correct folder of the product, no zip archive
        File tempDirectory = RuntimeUtils.getApplicationTempDirectory();
        String name = item.getFormat().name();
        tempDirectory = new File(tempDirectory, "__" + name + "__");
        if (tempDirectory.exists())
            FileUtils.cleanDirectory(tempDirectory);
        else
            FileUtils.forceMkdir(tempDirectory);

        switch (item.getFormat()) {
            case Effect:
                LibraryEffectUtils.serialize((LibraryEffect)item, product, tempDirectory);
                break;
            case Group:
                LibraryGroupUtils.serialize((LibraryGroup)item, product, tempDirectory);
                break;
            case Instrument:
                LibraryInstrumentUtils.serialize((LibraryInstrument)item, product, tempDirectory);
                break;
            case Product:
                break;
            case Project:
                break;
            case Sample:
                break;
            case Sound:
                LibrarySoundUtils.serialize((LibrarySound)item, product, tempDirectory);
                break;
        }

        File zipFile = product.resolveInternalArchive(item);
        ZipCompress compress = new ZipCompress(tempDirectory);
        compress.zip(zipFile);
        return zipFile;
    }

    public static void addToProduct(File archiveFile, LibraryProduct product, Class<?> clazz)
            throws CausticException {
        // XXX FIX readZipString()
        //        final String json = ZipUtils.readZipString(archiveFile, new File(MANIFEST_JSON));
        //        final LibraryProductItem libraryItem = SerializeUtils.unpack(json, clazz);
        //        product.addItem(libraryItem);
    }

    public static String toItemBaseDirectoryName(LibraryItemManifest manifest) {
        String name = manifest.getFormat().name();
        return name + "s";
    }

    public static String toItemBaseDirectoryName(LibraryProductItem item) {
        String name = item.getFormat().name();
        return name + "s";
    }

    //--------------------------------------------------------------------------

    public static void addLibraryItemArchive(File archiveFile, LibraryProduct product)
            throws IOException, CausticException {
        String extension = FilenameUtils.getExtension(archiveFile.getName());

        LibraryItemFormat format = LibraryItemFormat.fromString(extension);
        if (format == null) {
            System.err.println(extension);
            return;
        }

        switch (format) {
            case Effect:
                addEffectArchive(archiveFile, product);
                break;
            case Group:
                addGroupArchive(archiveFile, product);
                break;
            case Instrument:
                addInstrumentArchive(archiveFile, product);
                break;
            case Product:
                break;
            case Project:
                addProjectArchive(archiveFile, product);
                break;
            case Sample:
                break;
            case Sound:
                addSoundArchive(archiveFile, product);
                break;
        }
    }

    private static void addGroupArchive(File file, LibraryProduct product) throws CausticException {
        addToProduct(file, product, LibraryGroup.class);
    }

    private static void addInstrumentArchive(File file, LibraryProduct product)
            throws CausticException {
        addToProduct(file, product, LibraryInstrument.class);
    }

    private static void addProjectArchive(File file, LibraryProduct product)
            throws CausticException {
        addToProduct(file, product, LibraryProject.class);
    }

    private static void addSoundArchive(File file, LibraryProduct product) throws CausticException {
        addToProduct(file, product, LibrarySound.class);
    }

    private static void addEffectArchive(File file, LibraryProduct product) throws IOException,
            CausticException {
        addToProduct(file, product, LibraryEffect.class);
    }

    public static <T> T readXMLManifest(LibraryProduct product, File archive, Class<T> clazz)
            throws IOException {
        if (!archive.exists())
            throw new IOException("Archive does not exist: " + archive);
        ZipUncompress uncompress = new ZipUncompress(archive);
        File uncompressDirectory = product.getCacheDirectory(UUID.randomUUID().toString());
        uncompress.unzip(uncompressDirectory);
        File manifestFile = new File(uncompressDirectory, MANIFEST_XML);
        T instance = getSerializer().fromXMLManifest(manifestFile, clazz);
        FileUtils.forceDeleteOnExit(uncompressDirectory);
        return instance;
    }

    /*
     * <effect displayName="Foo Effect">
     *   <type index="0" type="Autowah"/>
     *   <type index="1" type="Delay"/>
     * </effect>
     */
    static CausticEffect createEffect(LibraryEffect item) {
        return new CausticEffect(item);
    }

    /*
     * <instrument displayName="Foo Instrument" type="SubSynth"/>
     */
    static CausticInstrument createInstrument(LibraryInstrument item) {
        CausticInstrument causticInstrument = new CausticInstrument(item);
        return causticInstrument;
    }

    /*
     * <sound displayName="Ride" index="3" path="Drum/Percussion">
     *   <instrument displayName="Foo Instrument" type="SubSynth"/>
     *   <effect displayName="Foo Effect">
     *     <type index="0" type="Autowah"/>
     *     <type index="1" type="Delay"/>
     *   </effect>
     * </sound>
     */
    static CausticSound createSound(LibrarySound item) {
        CausticSound causticSound = new CausticSound(item);
        causticSound.setInstrument(createInstrument(item.getInstrument()));
        causticSound.setEffect(createEffect(item.getEffect()));
        return causticSound;
    }

    public static String toEffectXML(LibraryEffect item) {
        CausticEffect causticEffect = createEffect(item);
        String xml = getSerializer().toXML(causticEffect);
        return xml;
    }

    public static String toInstrumentXML(LibraryInstrument item) {
        CausticInstrument causticInstrument = createInstrument(item);
        String xml = getSerializer().toXML(causticInstrument);
        return xml;
    }

    public static String toSoundXML(LibrarySound item) {
        CausticSound causticSound = createSound(item);
        String xml = getSerializer().toXML(causticSound);
        return xml;
    }

}
