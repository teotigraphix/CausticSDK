////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.project;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * The main class that is serialized in a <code>ctk</code> file.
 */
public class Project {

    private transient boolean isClosed;

    private transient boolean initializing = false;

    public boolean isInitializing() {
        return initializing;
    }

    public void setInitializing(boolean value) {
        initializing = value;
    }

    //----------------------------------
    // file
    //----------------------------------

    private File file;

    /**
     * Returns the relative File path to the project in the
     * <code>projects</code> directory.
     */
    public File getFile() {
        return file;
    }

    public void setFile(File value) {
        file = value;
    }

    public File getDirectory() {
        return file;
    }

    //----------------------------------
    // info
    //----------------------------------

    private ProjectInfo info;

    /**
     * Returns the project's meta data containing the project's name, author
     * etc.
     */
    public ProjectInfo getInfo() {
        return info;
    }

    public void setInfo(ProjectInfo value) {
        info = value;
    }

    //----------------------------------
    // map
    //----------------------------------

    private Map<String, Object> map = new HashMap<String, Object>();

    /**
     * Adds a key/value pair to the session map.
     * <p>
     * The value must be of primitive type.
     * 
     * @param key The String key.
     * @param value The primitive value.
     */
    public void put(String key, Object value) {
        map.put(key, value);
    }

    /**
     * Returns a String for the key, <code>null</code> if the key does not
     * exist.
     * 
     * @param key The String key.
     */
    public String getString(String key) {
        if (!map.containsKey(key))
            return null;
        return String.valueOf(map.get(key));
    }

    public String getString(String key, String defaultValue) {
        String result = getString(key);
        if (result == null)
            return defaultValue;
        return result;
    }

    /**
     * Returns a Integer for the key, <code>null</code> if the key does not
     * exist.
     * 
     * @param key The String key.
     */
    public Integer getInterger(String key) {
        if (!map.containsKey(key))
            return null;
        return Integer.parseInt((String)map.get(key));
    }

    /**
     * Returns a Float for the key, <code>null</code> if the key does not exist.
     * 
     * @param key The String key.
     */
    public Float getFloat(String key) {
        if (!map.containsKey(key))
            return null;
        return Float.parseFloat((String)map.get(key));
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Project() {
    }

    /**
     * Returns whether the project is open or closed.
     * <p>
     * The project is open when the {@link IProjectManager#create(File)} or
     * {@link IProjectManager#load(File)} has been called.
     * <p>
     * The project is closed when the {@link IProjectManager#exit()} has been
     * called.
     */
    public boolean isClosed() {
        return isClosed;
    }

    /**
     * Opens a project.
     */
    public void open() {
        isClosed = false;
    }

    /**
     * Closes a project.
     */
    public void close() {
        isClosed = true;
    }

}
