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

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.core.ICaustkRackSerializer;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibraryProductItem;
import com.teotigraphix.caustk.utils.SerializeUtils;
import com.teotigraphix.caustk.utils.ZipUncompress;

public class LibraryEffectUtils {

    private static final String EFFECT_BIN = "effect.bin";

    private static final String SOUND_EFFECT_DIRECTORY = "effect/";

    private static final String SOUND_EFFECT_ARCHIVE = "effect.gfx";

    private static ICaustkRackSerializer getSerializer() {
        return CaustkRuntime.getInstance().getRack().getSerializer();
    }

    public static void serialize(LibraryProductItem item, LibraryProduct product, File tempDirectory)
            throws IOException {
        getSerializer().serialize(new File(tempDirectory, EFFECT_BIN), item);
    }

    public static LibraryEffect importEffectFromSoundDirectory(File soundDirectory)
            throws CausticException, IOException {
        File uncompressDirectory = new File(soundDirectory, SOUND_EFFECT_DIRECTORY);
        File effectFile = new File(soundDirectory, SOUND_EFFECT_ARCHIVE);
        return importEffect(uncompressDirectory, effectFile);
    }

    /**
     * Deserializes a {@link LibraryEffect} from a <code>.gfx</code> file.
     * 
     * @param uncompressDirectory The directory to uncompress the effect
     *            archive.
     * @param effectFile The absolute location to the effect archive.
     * @return The deserialized {@link LibraryEffect}.
     * @throws CausticException
     * @throws IOException
     */
    public static LibraryEffect importEffect(File uncompressDirectory, File effectFile)
            throws CausticException, IOException {
        ZipUncompress uncompress = new ZipUncompress(effectFile);
        uncompress.unzip(uncompressDirectory);

        File manifest = new File(uncompressDirectory, EFFECT_BIN);
        if (!manifest.exists())
            throw new CausticException(EFFECT_BIN + " does not exist");

        LibraryEffect libraryEffect = SerializeUtils.unpack(manifest, LibraryEffect.class);
        return libraryEffect;
    }
}
