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
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.utils.SerializeUtils;
import com.teotigraphix.caustk.utils.ZipCompress;
import com.teotigraphix.caustk.utils.ZipUncompress;

public class LibrarySoundUtils {

    private static final String INSTRUMENT_GINST = "instrument.ginst";

    private static final String EFFECT_GFX = "effect.gfx";

    private static final String SOUND_BIN = "sound.bin";

    public static void serialize(LibrarySound item, LibraryProduct product, File tempDirectory)
            throws IOException {

        FileUtils.writeStringToFile(new File(tempDirectory, LibraryProductUtils.MANIFEST_XML),
                LibraryProductUtils.toSoundXML(item));

        LibraryEffect effect = item.getEffect();
        LibraryInstrument instrument = item.getInstrument();

        File tempEffectDir = new File(tempDirectory, "effect");
        if (!tempEffectDir.exists())
            FileUtils.forceMkdir(tempEffectDir);
        File tempInstrumentDir = new File(tempDirectory, "instrument");
        if (!tempInstrumentDir.exists())
            FileUtils.forceMkdir(tempInstrumentDir);

        LibraryEffectUtils.serialize(effect, product, tempEffectDir);
        LibraryInstrumentUtils.serialize(instrument, product, tempInstrumentDir);

        ZipCompress compress = null;

        compress = new ZipCompress(tempEffectDir);
        compress.zip(new File(tempDirectory, EFFECT_GFX));

        compress = new ZipCompress(tempInstrumentDir);
        compress.zip(new File(tempDirectory, INSTRUMENT_GINST));

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FileUtils.forceDelete(tempEffectDir);
        FileUtils.forceDelete(tempInstrumentDir);

        SerializeUtils.pack(new File(tempDirectory, SOUND_BIN), item);
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

        LibraryEffect effect = LibraryEffectUtils.importEffectFromSoundDirectory(tempDirectory);
        librarySound.setEffect(effect);

        LibraryInstrument instrument = LibraryInstrumentUtils
                .importInstrumentFromSoundDirectory(tempDirectory);
        librarySound.setInstrument(instrument);
    }
}
