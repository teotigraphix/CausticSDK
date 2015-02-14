
package com.teotigraphix.caustk.gdx.app.model.song;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.badlogic.gdx.utils.Array;
import com.google.common.eventbus.EventBus;
import com.teotigraphix.caustk.core.internal.CaustkRuntime;
import com.teotigraphix.caustk.utils.core.RuntimeUtils;

public class SongFileMultiCollection implements ISongFileCollection {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private SongFileLoader loader;

    private Collection<SongFile> files = new ArrayList<SongFile>();

    private Collection<File> directories = new ArrayList<File>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // files
    //----------------------------------

    @Override
    public Collection<SongFile> getFiles() {
        return files;
    }

    public void setFiles(Collection<SongFile> files) {
        this.files = files;
    }

    //----------------------------------
    // directories
    //----------------------------------

    @Override
    public Collection<File> getDirectories() {
        return directories;
    }

    public void setDirectories(Collection<File> directories) {
        this.directories = directories;
    }

    //--------------------------------------------------------------------------
    // Private :: Properties
    //--------------------------------------------------------------------------

    SongFileLoader getLoader() {
        return loader;
    }

    public Array<SongFile> getFilesAsArray() {
        Array<SongFile> result = new Array<SongFile>();
        for (SongFile songFile : files) {
            result.add(songFile);
        }
        return result;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SongFileMultiCollection() {
        loader = new SongFileLoader(this);
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void addSourceDirectory(File sourceDirectory) {
        Collection<File> collection = internalAddSourceDirectory(sourceDirectory, false, true);
        if (collection != null) {
            getEventBus().post(
                    new SongFileCollectionEvent(SongFileCollectionEventKind.SourceDirectoryAdd,
                            sourceDirectory));
        }
    }

    @Override
    public Collection<SongFile> removedSourceDirectory(File sourceDirectory) {
        Collection<SongFile> collection = internalRemovedSourceDirectory(sourceDirectory);
        if (collection != null) {
            getEventBus().post(
                    new SongFileCollectionEvent(SongFileCollectionEventKind.SourceDirectoryRemove,
                            collection));
        }
        return collection;
    }

    //--------------------------------------------------------------------------
    // Internal :: Methods
    //--------------------------------------------------------------------------

    Collection<File> internalAddSourceDirectory(File sourceDirectory, boolean recursive,
            boolean loadFiles) {
        if (RuntimeUtils.directoriesContain(directories, sourceDirectory))
            return null;

        directories.add(sourceDirectory);

        IOFileFilter dirFilter = null;
        if (recursive)
            dirFilter = DirectoryFileFilter.DIRECTORY;

        // Shows both files, dirs
        // FileUtils.listFilesAndDirs(new File(dir), TrueFileFilter.INSTANCE, DirectoryFileFilter.DIRECTORY);

        // Only shows directories and not files
        // FileUtils.listFilesAndDirs(new File(dir), new NotFileFilter(TrueFileFilter.INSTANCE), DirectoryFileFilter.DIRECTORY)
        Collection<File> collection = FileUtils.listFiles(sourceDirectory, new IOFileFilter() {
            @Override
            public boolean accept(File file, String name) {
                return false;
            }

            @Override
            public boolean accept(File file) {
                return FilenameUtils.getExtension(file.getName()).endsWith("caustic");
            }
        }, dirFilter);

        if (collection.size() == 0)
            return null;

        if (loadFiles)
            loader.load(collection);

        return collection;
    }

    Collection<SongFile> internalRemovedSourceDirectory(File sourceDirectory) {
        if (!directories.contains(sourceDirectory))
            return null;

        ArrayList<SongFile> remove = new ArrayList<SongFile>();
        for (SongFile file : files) {
            if (RuntimeUtils.isInSubDirectory(sourceDirectory, file.getFile())) {
                remove.add(file);
            }
        }
        files.removeAll(remove);
        directories.remove(sourceDirectory);
        return remove;
    }

    @Override
    public void reset() {
        files.clear();
    }

    @Override
    public EventBus getEventBus() {
        return CaustkRuntime.getInstance().getApplication().getEventBus();
    }

}
