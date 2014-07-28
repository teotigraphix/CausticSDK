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
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.utils.ZipCompress;
import com.teotigraphix.caustk.utils.ZipUncompress;

public class LibrarySoundUtils {

    private static final String DIR_TEMP_EFFECT = "C:\\Users\\Teoti\\Desktop\\TempEffect";

    private static final String DIR_TEMP_INSTRUMENT = "C:\\Users\\Teoti\\Desktop\\TempInstrument";

    private static final String DIR_TEMP_SOUND = "C:\\Users\\Teoti\\Desktop\\TempSound";

    //    public static LibrarySound __createSound(LibraryProduct product, String soundNameWithExtension,
    //            String groupName, MachineNode machineNode, File targetDirectory,
    //            CausticSound causticSound) throws IOException {
    //
    //        File effectDirectory = new File(DIR_TEMP_EFFECT);
    //        effectDirectory.mkdirs();
    //
    //        File instrumentDirectory = new File(DIR_TEMP_INSTRUMENT);
    //        instrumentDirectory.mkdirs();
    //
    //        File soundDirectory = new File(DIR_TEMP_SOUND);
    //        soundDirectory.mkdirs();
    //
    //        // create Effect
    //        // /TempSound/effects/effect-[i].gfx
    //        LibraryEffect effect = LibraryEffectUtils.createEffect(product, machineNode, causticSound);
    //
    //        // create Instrument
    //        LibraryInstrument instrument = LibraryInstrumentUtils.createInstrument(product,
    //                machineNode, causticSound);
    //
    //        // 
    //        String name = groupName + "-" + machineNode.getName();
    //        if (causticSound != null)
    //            name = causticSound.getDisplayName();
    //
    //        CaustkFactory factory = CaustkRuntime.getInstance().getFactory();
    //
    //        LibrarySound sound = factory.createLibrarySound(product, name, "");
    //
    //        //        LibraryEffectUtils.saveEffect(effect, effectDirectory,
    //        //                LibraryEffectUtils.toEffectFile(soundDirectory));
    //
    //        LibraryInstrumentUtils.saveInstrument(instrument, instrumentDirectory,
    //                LibraryInstrumentUtils.toInstrumentFile(soundDirectory));
    //
    //        sound.setEffect(effect);
    //        sound.setInstrument(instrument);
    //
    //        LibrarySoundUtils._saveSound(sound, soundDirectory, new File(targetDirectory,
    //                soundNameWithExtension));
    //
    //        try {
    //            Thread.sleep(200);
    //        } catch (InterruptedException e) {
    //            e.printStackTrace();
    //        }
    //
    //        FileUtils.deleteDirectory(soundDirectory);
    //        FileUtils.deleteDirectory(instrumentDirectory);
    //        return sound;
    //    }

    public static void saveSound(LibrarySound item, LibraryProduct product, File tempDirectory)
            throws IOException {

        LibraryEffect effect = item.getEffect();
        LibraryInstrument instrument = item.getInstrument();

        File tempEffectDir = new File(tempDirectory, "effect");
        File tempInstrumentDir = new File(tempDirectory, "instrument");

        LibraryEffectUtils.saveEffect(effect, product, tempEffectDir);
        LibraryInstrumentUtils.saveInstrument(instrument, product, tempInstrumentDir);

        ZipCompress compress = null;

        compress = new ZipCompress(tempEffectDir);
        compress.zip(new File(tempDirectory, "effect.gfx"));

        compress = new ZipCompress(tempInstrumentDir);
        compress.zip(new File(tempDirectory, "instrument.gfx"));

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FileUtils.forceDelete(tempEffectDir);
        FileUtils.forceDelete(tempInstrumentDir);

        String json = CaustkRuntime.getInstance().getFactory().serialize(item, true);
        FileUtils.write(new File(tempDirectory, "manifest.json"), json);
    }

    public static void _saveSound(LibrarySound sound, File sourceDirectory, File zipFile)
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
