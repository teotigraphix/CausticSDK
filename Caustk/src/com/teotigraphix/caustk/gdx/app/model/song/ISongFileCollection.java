
package com.teotigraphix.caustk.gdx.app.model.song;

import java.io.File;
import java.util.Collection;

import com.google.common.eventbus.EventBus;

public interface ISongFileCollection {

    Collection<SongFileRoot> getRoots();

    Collection<SongFile> getFiles();

    EventBus getEventBus();

    void addSourceDirectory(File sourceDirectory);

    Collection<SongFile> removedSourceDirectory(File sourceDirectory);

    void reset();

    public static enum SongFileCollectionEventKind {

        SourceDirectoryChange,

        SelectedFilesChange,

        UI_FilesChange,

        Action_FileLoadStart,

        Action_FileLoadUpdate,

        Action_FileLoadComplete,

        SourceDirectoryAdd,

        SourceDirectoryRemove,
    }

    public static class SongFileCollectionEvent {

        private SongFileCollectionEventKind kind;

        private File file;

        private Collection<SongFile> songFiles;

        public File getFile() {
            return file;
        }

        public Collection<SongFile> getSongFiles() {
            return songFiles;
        }

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

        public SongFileCollectionEvent(SongFileCollectionEventKind kind,
                Collection<SongFile> songFiles) {
            this.kind = kind;
            this.songFiles = songFiles;
        }
    }

}
