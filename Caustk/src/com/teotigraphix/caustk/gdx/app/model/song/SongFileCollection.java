
package com.teotigraphix.caustk.gdx.app.model.song;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.google.common.eventbus.EventBus;
import com.teotigraphix.caustk.core.internal.CaustkRuntime;
import com.teotigraphix.caustk.utils.core.RuntimeUtils;

public class SongFileCollection implements ISongFileCollection {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private SongFileLoader loader;

    private Collection<SongFile> files = new ArrayList<SongFile>();

    private Collection<SongFileRoot> roots = new ArrayList<SongFileRoot>();

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
    public Collection<SongFileRoot> getRoots() {
        return roots;
    }

    public void setRoots(Collection<SongFileRoot> directories) {
        this.roots = directories;
    }

    //--------------------------------------------------------------------------
    // Private :: Properties
    //--------------------------------------------------------------------------

    SongFileLoader getLoader() {
        return loader;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SongFileCollection() {
        loader = new SongFileLoader();
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void addSourceDirectory(File sourceDirectory, boolean recursive) {
        Collection<File> collection = internalAddSourceDirectory(sourceDirectory, recursive, true);
        if (collection != null) {
            getEventBus().post(
                    new SongFileCollectionEvent(SongFileCollectionEventKind.SourceDirectoryAdd,
                            sourceDirectory));
        }
    }

    @Override
    public Collection<SongFile> removeSourceDirectory(File sourceDirectory) {
        Collection<SongFile> collection = internalRemoveSourceDirectory(sourceDirectory);
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
        if (directoriesContain(roots, sourceDirectory))
            return null;

        SongFileRoot root = new SongFileRoot(sourceDirectory);
        roots.add(root);

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
            loader.load(root, collection);

        return collection;
    }

    Collection<SongFile> internalRemoveSourceDirectory(File sourceDirectory) {
        if (!directoriesContain(roots, sourceDirectory))
            return null;

        ArrayList<SongFile> remove = new ArrayList<SongFile>();
        for (SongFile file : files) {
            if (RuntimeUtils.isInSubDirectory(sourceDirectory, file.getFile())) {
                remove.add(file);
            }
        }
        files.removeAll(remove);
        roots.remove(getRoot(sourceDirectory));
        return remove;
    }

    private SongFileRoot getRoot(File directory) {
        for (SongFileRoot root : roots) {
            if (root.getFile().equals(directory))
                return root;
        }
        return null;
    }

    @Override
    public void reset() {
        files.clear();
    }

    @Override
    public EventBus getEventBus() {
        return CaustkRuntime.getInstance().getApplication().getEventBus();
    }

    private static boolean directoriesContain(Collection<SongFileRoot> directories,
            File fileOrDirectory) {
        if (directories.contains(fileOrDirectory))
            return true;
        for (SongFileRoot file : directories) {
            if (RuntimeUtils.isInSubDirectory(file.getFile(), fileOrDirectory))
                return true;
        }
        return false;
    }
}
