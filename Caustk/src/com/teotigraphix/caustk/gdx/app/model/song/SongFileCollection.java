
package com.teotigraphix.caustk.gdx.app.model.song;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.badlogic.gdx.utils.Array;
import com.google.common.eventbus.EventBus;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.core.internal.CaustkRuntime;
import com.teotigraphix.caustk.gdx.app.model.ViewModelBase;

public class SongFileCollection {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private SongFileLoader loader;

    private File sourceDirectory;

    private Array<SongFile> files = new Array<SongFile>();

    private Array<SongFile> selectedFiles = new Array<SongFile>();

    private ViewModelBase model;

    public File getSourceDirectory() {
        return sourceDirectory;
    }

    /**
     * @param sourceDirectory
     * @see SongFileCollectionEventKind#SourceDirectoryChange
     */
    public void setSourceDirectory(File sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
        if (sourceDirectory != null)
            fillSourceFiles();
        fire(SongFileCollectionEventKind.SourceDirectoryChange);
    }

    private void fillSourceFiles() {
        Collection<File> collection = FileUtils.listFiles(sourceDirectory, new IOFileFilter() {
            @Override
            public boolean accept(File file, String name) {
                return false;
            }

            @Override
            public boolean accept(File file) {
                return FilenameUtils.getExtension(file.getName()).endsWith("caustic");
            }
        }, null);
        setRawFiles(collection);
    }

    public void setSelectedFiles(Array<SongFile> selectedFiles) {
        this.selectedFiles = selectedFiles;
        fire(SongFileCollectionEventKind.SelectedFilesChange);
    }

    public Array<SongFile> getFiles() {
        return files;
    }

    void setRawFiles(Collection<File> rawFiles) {
        loader.load(rawFiles);
    }

    public Array<SongFile> getSelectedFiles() {
        return selectedFiles;
    }

    public SongFileCollection(ViewModelBase model) {
        this.model = model;
        loader = new SongFileLoader(this);
    }

    ICaustkRack getRack() {
        return CaustkRuntime.getInstance().getRack();
    }

    public void reset() {
        selectedFiles.clear();
        files.clear();
        setSourceDirectory(null);
    }

    void fire(SongFileCollectionEventKind kind) {
        model.getEventBus().post(new SongFileCollectionEvent(kind));
    }

    public static enum SongFileCollectionEventKind {

        SourceDirectoryChange,

        SelectedFilesChange,

        FilesChange,

        Action_FileLoadStart,

        Action_FileLoadUpdate,

        Action_FileLoadComplete,

        // FilesRefresh,
    }

    public static class SongFileCollectionEvent {

        private SongFileCollectionEventKind kind;

        private File file;

        public SongFileCollectionEventKind getKind() {
            return kind;
        }

        public SongFileCollectionEvent(SongFileCollectionEventKind kind) {
            this.kind = kind;
        }

        public SongFileCollectionEvent(SongFileCollectionEventKind kind, File file) {
            this.kind = kind;
            this.file = file;
        }

        public File getFile() {
            return file;
        }
    }

    EventBus getEventBus() {
        return model.getEventBus();
    }
}
