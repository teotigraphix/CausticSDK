
package com.teotigraphix.caustk.gdx.app.model.song;

import java.io.File;
import java.util.Collection;

import com.google.common.eventbus.EventBus;

public interface ISongFileCollection {

    Collection<SongFileSource> getSources();

    /**
     * Returns all files held in each {@link SongFileSource}, in no particular
     * order.
     */
    Collection<SongFile> getFiles();

    EventBus getEventBus();

    SongFileSource addSourceDirectory(File sourceDirectory, boolean recursive);

    SongFileSource removeSourceDirectory(File sourceDirectory);

    void reset();

    public static enum SongFileCollectionEventKind {

        SourceDirectoryChange,

        SelectedFilesChange,

        UI_FilesChange,

        Action_FileLoadStart,

        Action_FileLoadUpdate,

        /**
         * @see SongFileCollectionEvent#getSource()
         */
        Action_SourceLoadComplete,

        SourceDirectoryAdd,

        SourceDirectoryRemove,
    }

    public static class SongFileCollectionEvent {

        private SongFileCollectionEventKind kind;

        private File file;

        private Collection<SongFile> songFiles;

        private SongFileSource source;

        public File getFile() {
            return file;
        }

        public Collection<SongFile> getSongFiles() {
            return songFiles;
        }

        public SongFileCollectionEventKind getKind() {
            return kind;
        }

        public SongFileSource getSource() {
            return source;
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

        public SongFileCollectionEvent(SongFileCollectionEventKind kind, SongFileSource source) {
            this.kind = kind;
            this.source = source;
        }
    }

}
