
package com.teotigraphix.gdx.controller;

import java.io.File;
import java.util.List;

import com.teotigraphix.gdx.app.IApplicationComponent;

public interface IFileModel extends IApplicationComponent {

    List<String> getItems();

    List<String> getPaths();

    File getLastDirectory();

    File getRootDirectory();

    void setRootDirectory(File rootDirectory);

    File getLocation();

    void setLocation(File location);

    void selectFile(File selectedFile);

    /**
     * @param fileRequest
     * @see OnFileModelEvent#Request
     */
    void browse(FileRequest fileRequest);

    public static class OnFileModelEvent {

        public static final int Request = 0;

        public static final int Location = 1;

        public static final int Complete = 2;

        private int kind;

        private FileRequest fileRequest;

        private File location;

        public int getKind() {
            return kind;
        }

        public FileRequest getFileRequest() {
            return fileRequest;
        }

        public File getLocation() {
            return location;
        }

        public OnFileModelEvent(int kind, File location) {
            this.kind = kind;
            this.location = location;
        }

        public OnFileModelEvent(int kind, FileRequest fileRequest) {
            this.kind = kind;
            this.fileRequest = fileRequest;
        }
    }
}
