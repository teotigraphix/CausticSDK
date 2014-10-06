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
import com.teotigraphix.caustk.core.ICaustkFactory;
import com.teotigraphix.caustk.core.ICaustkSerializer;
import com.teotigraphix.caustk.groove.importer.CausticEffect;
import com.teotigraphix.caustk.groove.importer.CausticGroup;
import com.teotigraphix.caustk.groove.importer.CausticInstrument;
import com.teotigraphix.caustk.groove.importer.CausticPatternBank;
import com.teotigraphix.caustk.groove.importer.CausticSound;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryItemFormat;
import com.teotigraphix.caustk.groove.library.LibraryPatternBank;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibraryProductItem;
import com.teotigraphix.caustk.groove.library.LibraryProject;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.groove.manifest.LibraryItemManifest;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.effect.EffectNode;
import com.teotigraphix.caustk.node.effect.EffectsChannel;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.utils.ZipCompress;
import com.teotigraphix.caustk.utils.ZipUncompress;

public class LibraryProductUtils {

    static final String PRODUCT_BIN = "product.bin";

    private static final String TEMP_EXTRACTION = ".tempExtraction";

    static final String MANIFEST_XML = "manifest.xml";

    static final String EFFECT_BIN = "effect.bin";

    static final String SOUND_EFFECT_DIR = "effect/";

    static final String SOUND_EFFECT_ARCHIVE = "effect.gfx";

    static final String PATTERNS_BIN = "patterns.bin";

    static final String SOUND_PATTERNS_DIR = "patterns/";

    static final String SOUND_PATTERNS_ARCHIVE = "patterns.gptbk";

    private static ICaustkSerializer getSerializer() {
        return CaustkRuntime.getInstance().getRack().getSerializer();
    }

    public static void addToDirectory(LibraryProduct product, File groupArchive)
            throws IOException, CausticException {
        File productDirectory = product.getDirectory();

        File groupsDirectory = new File(productDirectory, "Groups");
        File soundsDirectory = new File(productDirectory, "Sounds");
        File instrumentsDirectory = new File(productDirectory, "Instruments");
        File effectsDirectory = new File(productDirectory, "Effects");
        File patternsDirectory = new File(productDirectory, "PatternBanks");

        File uncompressDir = product.getCacheDirectory(TEMP_EXTRACTION);
        File uncompressSoundsDir = new File(uncompressDir, "sounds");
        LibraryGroup libraryGroup = LibraryGroupUtils.importGroup(groupArchive, uncompressDir);

        //------------------------------
        // Copy LibraryGroup.ggrp to Product directory
        File groupSrc = groupArchive;

        String groupPath = libraryGroup.getCalculatedPath();
        String groupName = libraryGroup.getFileName();
        File groupDest = new File(groupsDirectory, groupPath);

        // Save group to product directory
        FileUtils.copyFile(groupSrc, new File(groupDest, groupName));
        product.addItem(libraryGroup);

        //------------------------------
        for (LibrarySound librarySound : libraryGroup.getSounds()) {
            // SOUND
            int index = librarySound.getIndex();
            String soundPath = librarySound.getCalculatedPath();
            String soundName = librarySound.getName();
            File soundDest = new File(soundsDirectory, soundPath);
            File soundArchiveSrc = new File(uncompressSoundsDir, "sound-" + index + ".gsnd");

            // Save sound to product directory
            FileUtils.copyFile(soundArchiveSrc, new File(soundDest, soundName + ".gsnd"));
            product.addItem(librarySound);

            // INSTRUMENT
            LibraryInstrument libraryInstrument = librarySound.getInstrument();
            String instPath = libraryInstrument.getCalculatedPath();
            String instName = libraryInstrument.getName();

            File instDest = new File(instrumentsDirectory, instPath);
            File instArchiveSrc = new File(uncompressSoundsDir, "sound-" + index + File.separator
                    + "instrument.ginst");

            // Save instrument to product directory
            FileUtils.copyFile(instArchiveSrc, new File(instDest, instName + ".ginst"));
            product.addItem(libraryInstrument);

            // PATTERNS
            LibraryPatternBank libraryPatternBank = librarySound.getPatternBank();
            //String pbType = libraryPatternBank.getManifest().getMachineType().name();
            String pbPath = libraryPatternBank.getCalculatedPath();
            String pbName = libraryPatternBank.getName();

            File pbDest = new File(patternsDirectory, pbPath);
            File pbArchiveSrc = new File(uncompressSoundsDir, "sound-" + index + File.separator
                    + "patterns.gptbk");

            // Save patternBank to product directory
            FileUtils.copyFile(pbArchiveSrc, new File(pbDest, pbName + ".gptbk"));
            product.addItem(libraryPatternBank);

            // EFFECT
            LibraryEffect libraryEffect = librarySound.getEffect();
            String effectPath = libraryEffect.getCalculatedPath();
            String effectName = libraryEffect.getName();

            File effectDest = new File(effectsDirectory, effectPath);
            File effectArchiveSrc = new File(uncompressSoundsDir, "sound-" + index + File.separator
                    + "effect.gfx");

            // Save instrument to product directory
            FileUtils.copyFile(effectArchiveSrc, new File(effectDest, effectName + ".gfx"));
            product.addItem(libraryEffect);
        }
    }

    public static File saveItemAsArchive(LibraryProductItem item, LibraryProduct product,
            File zipFile) throws IOException {
        File tempDirectory = product.getCacheDirectory("extract");
        FileUtils.forceMkdir(tempDirectory);

        switch (item.getFormat()) {
            case Effect:
                LibraryEffectUtils.serialize((LibraryEffect)item, product, tempDirectory);
                break;

            case Instrument:
                LibraryInstrumentUtils.serialize((LibraryInstrument)item, product, tempDirectory);
                break;

            case PatternBank:
                LibraryPatternBankUtils.serialize((LibraryPatternBank)item, product, tempDirectory);
                break;

            case Sound:
                LibrarySoundUtils.serialize((LibrarySound)item, product, tempDirectory);
                break;

            case Group:
                LibraryGroupUtils.serialize((LibraryGroup)item, product, tempDirectory);
                break;

            case Product:
                break;

            case Project:
                break;

            case Sample:
                break;

        }

        ZipCompress compress = new ZipCompress(tempDirectory);
        compress.zip(zipFile);
        FileUtils.forceDeleteOnExit(tempDirectory);
        return zipFile;
    }

    public static File addArchiveToProduct(LibraryProductItem item, LibraryProduct product)
            throws IOException {
        File zipFile = product.resolveInternalArchive(item);
        return saveItemAsArchive(item, product, zipFile);
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

            case Instrument:
                addInstrumentArchive(archiveFile, product);
                break;

            case PatternBank:
                addPatternBankArchive(archiveFile, product);
                break;

            case Sound:
                addSoundArchive(archiveFile, product);
                break;

            case Group:
                addGroupArchive(archiveFile, product);
                break;

            case Product:
                break;

            case Project:
                addProjectArchive(archiveFile, product);
                break;

            case Sample:
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

    private static void addPatternBankArchive(File file, LibraryProduct product)
            throws CausticException {
        addToProduct(file, product, LibraryPatternBank.class);
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

    static CausticPatternBank createPatternBank(LibraryPatternBank item) {
        return new CausticPatternBank(item);
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
        return causticSound;
    }

    static CausticGroup createGroup(LibraryGroup item) {
        CausticGroup causticGroup = new CausticGroup(item);
        return causticGroup;
    }

    public static String toEffectXML(LibraryEffect item) {
        CausticEffect causticEffect = createEffect(item);
        String xml = getSerializer().getImporter().toXML(causticEffect);
        return xml;
    }

    public static String toInstrumentXML(LibraryInstrument item) {
        CausticInstrument causticInstrument = createInstrument(item);
        String xml = getSerializer().getImporter().toXML(causticInstrument);
        return xml;
    }

    public static String toPatternBankXML(LibraryPatternBank item) {
        CausticPatternBank causticPatternBank = createPatternBank(item);
        String xml = getSerializer().getImporter().toXML(causticPatternBank);
        return xml;
    }

    public static String toSoundXML(LibrarySound item) {
        CausticSound causticSound = createSound(item);
        String xml = getSerializer().getImporter().toXML(causticSound);
        return xml;
    }

    public static String toGroupXML(LibraryGroup item) {
        CausticGroup causticGroup = createGroup(item);
        String xml = getSerializer().getImporter().toXML(causticGroup);
        return xml;
    }

    private static void fillGroup(LibraryProduct product, LibraryGroup libraryGroup,
            File causticFile) throws CausticException {
        if (!causticFile.exists())
            throw new CausticException(".caustic File does not exist ;" + causticFile);

        RackNode rackNode = CaustkRuntime.getInstance().getRack().create(causticFile);

        for (MachineNode machineNode : rackNode.getMachines()) {
            int index = machineNode.getIndex();
            LibrarySound librarySound = fillSound(index, product, libraryGroup, machineNode);
            libraryGroup.addSound(machineNode.getIndex(), librarySound);
        }
    }

    private static LibrarySound fillSound(int index, LibraryProduct product,
            LibraryGroup libraryGroup, MachineNode machineNode) {
        String groupName = libraryGroup.getDisplayName();
        String displayName = groupName + "-" + machineNode.getName();
        String path = groupName;

        LibrarySound librarySound = getFactory().getLibraryFactory().createSound(product, index,
                displayName, path);

        LibraryEffect libraryEffect = fillEffect(machineNode.getEffects(), product,
                machineNode.getName(), groupName, librarySound);
        LibraryInstrument libraryInstrument = fillInstrument(machineNode, product, displayName,
                groupName);
        LibraryPatternBank libraryPatternBank = fillPatternBank(machineNode, product, displayName,
                groupName);

        librarySound.setEffect(libraryEffect);
        librarySound.setInstrument(libraryInstrument);
        librarySound.setPatternBank(libraryPatternBank);

        return librarySound;
    }

    private static LibraryEffect fillEffect(EffectsChannel effectsChannel, LibraryProduct product,
            String machineName, String groupName, LibrarySound librarySound) {
        String name = machineName + " FX";
        String relativePath = groupName;

        EffectNode efffect0 = effectsChannel.getEfffect(0);
        EffectNode efffect1 = effectsChannel.getEfffect(1);

        LibraryEffect libraryEffect = getFactory().getLibraryFactory().createEffect(product, name,
                relativePath, efffect0, efffect1);

        return libraryEffect;
    }

    private static LibraryInstrument fillInstrument(MachineNode machineNode,
            LibraryProduct product, String name, String groupName) {
        String relativePath = groupName;
        LibraryInstrument libraryInstrument = getFactory().getLibraryFactory().createInstrument(
                product, name, relativePath, machineNode);

        return libraryInstrument;
    }

    private static LibraryPatternBank fillPatternBank(MachineNode machineNode,
            LibraryProduct product, String name, String groupName) {
        String relativePath = groupName;
        LibraryPatternBank libraryPatternBank = getFactory().getLibraryFactory().createPatternBank(
                product, name, relativePath, machineNode);
        return libraryPatternBank;
    }

    public static LibraryGroup createGroupFromCausticFile(LibraryProduct product, String path,
            String displayName, File causticFile) throws CausticException {

        // - load .caustic into rack
        // - restore rack
        // - 

        LibraryGroup libraryGroup = product.createGroup(path, displayName, causticFile);
        fillGroup(product, libraryGroup, causticFile);

        return libraryGroup;
    }

    private static ICaustkFactory getFactory() {
        return CaustkRuntime.getInstance().getFactory();
    }
}
