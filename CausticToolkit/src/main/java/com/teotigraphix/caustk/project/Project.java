
package com.teotigraphix.caustk.project;

import java.io.File;

/**
 * The main class that is serialized in a ctk file.
 * <p>
 * Since using json as the main serializer, the Project needs to have concrete
 * references for deserialization to work correct.
 * <p>
 * If an application needs specific model added to the project, either create
 * separate serialized files or subclass {@link Project} and add new API.
 */
public class Project {

    private transient boolean isClosed;

    //----------------------------------
    // file
    //----------------------------------

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File value) {
        file = value;
    }

    //----------------------------------
    // info
    //----------------------------------

    private ProjectInfo info;

    public ProjectInfo getInfo() {
        return info;
    }

    public void setInfo(ProjectInfo value) {
        info = value;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Project() {
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void open() {
        isClosed = false;
    }

    public void close() {
        isClosed = true;
    }

}
