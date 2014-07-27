
package com.teotigraphix.caustk.groove.utils;

import java.io.File;
import java.io.IOException;

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
import com.teotigraphix.caustk.utils.SerializeUtils;
import com.teotigraphix.caustk.utils.ZipUtils;

public class LibraryProductUtils {

    private static final String MANIFEST_JSON = "manifest.json";

    public static String toProductPath(LibraryProduct product, File archiveFile) {
        File sourceDirectory = product.getSourceDirectory();
        String sourceNormalized = FilenameUtils.normalize(sourceDirectory.getPath(), true);
        String archiveNormalized = FilenameUtils.normalize(archiveFile.getPath(), true);
        return archiveNormalized.replace(sourceNormalized, "");
    }

    public static void addToProduct(File archiveFile, LibraryProduct product, Class<?> clazz)
            throws CausticException {
        final String json = ZipUtils.readZipString(archiveFile, new File(MANIFEST_JSON));
        final LibraryProductItem libraryItem = SerializeUtils.unpack(json, clazz);
        libraryItem.setProductPath(LibraryProductUtils.toProductPath(product, archiveFile));
        product.addItem(libraryItem);
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
