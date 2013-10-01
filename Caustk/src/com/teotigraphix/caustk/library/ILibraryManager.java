////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.library;

import java.io.File;
import java.io.IOException;

import com.teotigraphix.caustk.controller.IControllerComponent;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.library.core.Library;
import com.teotigraphix.caustk.library.item.LibraryPatch;
import com.teotigraphix.caustk.tone.Tone;

public interface ILibraryManager extends IControllerComponent {

    Library getSelectedLibrary();

    /**
     * @see OnLibraryManagerSelectedLibraryChange
     * @param value
     */
    void setSelectedLibrary(Library value);

    Library loadLibrary(File file);

    Library loadLibrary(String relativePath);

    void saveLibrary(Library library) throws IOException;

    /**
     * @param relativePath The string path IE <code>User</code> or
     *            <code>subdir/User</code>.
     * @see #createLibrary(File)
     * @throws IOException
     */
    Library createLibrary(String relativePath) throws IOException;

    /**
     * Create a library in the application's <code>libraries</code> directory.
     * 
     * @param file The relative directory of the new library. IE
     *            <code>User</code> or <code>subdir/User</code>.
     * @throws IOException
     */
    Library createLibrary(File file) throws IOException;

    /**
     * @param library
     * @param causticFile
     * @throws IOException
     * @throws CausticException
     * @see OnLibraryManagerImportComplete
     */
    void importSong(Library library, File causticFile) throws IOException, CausticException;

    void importPatterns(Library library, File causticFile) throws IOException, CausticException;

    void delete() throws IOException;

    //Library createLibrary(String name) throws IOException;

    void clear();

    //    boolean isLibrary(File libraryFile);

    void deleteLibrary(File reletivePath) throws IOException;

    void exportLibrary(File file, Library library) throws IOException;

    //    Library importLibrary(File file) throws IOException;

    public static class OnLibraryManagerSelectedLibraryChange {

        private Library library;

        public Library getLibrary() {
            return library;
        }

        public OnLibraryManagerSelectedLibraryChange(Library library) {
            this.library = library;
        }

    }

    public static class OnLibraryManagerImportComplete {

    }

    /**
     * Saves the selected library.
     * 
     * @throws IOException
     */
    void save() throws IOException;

    /**
     * Creates an empty library with no directory
     */
    Library createLibrary();

    void assignPatch(Tone tone, LibraryPatch libraryPatch);

    void assignPatch(int toneIndex, LibraryPatch libraryPatch);

}
