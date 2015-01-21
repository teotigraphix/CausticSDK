
package com.teotigraphix.caustk.groove.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.ICaustkSerializer;
import com.teotigraphix.caustk.core.internal.CaustkRuntime;
import com.teotigraphix.caustk.groove.importer.CausticPatternBank;
import com.teotigraphix.caustk.groove.library.LibraryPatternBank;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.utils.SerializeUtils;
import com.teotigraphix.caustk.utils.ZipUncompress;
import com.thoughtworks.xstream.XStream;

public final class LibraryPatternBankUtils {

    private static ICaustkSerializer getSerializer() {
        return CaustkRuntime.getInstance().getRack().getSerializer();
    }

    public static void configureXStream(XStream xstream) {
        xstream.alias("patterns", CausticPatternBank.class);
    }

    public static void serialize(LibraryPatternBank item, LibraryProduct product, File tempDirectory)
            throws IOException {
        FileUtils.writeStringToFile(new File(tempDirectory, LibraryProductUtils.MANIFEST_XML),
                LibraryProductUtils.toPatternBankXML(item));
        getSerializer().serialize(new File(tempDirectory, LibraryProductUtils.PATTERNS_BIN), item);
    }

    public static LibraryPatternBank importPatternBankFromSoundDirectory(File soundDirectory)
            throws CausticException, IOException {
        File uncompressDirectory = new File(soundDirectory, LibraryProductUtils.SOUND_PATTERNS_DIR);
        File patternsFile = new File(soundDirectory, LibraryProductUtils.SOUND_PATTERNS_ARCHIVE);
        return importPatternBank(uncompressDirectory, patternsFile);
    }

    public static LibraryPatternBank importPatternBank(File uncompressDirectory, File patternFile)
            throws CausticException, IOException {
        ZipUncompress uncompress = new ZipUncompress(patternFile);
        uncompress.unzip(uncompressDirectory);

        File manifest = new File(uncompressDirectory, LibraryProductUtils.PATTERNS_BIN);
        if (!manifest.exists())
            throw new CausticException(LibraryProductUtils.PATTERNS_BIN + " does not exist");

        LibraryPatternBank instance = SerializeUtils.unpack(manifest, LibraryPatternBank.class);
        return instance;
    }

}
