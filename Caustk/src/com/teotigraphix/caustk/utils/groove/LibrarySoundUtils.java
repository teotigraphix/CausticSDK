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

package com.teotigraphix.caustk.utils.groove;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.groove.importer.CausticSound;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryPatternBank;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.utils.core.SerializeUtils;
import com.teotigraphix.caustk.utils.core.ZipCompress;
import com.teotigraphix.caustk.utils.core.ZipUncompress;
import com.thoughtworks.xstream.XStream;

public class LibrarySoundUtils {

    private static final String INSTRUMENT_GINST = "instrument.ginst";

    private static final String PATTERNS_GPTBK = "patterns.gptbk";

    private static final String EFFECT_GFX = "effect.gfx";

    private static final String SOUND_BIN = "sound.bin";

    public static void configureXStream(XStream xstream) {
        xstream.alias("sound", CausticSound.class);
        xstream.useAttributeFor(CausticSound.class, "path");
        xstream.useAttributeFor(CausticSound.class, "index");
    }

    public static void serialize(LibrarySound item, LibraryProduct product, File tempDirectory)
            throws IOException {

        FileUtils.writeStringToFile(new File(tempDirectory, LibraryProductUtils.MANIFEST_XML),
                LibraryProductUtils.toSoundXML(item));

        LibraryEffect effect = item.getEffect();
        LibraryInstrument instrument = item.getInstrument();
        LibraryPatternBank patternBank = item.getPatternBank();

        File tempEffectDir = new File(tempDirectory, "effect");
        if (!tempEffectDir.exists())
            FileUtils.forceMkdir(tempEffectDir);
        File tempInstrumentDir = new File(tempDirectory, "instrument");
        if (!tempInstrumentDir.exists())
            FileUtils.forceMkdir(tempInstrumentDir);
        File tempPatternDir = new File(tempDirectory, "pattern");
        if (!tempPatternDir.exists())
            FileUtils.forceMkdir(tempPatternDir);

        LibraryEffectUtils.serialize(effect, product, tempEffectDir);
        LibraryInstrumentUtils.serialize(instrument, product, tempInstrumentDir);
        LibraryPatternBankUtils.serialize(patternBank, product, tempPatternDir);

        ZipCompress compress = null;

        compress = new ZipCompress(tempEffectDir);
        compress.zip(new File(tempDirectory, EFFECT_GFX));

        compress = new ZipCompress(tempInstrumentDir);
        compress.zip(new File(tempDirectory, INSTRUMENT_GINST));

        compress = new ZipCompress(tempPatternDir);
        compress.zip(new File(tempDirectory, PATTERNS_GPTBK));

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FileUtils.forceDelete(tempEffectDir);
        FileUtils.forceDelete(tempInstrumentDir);
        FileUtils.forceDelete(tempPatternDir);

        SerializeUtils.pack(new File(tempDirectory, SOUND_BIN), item);
    }

    public static void importSoundFromGroupDirectory(LibrarySound librarySound, File groupDirectory)
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

        LibraryPatternBank patternBank = LibraryPatternBankUtils
                .importPatternBankFromSoundDirectory(tempDirectory);
        librarySound.setPatternBank(patternBank);
    }

    public static LibrarySound importSound(File uncompressDirectory, File soundFile)
            throws CausticException, IOException {
        ZipUncompress uncompress = new ZipUncompress(soundFile);
        uncompress.unzip(uncompressDirectory);

        LibraryEffect libraryEffect = LibraryEffectUtils
                .importEffectFromSoundDirectory(uncompressDirectory);
        LibraryInstrument libraryInstrument = LibraryInstrumentUtils
                .importInstrumentFromSoundDirectory(uncompressDirectory);
        LibraryPatternBank patternBank = LibraryPatternBankUtils
                .importPatternBankFromSoundDirectory(uncompressDirectory);

        File manifest = new File(uncompressDirectory, "sound.bin");
        if (!manifest.exists())
            throw new CausticException("sound.bin" + " does not exist");

        LibrarySound librarySound = SerializeUtils.unpack(manifest, LibrarySound.class);
        librarySound.setEffect(libraryEffect);
        librarySound.setInstrument(libraryInstrument);
        librarySound.setPatternBank(patternBank);

        return librarySound;
    }
}
