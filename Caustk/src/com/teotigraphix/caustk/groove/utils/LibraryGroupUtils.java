
package com.teotigraphix.caustk.groove.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CaustkFactory;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.groove.importer.CausticGroup;
import com.teotigraphix.caustk.groove.importer.CausticSound;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.effect.EffectNode;
import com.teotigraphix.caustk.node.effect.EffectsChannel;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.utils.ZipCompress;
import com.teotigraphix.caustk.utils.ZipUncompress;

public class LibraryGroupUtils {

    public static final File DIR_TEMP_GROUP = new File("C:\\Users\\Teoti\\Desktop\\__Group__");

    public static void fillGroup(LibraryProduct product, LibraryGroup libraryGroup)
            throws CausticException {

        File causticFile = libraryGroup.getCausticGroup().getSourceFile();
        RackNode rackNode = CaustkRuntime.getInstance().getRack().create(causticFile);

        for (MachineNode machineNode : rackNode.getMachines()) {

            LibrarySound librarySound = createSound(product, libraryGroup, machineNode);
            libraryGroup.addSound(machineNode.getIndex(), librarySound);
        }

    }

    private static LibrarySound createSound(LibraryProduct product, LibraryGroup libraryGroup,
            MachineNode machineNode) {
        CausticGroup causticGroup = libraryGroup.getCausticGroup();
        CausticSound causticSound = libraryGroup.getCausticGroup().getSounds()
                .get(machineNode.getIndex());

        String name = causticGroup.getDisplayName() + "-" + machineNode.getName();
        String relativePath = "";
        if (causticSound != null) {
            name = causticSound.getDisplayName();
            relativePath = causticSound.getPath();
        }

        LibraryEffect libraryEffect = createEffect(machineNode.getEffects(), product, name
                + " Effect");
        LibraryInstrument libraryInstrument = createInstrument(machineNode, product, name);

        LibrarySound librarySound = getFactory().createLibrarySound(product, name, relativePath);

        librarySound.setEffect(libraryEffect);
        librarySound.setInstrument(libraryInstrument);

        return librarySound;
    }

    private static LibraryEffect createEffect(EffectsChannel effectsChannel,
            LibraryProduct product, String name) {
        String relativePath = "";
        EffectNode efffect0 = effectsChannel.getEfffect(0);
        EffectNode efffect1 = effectsChannel.getEfffect(1);

        LibraryEffect libraryEffect = getFactory().createLibraryEffect(product, name, relativePath,
                efffect0, efffect1);

        return libraryEffect;
    }

    private static LibraryInstrument createInstrument(MachineNode machineNode,
            LibraryProduct product, String name) {
        String relativePath = "";
        LibraryInstrument libraryInstrument = getFactory().createLibraryInstrument(product, name,
                relativePath, machineNode);

        return libraryInstrument;
    }

    private static CaustkFactory getFactory() {
        return CaustkRuntime.getInstance().getFactory();
    }

    //--------------------------------------------------------------------------

    //    public static LibraryGroup __exportGroup(LibraryProduct product, File causticFile,
    //            File archiveFile, CausticGroup _causticGroup) throws IOException, CausticException {
    //
    //        RackNode rackNode = CaustkRuntime.getInstance().getRack().create(causticFile);
    //
    //        File tempGroupDirectory = DIR_TEMP_GROUP;
    //        tempGroupDirectory.mkdirs();
    //
    //        //------------------------------
    //
    //        String name = causticGroup.getDisplayName();
    //        String relativePath = "";
    //
    //        //------------------------------
    //
    //        CaustkFactory factory = CaustkRuntime.getInstance().getFactory();
    //        LibraryGroup group = factory.createLibraryGroup(product, name, relativePath);
    //
    //        for (MachineNode machineNode : rackNode.getMachines()) {
    //            int index = machineNode.getIndex();
    //            File tempSoundsDir = new File(tempGroupDirectory, "sounds");
    //            // create Sound 
    //            String soundName = "sound-" + Integer.toString(machineNode.getIndex()) + ".gsnd";
    //            LibrarySound sound = LibrarySoundUtils.createSound(product, soundName, name,
    //                    machineNode, tempSoundsDir, causticGroup.getSounds().get(index));
    //            group.addSound(index, sound);
    //        }
    //
    //        _saveGroup(group, tempGroupDirectory, archiveFile);
    //
    //        try {
    //            Thread.sleep(100);
    //        } catch (InterruptedException e) {
    //            e.printStackTrace();
    //        }
    //
    //        FileUtils.deleteDirectory(tempGroupDirectory);
    //
    //        return group;
    //    }

    public static void saveGroup(LibraryGroup item, LibraryProduct product, File tempDirectory)
            throws IOException {

        for (LibrarySound librarySound : item.getSounds()) {
            File tempSoundDir = new File(tempDirectory, "sounds/sound-" + librarySound.getIndex());
            LibrarySoundUtils.saveSound(librarySound, product, tempSoundDir);

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

        String json = CaustkRuntime.getInstance().getFactory().serialize(item, true);
        FileUtils.write(new File(tempDirectory, "manifest.json"), json);
    }

    public static void _saveGroup(LibraryGroup group, File sourceDirectory, File zipFile)
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
