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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

/**
 * The main class that is serialized in a <code>.project</code> file.
 * 
 * @author Michael Schmalle
 */
public class Project {

    static final String FILE_PROJECT = ".project";

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private transient IProjectManager projectManager;

    void setProjectManager(IProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    private transient boolean isFirstRun;

    private transient boolean isClosed;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private File directory;

    @Tag(1)
    private ProjectInfo info;

    @Tag(2)
    private ProjectPreferences preferences;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // isFirstRun
    //----------------------------------

    void setFirstRun(boolean value) {
        isFirstRun = value;
    }

    /**
     * Returns whether this {@link Project} was just created <code>true</code>
     * or loaded from disk <code>false</code>.
     */
    public boolean isFirstRun() {
        return isFirstRun;
    }

    //----------------------------------
    // file
    //----------------------------------

    /**
     * Returns the name of the project's directory.
     */
    public String getName() {
        return directory.getName();
    }

    /**
     * Returns the project's relative location within the application root's
     * <code>project</code> directory. e.g.
     * <code>MyAp/projects/[MyProject]</code> or
     * <code>MyAp/projects/[sub/MyProject]</code>.
     */
    public File getDirectory() {
        return directory;
    }

    void setDirectory(File value) {
        directory = value;
    }

    /**
     * Returns the absolute location of the project's directory on the current
     * disk drive. e.g. <code>[/sdcard/MyAp/projects/MyProject]</code>
     */
    public File getAbsolutDirectory() {
        return projectManager.getDirectory(directory.getPath());
    }

    /**
     * Returns the <code>.project</code> state file located at the root of the
     * project directory.
     */
    public File getProjectFile() {
        return getAbsoluteResource(FILE_PROJECT);
    }

    /**
     * Returns a relative File handle to relativePath passed.
     * 
     * @param relativePath The path inside the project's resource directory.
     */
    public File getResource(String relativePath) {
        return new File(getDirectory(), relativePath);
    }

    /**
     * Returns an absolute File handle using the relativePath passed to
     * construct a file from the <code>MyApp/projects/MyProject</code>
     * directory.
     * 
     * @par* @param relativePath The path inside the project's resource
     *      directory.
     */
    public File getAbsoluteResource(String relativePath) {
        return new File(getAbsolutDirectory(), relativePath);
    }

    //----------------------------------
    // info
    //----------------------------------

    /**
     * Returns the project's meta data containing the project's name, author
     * etc.
     */
    public ProjectInfo getInfo() {
        return info;
    }

    void setInfo(ProjectInfo value) {
        info = value;
    }

    //----------------------------------
    // preferences
    //----------------------------------

    /**
     * Returns the preferences that are save with the <code>.project</code> file
     * in JSON format.
     */
    public final ProjectPreferences getPreferences() {
        if (preferences == null)
            preferences = new ProjectPreferences();
        return preferences;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    Project() {
    }

    /**
     * Returns whether the project is open or closed.
     * <p>
     * The project is open when the {@link IProjectManager#createProject(File)}
     * or {@link IProjectManager#load(File)} has been called.
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
