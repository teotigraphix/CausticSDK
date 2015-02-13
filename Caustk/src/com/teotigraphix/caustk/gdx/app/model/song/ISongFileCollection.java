
package com.teotigraphix.caustk.gdx.app.model.song;

import java.io.File;
import java.util.Collection;

import com.badlogic.gdx.utils.Array;
import com.google.common.eventbus.EventBus;

public interface ISongFileCollection {

    Collection<SongFile> getFiles();

    Array<SongFile> getSelectedFilesAsArray();

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

    EventBus getEventBus();
}
