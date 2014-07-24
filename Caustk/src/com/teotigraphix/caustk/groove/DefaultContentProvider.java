
package com.teotigraphix.caustk.groove;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

public class DefaultContentProvider {

    public DefaultContentProvider() {
    }

    public void install(GrooveLibrary grooveLibrary, File rootDirectory) {
        // copy/unzip folders
        unarchiveAndCopyContent(rootDirectory);
        // scan tree hierarchy and add elements to the library
        initializeLibrary(rootDirectory, grooveLibrary);
    }

    private void unarchiveAndCopyContent(File rootDirectory) {
        // TODO Auto-generated method stub

    }

    private void initializeLibrary(File rootDirectory, GrooveLibrary grooveLibrary) {

        FileUtils.listFiles(rootDirectory, new IOFileFilter() {
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
