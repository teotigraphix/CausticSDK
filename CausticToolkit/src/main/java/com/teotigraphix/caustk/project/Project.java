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

/**
 * The main class that is serialized in a <code>ctk</code> file.
 */
public class Project {

    private transient boolean isClosed;

    //----------------------------------
    // file
    //----------------------------------

    private File file;

    /**
     * Returns the reletive File path to the project in the
     * <code>projects</code> directory.
     */
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
