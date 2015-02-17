
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

    //private Collection<SongFile> files = new ArrayList<SongFile>();

    private Collection<SongFileSource> sources;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //-------------------------------------------------------------------------- 

    //----------------------------------
    // files
    //----------------------------------

    @Override
    public Collection<SongFile> getFiles() {
        ArrayList<SongFile> result = new ArrayList<SongFile>();
        for (SongFileSource source : sources) {
            result.addAll(source.getFiles());
        }
        return result;
    }

    //----------------------------------
    // directories
    //----------------------------------

    @Override
    public Collection<SongFileSource> getSources() {
        return sources;
    }

    public void setSources(Collection<SongFileSource> sources) {
        this.sources = sources;
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
    public SongFileSource addSourceDirectory(File sourceDirectory, boolean recursive) {
        SongFileSource source = internalAddSourceDirectory(sourceDirectory, recursive, true);
        if (source != null) {
            getEventBus().post(
                    new SongFileCollectionEvent(SongFileCollectionEventKind.SourceDirectoryAdd,
                            sourceDirectory));
        }
        return source;
    }

    @Override
    public SongFileSource removeSourceDirectory(File sourceDirectory) {
        SongFileSource source = internalRemoveSourceDirectory(sourceDirectory);
        if (source != null) {
            getEventBus().post(
                    new SongFileCollectionEvent(SongFileCollectionEventKind.SourceDirectoryRemove,
                            source.getFiles()));
        }
        return source;
    }

    //--------------------------------------------------------------------------
    // Internal :: Methods
    //--------------------------------------------------------------------------

    SongFileSource internalAddSourceDirectory(File sourceDirectory, boolean recursive,
            boolean loadFiles) {
        if (directoriesContain(sources, sourceDirectory))
            return null;

        SongFileSource source = new SongFileSource(sourceDirectory);
        sources.add(source);

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
            loader.load(source, collection);

        return source;
    }

    SongFileSource internalRemoveSourceDirectory(File sourceDirectory) {
        if (!directoriesContain(sources, sourceDirectory))
            return null;

        //        ArrayList<SongFile> remove = new ArrayList<SongFile>();
        //        for (SongFile file : files) {
        //            if (RuntimeUtils.isInSubDirectory(sourceDirectory, file.getFile())) {
        //                remove.add(file);
        //            }
        //        }
        //        files.removeAll(remove);
        SongFileSource source = getSource(sourceDirectory);
        sources.remove(source);
        return source;
    }

    private SongFileSource getSource(File directory) {
        for (SongFileSource root : sources) {
            if (root.getFile().equals(directory))
                return root;
        }
        return null;
    }

    @Override
    public void reset() {
        //files.clear();
    }

    @Override
    public EventBus getEventBus() {
        return CaustkRuntime.getInstance().getApplication().getEventBus();
    }

    private static boolean directoriesContain(Collection<SongFileSource> directories,
            File fileOrDirectory) {
        if (directories.contains(fileOrDirectory))
            return true;
        for (SongFileSource file : directories) {
            if (RuntimeUtils.isInSubDirectory(file.getFile(), fileOrDirectory))
                return true;
        }
        return false;
    }
}
