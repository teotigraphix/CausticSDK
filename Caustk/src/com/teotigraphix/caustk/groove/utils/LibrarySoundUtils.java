
package com.teotigraphix.caustk.groove.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.groove.importer.CausticSound;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.groove.manifest.LibrarySoundManifest;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.utils.ZipCompress;
import com.teotigraphix.caustk.utils.ZipUncompress;

public class LibrarySoundUtils {

    private static final String DIR_TEMP_EFFECT = "C:\\Users\\Teoti\\Desktop\\TempEffect";

    private static final String DIR_TEMP_INSTRUMENT = "C:\\Users\\Teoti\\Desktop\\TempInstrument";

    private static final String DIR_TEMP_SOUND = "C:\\Users\\Teoti\\Desktop\\TempSound";

    public static LibrarySound createSound(LibraryProduct product, String soundNameWithExtension,
            String groupName, MachineNode machineNode, File targetDirectory,
            CausticSound causticSound) throws IOException {

        File effectDirectory = new File(DIR_TEMP_EFFECT);
        effectDirectory.mkdirs();

        File instrumentDirectory = new File(DIR_TEMP_INSTRUMENT);
        instrumentDirectory.mkdirs();

        File soundDirectory = new File(DIR_TEMP_SOUND);
        soundDirectory.mkdirs();

        // create Effect
        // /TempSound/effects/effect-[i].gfx
        LibraryEffect effect = LibraryEffectUtils.createEffect(product, machineNode, causticSound);

        // create Instrument
        LibraryInstrument instrument = LibraryInstrumentUtils.createInstrument(product,
                machineNode, causticSound);

        // 
        String displayName = groupName + "-" + machineNode.getName();
        if (causticSound != null)
            displayName = causticSound.getDisplayName();

        LibrarySoundManifest manifest = new LibrarySoundManifest(displayName, null, "");
        LibrarySound sound = new LibrarySound(product.getId(), manifest);

        LibraryEffectUtils.saveEffect(effect, effectDirectory,
                LibraryEffectUtils.toEffectFile(soundDirectory));

        LibraryInstrumentUtils.saveInstrument(instrument, instrumentDirectory,
                LibraryInstrumentUtils.toInstrumentFile(soundDirectory));

        sound.setEffect(effect);
        sound.setInstrument(instrument);

        LibrarySoundUtils.saveSound(sound, soundDirectory, new File(targetDirectory,
                soundNameWithExtension));

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FileUtils.deleteDirectory(soundDirectory);
        FileUtils.deleteDirectory(instrumentDirectory);
        return sound;
    }

    public static void saveSound(LibrarySound sound, File sourceDirectory, File zipFile)
            throws IOException {
        String json = CaustkRuntime.getInstance().getFactory().serialize(sound, true);
        FileUtils.write(new File(sourceDirectory, "manifest.json"), json);
        ZipCompress compress = new ZipCompress(sourceDirectory);
        compress.zip(zipFile);
    }

    public static void importSound(LibrarySound librarySound, File groupDirectory)
            throws CausticException, IOException {
        String fileName = "sound-" + Integer.toString(librarySound.getIndex());
        File tempDirectory = new File(groupDirectory, "sounds/" + fileName);
        tempDirectory.mkdirs();
        File soundsDirectory = new File(groupDirectory, "sounds");
        File soundFile = new File(soundsDirectory, fileName + ".gsnd");

        ZipUncompress uncompress = new ZipUncompress(soundFile);
        uncompress.unzip(tempDirectory);

        //        File manifest = new File(tempDirectory, "manifest.json");
        //        if (!manifest.exists())
        //            throw new CausticException("manifest does not exist");
        //        
        //        String json = FileUtils.readFileToString(manifest);
        //        LibrarySound librarySound = CaustkRuntime.getInstance().getFactory()
        //                ._deserialize(json, LibrarySound.class);

        LibraryEffect effect = LibraryEffectUtils.importEffect(tempDirectory);
        librarySound.setEffect(effect);

        LibraryInstrument instrument = LibraryInstrumentUtils.importInstrument(tempDirectory);
        librarySound.setInstrument(instrument);
    }
}
