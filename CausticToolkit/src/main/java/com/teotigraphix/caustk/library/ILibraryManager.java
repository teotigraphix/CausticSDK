
package com.teotigraphix.caustk.library;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.teotigraphix.caustic.core.CausticException;

public interface ILibraryManager {

    /**
     * Returns the appRoot/libraries directory.
     */
    File getLibrariesDirectory();

    int getLibraryCount();

    Collection<Library> getLibraries();

    /**
     * Creates an initial {@link Library} from a <code>.caustic</code> file.
     * 
     * @param name The library name but will also be the directory that is
     *            created in the <code>libraries</code> directory.
     * @throws CausticException
     * @throws IOException
     */
    Library createLibrary(String name) throws IOException;

    /**
     * Imports a <code>.caustic</code> song file into the {@link Library}.
     * 
     * @param library The {@link Library} to import the caustic song data.
     * @param causticFile The <code>.caustic</code> file to initially load this
     *            libraries first data.
     * @throws IOException
     */
    void importSong(Library library, File causticFile) throws IOException;

    /**
     * Creates and loads a {@link Library} from the <code>libraries</code>
     * directory.
     * 
     * @param name The library name to load, this will be a directory name.
     * @throws IOException
     */
    Library loadLibrary(String name) throws IOException;

    /**
     * Saves the {@link Library} to disk using the <code>ctk</code> library
     * format.
     * 
     * @param library The {@link Library} to save.
     * @throws IOException
     */
    void saveLibrary(Library library) throws IOException;

    /**
     * Loads a {@link Library}s found in the <code>libraries</code> directory.
     * 
     * @throws IOException
     */
    void load() throws IOException;

    /**
     * Saves a {@link Library}s found in the {@link LibraryRegistry}.
     * 
     * @throws IOException
     */
    void save() throws IOException;

    /**
     * Clears the {@link LibraryRegistry}.
     */
    void clear();

    /**
     * Mainly for testing, deletes all library directories.
     * @throws IOException 
     */
    void delete() throws IOException;

}
