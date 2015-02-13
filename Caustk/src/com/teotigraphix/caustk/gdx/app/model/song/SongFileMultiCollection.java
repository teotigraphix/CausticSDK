
package com.teotigraphix.caustk.gdx.app.model.song;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.google.common.eventbus.EventBus;
import com.teotigraphix.caustk.core.internal.CaustkRuntime;

public class SongFileMultiCollection implements ISongFileCollection {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    @Tag(0)
    private SongFileLoader loader;

    @Tag(1)
    private ArrayList<SongFile> files = new ArrayList<SongFile>();

    @Tag(2)
    private ArrayList<File> directories = new ArrayList<File>();

    @Override
    public Collection<SongFile> getFiles() {
        return files;
    }

    @Override
    public Array<SongFile> getSelectedFilesAsArray() {
        //        Array<SongFile> result = new Array<SongFile>();
        //        for (SongFile songFile : selectedFiles) {
        //            result.add(songFile);
        //        }
        //        return result;
        return null;
    }

    public Array<SongFile> getFilesAsArray() {
        Array<SongFile> result = new Array<SongFile>();
        for (SongFile songFile : files) {
            result.add(songFile);
        }
        return result;
    }

    public void addSourceDirectory(File sourceDirectory) {
        fire(SongFileMultiCollectionEventKind.SourceDirectoryAdd);
    }

    public SongFileMultiCollection() {
        loader = new SongFileLoader(this);
    }

    public void reset() {
        files.clear();
    }

    void fire(SongFileMultiCollectionEventKind kind) {
        getEventBus().post(new SongFileMultiCollectionEvent(kind));
    }

    public static enum SongFileMultiCollectionEventKind {

        SourceDirectoryAdd,

        SourceDirectoryRemove,
    }

    public static class SongFileMultiCollectionEvent {

        private SongFileMultiCollectionEventKind kind;

        private File file;

        public SongFileMultiCollectionEventKind getKind() {
            return kind;
        }

        public SongFileMultiCollectionEvent(SongFileMultiCollectionEventKind kind) {
            this.kind = kind;
        }

        public SongFileMultiCollectionEvent(SongFileMultiCollectionEventKind kind, File file) {
            this.kind = kind;
            this.file = file;
        }

        public File getFile() {
            return file;
        }
    }

    @Override
    public EventBus getEventBus() {
        return CaustkRuntime.getInstance().getApplication().getEventBus();
    }
}
