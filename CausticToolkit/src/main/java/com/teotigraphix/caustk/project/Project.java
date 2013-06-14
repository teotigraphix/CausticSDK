
package com.teotigraphix.caustk.project;

import java.io.File;

public class Project {

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

}
