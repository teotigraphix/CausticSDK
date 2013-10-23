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

import com.teotigraphix.caustk.controller.ICaustkController;

/**
 * The project manager manages the single project loaded for an application.
 * <p>
 * The manager will have a root directory passed to it when it is created. All
 * project related files are stored within this directory.
 */
public interface IProjectManager {

    /**
     * The root application directory, all {@link Project}s are stored in the
     * <code>applicationRoot/projects</code> directory.
     * 
     * @return The absolute path to the directory.
     */
    File getApplicationRoot();

    /**
     * Returns the <code>projects</code> directory held within the
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
     * Returns a List of all Projects in the <code>projects</code> directory.
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
     * @param relativePath The path within the <code>projects</code> directory
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
     *            the <code>projects</code> directory. The name of the
     *            {@link File} is used for the project directory name upon
     *            creation.
     * @return A new {@link Project} instance that is not yet on disk.
     * @throws IOException
     * @see OnProjectManagerChange
     * @see ProjectManagerChangeKind#Create
     */
    Project createProject(File projectFile) throws IOException;

    /**
     * Loads a project from disk using the <code>.project</code> project format.
     * 
     * @param directory The project directory path; contained in the
     *            <code>projects</code> directory holding a
     *            <code>.project</code> file.
     * @return A fully loaded <code>.project</code> project state.
     * @throws IOException Project file does not exist
     * @see OnProjectManagerChange
     * @see ProjectManagerChangeKind#Load
     * @see ProjectManagerChangeKind#LoadComplete
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
     * @see OnProjectManagerChange
     * @see ProjectManagerChangeKind#Save
     * @see ProjectManagerChangeKind#SaveComplete
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
     * @see OnProjectManagerChange
     * @see ProjectManagerChangeKind#Exit
     */
    void exit() throws IOException;

    /**
     * Closes the {@link Project#close()} and clears the project from the
     * project manager.
     * 
     * @see OnProjectManagerChange
     * @see ProjectManagerChangeKind$#CloseComplete
     */
    void clear();

    /**
     * @see ICaustkController#getDispatcher()
     */
    public static class ProjectEvent {

        private Project project;

        public Project getProject() {
            return project;
        }

        public ProjectEvent(Project project) {
            this.project = project;
        }
    }

    public enum ProjectManagerChangeKind {

        /**
         * Dispatched when a project has been created and is getting registered
         * with the system for the first time.
         */
        Create,

        /**
         * Dispatched when a project has been loaded and has been deserialzed.
         */
        Load,

        /**
         * No impl.
         */
        LoadComplete,

        /**
         * Dispatched when the project manager is about to save state.
         * <p>
         * Clients can listen to this event and save their state as necessary.
         */
        Save,

        /**
         * Dispatched when the project manager has completely saved all state.
         */
        SaveComplete,

        /**
         * Dispatched when a project has been closed by
         * {@link IProjectManager#clear()}.
         */
        CloseComplete,

        /**
         * Dispatched when the project manager has had its
         * {@link IProjectManager#exit()} method called.
         */
        Exit;
    }

    /**
     * Dispatched when the project manager's state changes.
     * 
     * @see ICaustkController#getDispatcher()
     */
    public static class OnProjectManagerChange extends ProjectEvent {
        private ProjectManagerChangeKind kind;

        public final ProjectManagerChangeKind getKind() {
            return kind;
        }

        public OnProjectManagerChange(Project project, ProjectManagerChangeKind kind) {
            super(project);
            this.kind = kind;
        }
    }

}
