
package com.teotigraphix.caustk.project;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

    //----------------------------------
    // data
    //----------------------------------

    private Map<String, Object> data = new HashMap<String, Object>();

    public void register(Class<? extends IProjectData> clazz, IProjectData instance) {
        data.put(clazz.getName(), instance);
    }

    public <T extends IProjectData> T data(Class<T> clazz) {
        String name = clazz.getName();
        Object object = data.get(name);
        return clazz.cast(object);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Project() {
    }

}
