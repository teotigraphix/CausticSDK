
package com.teotigraphix.caustk.groove.utils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.utils.SerializeUtils;
import com.teotigraphix.caustk.utils.ZipCompress;
import com.teotigraphix.caustk.utils.ZipUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public final class LibraryProductExportUtils {

    public static void exportProduct(final LibraryProduct product, File targetArchive)
            throws IOException {
        final File productDirectory = product.getDirectory();
        SerializeUtils.pack(new File(productDirectory, LibraryProductUtils.PRODUCT_BIN), product);
        final ZipCompress compress = new ZipCompress(productDirectory);
        compress.zip(targetArchive);
    }

    public static LibraryProduct getProductFromArchive(File archiveFile, String entryName,
            File outputFile) throws IOException {
        ZipUtils.writeZipEntryToFile(archiveFile, entryName, outputFile);
        if (!outputFile.exists())
            throw new IOException("Zip entry failed to write: " + outputFile);

        LibraryProduct product = CaustkRuntime.getInstance().getRack().getSerializer()
                .deserialize(outputFile, LibraryProduct.class);
        outputFile.deleteOnExit();
        return product;
    }

    public static void packageProduct(final LibraryProduct product, File targetArchive)
            throws IOException, CausticException {
        final File productDirectory = product.getDirectory();

        Collection<File> files = FileUtils.listFiles(productDirectory, new IOFileFilter() {
            // File
            @Override
            public boolean accept(File directory, String filename) {
                System.out.println("File1: " + filename);
                return true;
            }

            @Override
            public boolean accept(File pathname) {
                //System.out.println("File2: " + pathname);
                return true;
            }
        }, new IOFileFilter() {
            // Directory
            @Override
            public boolean accept(File directory, String filename) {
                System.out.println("Directory1: " + filename);
                return true;
            }

            @Override
            public boolean accept(File pathname) {
                System.out.println("Directory2: " + pathname);
                return true;
            }
        });

        for (File archiveFile : files) {
            // adds the manifest of the LibraryItem to the LibraryProduct
            LibraryProductUtils.addLibraryItemArchive(archiveFile, product);
        }

        exportProduct(product, targetArchive);
    }
}
