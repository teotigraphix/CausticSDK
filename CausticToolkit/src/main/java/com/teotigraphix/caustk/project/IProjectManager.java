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

import com.teotigraphix.caustk.controller.ICaustkConfiguration;
import com.teotigraphix.caustk.controller.ICaustkController;

public interface IProjectManager {

    /**
     * The root application directory, all {@link Project}s are stored in the
     * <code>applicationRoot/projects</code> directory.
     */
    File getApplicationRoot();

    /**
     * Returns a directory within the {@link #getApplicationRoot()} directory.
     * <p>
     * Will create the directory if it doesn't exist.
     * 
     * @param path The simple path of the directory.
     */
    File getDirectory(String name);

    /**
     * Returns the current {@link Project} instantiated by {@link #createProject(File)}
     * or {@link #load(File)}.
     */
    Project getProject();

    /**
     * Returns the single {@link SessionPreferences} instance for the project
     * session.
     */
    SessionPreferences getSessionPreferences();

    /**
     * Loads the {@link SessionPreferences} from the application root.
     * <p>
     * Must be called after the
     * {@link ICaustkConfiguration#setApplicationRoot(File)} is called.
     */
    void initialize();

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
     * This method will NOT save the {@link Project}. This allows for clients to
     * add data entries before the initial save.
     * <p>
     * A default {@link ProjectInfo} is created for the empty project.
     * 
     * @param projectFile the relative path and name of the <code>.ctk</code>
     *            file without the <code>project</code> sub directory. This is
     *            the file path located within the projects app directory.
     * @return
     * @throws IOException
     * @see OnProjectManagerChange
     * @see ProjectManagerChangeKind#CREATE
     */
    Project createProject(File projectFile) throws IOException;

    /**
     * Loads a project from disk using the <code>.ctk</code> project format.
     * <p>
     * 
     * @param directory The project directory path; contained in the
     *            <code>projects</code> directory.
     * @return A fully loaded <code>.ctk</code> project state.
     * @throws IOException Project file does not exist
     * @see OnProjectManagerChange
     * @see ProjectManagerChangeKind#LOAD
     * @see ProjectManagerChangeKind#LOAD_COMPLETE
     */
    Project load(File directory) throws IOException;

    /**
     * Saves the current {@link Project} to disk using the project's file
     * location.
     * 
     * @throws IOException
     * @see OnProjectManagerChange
     * @see ProjectManagerChangeKind#SAVE
     * @see ProjectManagerChangeKind#SAVE_COMPLETE
     */
    void save() throws IOException;

    /**
     * Saves the project state and exits.
     * <p>
     * The {@link #createProject(File)} or {@link #load(File)} method has to be called
     * again for the project to be active. Calling this method will remove the
     * current {@link Project} instance.
     * 
     * @throws IOException
     * @see OnProjectManagerChange
     * @see ProjectManagerChangeKind#EXIT
     */
    void exit() throws IOException;

    /**
     * Returns whether the project exists and is a valid caustk project.
     * 
     * @param file The project {@link File} relative to the projects directory
     *            of the application.
     */
    boolean isProject(File file);

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
        CREATE,

        /**
         * Dispatched when a project has been loaded and has been deserialzed.
         */
        LOAD,

        /**
         * No impl.
         */
        LOAD_COMPLETE,

        /**
         * Dispatched when the project manager is about to save state.
         * <p>
         * Clients can listen to this event and save their state as necessary.
         */
        SAVE,

        /**
         * Dispatched when the project manager has completely saved all state.
         */
        SAVE_COMPLETE,

        /**
         * Dispatched when a project has been closed by
         * {@link IProjectManager#clear()}.
         */
        CLOSE_COMPLETE,

        /**
         * Dispatched when the project manager has had its
         * {@link IProjectManager#exit()} method called.
         */
        EXIT;
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
