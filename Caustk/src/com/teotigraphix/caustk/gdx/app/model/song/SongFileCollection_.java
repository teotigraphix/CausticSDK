
package com.teotigraphix.caustk.gdx.app.model.song;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.badlogic.gdx.utils.Array;
import com.google.common.eventbus.EventBus;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.core.internal.CaustkRuntime;
import com.teotigraphix.caustk.gdx.app.model.ViewModelBase;

public class SongFileCollection_ implements ISongFileCollection {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private SongFileLoader loader;

    private File sourceDirectory;

    private ArrayList<SongFile> files = new ArrayList<SongFile>();

    private ArrayList<SongFile> selectedFiles = new ArrayList<SongFile>();

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
        getEventBus().post(
                new SongFileCollectionEvent(SongFileCollectionEventKind.SourceDirectoryChange));
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

    }

    public void setSelectedFiles(ArrayList<SongFile> selectedFiles) {
        this.selectedFiles = selectedFiles;
        getEventBus().post(
                new SongFileCollectionEvent(SongFileCollectionEventKind.SelectedFilesChange));
    }

    @Override
    public Collection<SongFile> getFiles() {
        return files;
    }

    void setRawFiles(Collection<File> rawFiles) {
        loader.load(rawFiles);
    }

    public ArrayList<SongFile> getSelectedFiles() {
        return selectedFiles;
    }

    public SongFileCollection_(ViewModelBase model) {
        this.model = model;
        loader = new SongFileLoader(this);
    }

    ICaustkRack getRack() {
        return CaustkRuntime.getInstance().getRack();
    }

    @Override
    public void reset() {
        selectedFiles.clear();
        files.clear();
        setSourceDirectory(null);
    }

    @Override
    public EventBus getEventBus() {
        return model.getEventBus();
    }

    public Array<SongFile> getSelectedFilesAsArray() {
        Array<SongFile> result = new Array<SongFile>();
        for (SongFile songFile : selectedFiles) {
            result.add(songFile);
        }
        return result;
    }

    public Array<SongFile> getFilesAsArray() {
        Array<SongFile> result = new Array<SongFile>();
        for (SongFile songFile : files) {
            result.add(songFile);
        }
        return result;
    }

    @Override
    public void addSourceDirectory(File sourceDirectory) {
        // TODO Auto-generated method stub

    }

    @Override
    public Collection<SongFile> removedSourceDirectory(File sourceDirectory) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<SongFileRoot> getRoots() {
        // TODO Auto-generated method stub
        return null;
    }

}
