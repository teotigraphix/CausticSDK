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
import java.io.IOException;
import java.util.List;

/**
 * The project manager manages the single project loaded for an application.
 * <p>
 * The manager will have a root directory passed to it when it is created. All
 * project related files are stored within this directory.
 */
public interface IProjectManager {

    public static final String PREF_LAST_PROJECT = "lastProject";

    /**
     * The root application directory, all {@link Project}s are stored in the
     * <code>applicationRoot/Projects</code> directory.
     * 
     * @return The absolute path to the directory.
     */
    File getApplicationRoot();

    /**
     * Returns the last project's relative path, <code>null</code> if this is
     * the first start of the application.
     */
    String getLastProject();

    /**
     * Returns the <code>Projects</code> directory held within the
     * {@link #getApplicationRoot()}.
     * 
     * @return The absolute path to the directory.
     */
    File getProjectDirectory();

    /**
     * Returns a directory within the {@link #getProjectDirectory()} directory.
     * <p>
     * Will create the directory if it doesn't exist.
     * 
     * @param relativePath The simple path of the directory.
     * @return The absolute File location of the relativePath within the
     *         <code>projects</code> directory.
     */
    File getDirectory(String relativePath);

    /**
     * Returns the current {@link Project} instantiated by
     * {@link #createProject(File)} or {@link #load(File)}.
     */
    Project getProject();

    /**
     * Returns the single {@link SessionPreferences} instance for the project
     * session.
     * <p>
     * The session preferences is a <code>.settings</code> file that gets saved
     * in the {@link #getApplicationRoot()} directory. It's a simple key, value
     * map of primitives that can hold simple session to session preferences.
     * <p>
     * Using this over native preference implementations will add the benefit of
     * portability from one device to another since you would be copying the
     * application root to transfer data. Which in doing so, you are copying the
     * settings file as well.
     * <p>
     * It's up to the application developer and the needs of the application in
     * the end whether to save in the native format or session settings format.
     */
    SessionPreferences getSessionPreferences();

    /**
     * Returns a List of all Projects in the <code>Projects</code> directory.
     */
    List<File> listProjects();

    /**
     * Returns whether the project exists and is a valid caustk project.
     * 
     * @param file The project {@link File} relative to the projects directory
     *            of the application.
     */
    boolean isProject(File file);

    /**
     * Creates a new {@link Project} file.
     * 
     * @param relativePath The path within the <code>Projects</code> directory
     *            of the application root.
     * @throws IOException
     * @see #createProject(File)
     */
    Project createProject(String relativePath) throws IOException;

    /**
     * Creates a new {@link Project} file.
     * <p>
     * This method will save the {@link Project} after creation.
     * <p>
     * A default {@link ProjectInfo} is created for the empty project.
     * 
     * @param projectFile the relative path and name of the project path within
     *            the <code>Projects</code> directory. The name of the
     *            {@link File} is used for the project directory name upon
     *            creation.
     * @return A new {@link Project} instance that is not yet on disk.
     * @throws IOException
     */
    Project createProject(File projectFile) throws IOException;

    /**
     * Loads a project from disk using the <code>.project</code> project format.
     * 
     * @param directory The project directory path; contained in the
     *            <code>Projects</code> directory holding a
     *            <code>.project</code> file.
     * @return A fully loaded <code>.project</code> project state.
     * @throws IOException Project file does not exist
     */
    Project load(File directory) throws IOException;

    /**
     * Saves the current {@link Project} to disk using the project's file
     * location.
     * <p>
     * This will save the <code>.project</code> and <code>.settings</code>
     * files, as well as any clients that were observing the
     * {@link ProjectManagerChangeKind#Save} event.
     * 
     * @throws IOException
     */
    void save() throws IOException;

    /**
     * Saves the project state and exits.
     * <p>
     * The {@link #createProject(File)} or {@link #load(File)} method has to be
     * called again for the project to be active. Calling this method will
     * remove the current {@link Project} instance.
     * 
     * @throws IOException
     */
    void exit() throws IOException;

    /**
     * Closes the {@link Project#close()} and clears the project from the
     * project manager.
     */
    void clear();
}
