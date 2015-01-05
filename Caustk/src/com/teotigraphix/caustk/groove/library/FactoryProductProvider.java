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

package com.teotigraphix.caustk.groove.library;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;

public class FactoryProductProvider {

    @SuppressWarnings("unused")
    private LibraryProduct product;

    /**
     * The zip archive File that holds the factory content directory and
     * {@link LibraryProduct} manifest at the root.
     * 
     * @param sourceProduct The location of the factory content archive to be
     *            extracted into the conentDirectory at
     *            {@link #install(GrooveLibrary, java.io.File)}.
     */
    public FactoryProductProvider(LibraryProduct product) {
        this.product = product;
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
