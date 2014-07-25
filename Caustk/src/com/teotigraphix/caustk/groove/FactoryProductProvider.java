
package com.teotigraphix.caustk.groove;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

public class FactoryProductProvider {

    /**
     * The zip archive File that holds the factory content directory and
     * {@link LibraryProduct} manifest at the root.
     * 
     * @param sourceProduct The location of the factory content archive to be
     *            extracted into the conentDirectory at
     *            {@link #install(GrooveLibrary, File)}.
     */
    public FactoryProductProvider(File sourceProduct) {
    }

    public void install(GrooveLibrary grooveLibrary, File contentDirectory) {
        // copy/unzip folders
        unarchiveAndCopyContent(contentDirectory);
        // scan tree hierarchy and add elements to the library
        initializeLibrary(contentDirectory, grooveLibrary);
    }

    private void unarchiveAndCopyContent(File contentDirectory) {
        // TODO Auto-generated method stub

    }

    private void initializeLibrary(File contentDirectory, GrooveLibrary grooveLibrary) {

        FileUtils.listFiles(contentDirectory, new IOFileFilter() {
            // File
            @Override
            public boolean accept(File directory, String filename) {
                return false;
            }

            @Override
            public boolean accept(File pathname) {
                return false;
            }
        }, new IOFileFilter() {
            // Directory
            @Override
            public boolean accept(File directory, String filename) {
                return true;
            }

            @Override
            public boolean accept(File pathname) {
                return true;
            }
        });
    }
}
