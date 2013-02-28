////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.song;

import java.io.File;
import java.util.List;

import com.teotigraphix.caustic.core.CausticError;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.internal.song.ProjectData;
import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.IPersist;
import com.teotigraphix.common.IRestore;

/**
 * The {@link IProject} API is the toplevel model for the whole Caustic part
 * framework.
 * <p>
 * The project will load the project file that points to the current preset bank
 * being used and patterns, songs, patches referenced within.
 * </p>
 * <p>
 * A project manages;
 * </p>
 * <ul>
 * <li>XML File path</li>
 * <li>Project name</li>
 * </ul>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IProject extends IPersist, IRestore {

    IWorkspace getWorkspace();

    IMemento getState();

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // name
    //----------------------------------

    /**
     * Returns the name of the project without any extension.
     */
    String getFileName();

    /**
     * The human given name.
     * <p>
     * Note: this is not the file name of the project file.
     * </p>
     */
    String getName();

    /**
     * Sets the name, null throws error.
     * 
     * @param value The new human readable name for the project.
     * @throws CausticError
     */
    void setName(String value) throws CausticError;

    //----------------------------------
    // projectFile
    //----------------------------------

    /**
     * Returns the current project's file location.
     */
    File getFile();

    /**
     * The absolute location of the project file.
     * <p>
     * Sub directories can be used in the project's directory.
     * </p>
     * 
     * @param file I File with a path like
     *            /sdcard/App/projects/myprojects/MyProject.xml
     * @throws CausticException Project file does not exist
     */
    //void setProjectFile(File file) throws CausticException;

    //----------------------------------
    // songs
    //----------------------------------

    /**
     * The IProject's ISong collection (if any).
     * <p>
     * This collection is created by default when an application firsts starts
     * up and is then created from a saved memento state.
     * </p>
     */
    List<ISong> getSongs();

    //----------------------------------
    // currentSong
    //----------------------------------

    /**
     * Returns the current song index that is found in the {@link #getSongs()}
     * collection.
     */
    int getCurrentSong();

    /**
     * @see #getCurrentSong()
     */
    void setCurrentSong(int index);

    //----------------------------------
    // selectedSong
    //----------------------------------

    ISong getSelectedSong();

    //----------------------------------
    // data
    //----------------------------------

    /**
     * Return data associated with the project.
     */
    ProjectData getData();

    /**
     * @see #getData()
     */
    void setData(ProjectData value);

    //--------------------------------------------------------------------------
    //
    // Methods
    //
    //--------------------------------------------------------------------------

    /**
     * Loads a project file and it's XML memento.
     * 
     * @param file The full path to the file to load.
     * @throws CausticException
     */
    //    void load(File file) throws CausticException;

    /**
     * Saves a project file.
     * <p>
     * The caller is responsible for checking whether file exists.
     * </p>
     * 
     * @param file The full path to the file to save.
     * @throws CausticException
     */
    //    void save(File file) throws CausticException;

    void startup();

    /**
     * Saves the project state to disk using the {@link IProject#getFile()} as
     * the location to save the file.
     * 
     * @throws CausticException
     */
    void save() throws CausticException;

    /**
     * Saves the project state to disk by renaming the file of the project and
     * then calling {@link IProject#save()}.
     * 
     * @param absoluteFile A file with an absolute location to the projects
     *            directory.
     * @throws CausticException
     */
    void saveAs(File absoluteFile) throws CausticException;

    void saveQuick(File quickSave) throws CausticException;

    /**
     * Adds a song to the project.
     * 
     * @param song
     */
    void addSong(ISong song);

    void removeSong(ISong song);

    ISong getSongAt(int index);

    //--------------------------------------------------------------------------
    //
    // Listeners
    //
    //--------------------------------------------------------------------------

    void addProjectListener(ProjectListener value);

    void removeProjectListener(ProjectListener value);

    public interface ProjectListener {

        /**
         * @param song
         */
        void onSongAdded(ISong song);

        /**
         * @param song
         */
        void onSongRemoved(ISong song);

        /**
         * @param newSong
         * @param oldSong
         */
        void onSongChanged(ISong song, ISong oldSong);
    }

    public static class ProjectEvent {
        private final IProject project;

        public IProject getProject() {
            return project;
        }

        public ProjectEvent(IProject project) {
            this.project = project;

        }
    }

    public static class OnProjectSongChangeEvent extends ProjectEvent {

        private ProjectSongChangKind kind;

        private ISong song;

        private ISong oldSong;

        public ProjectSongChangKind getKind() {
            return kind;
        }

        public ISong getSong() {
            return song;
        }

        public ISong getOldSong() {
            return oldSong;
        }

        public OnProjectSongChangeEvent(IProject project, ProjectSongChangKind kind, ISong song) {
            super(project);
            this.kind = kind;
            this.song = song;
        }

        public OnProjectSongChangeEvent(IProject project, ProjectSongChangKind kind, ISong song,
                ISong oldSong) {
            super(project);
            this.kind = kind;
            this.song = song;
            this.oldSong = oldSong;
        }
    }

    public enum ProjectSongChangKind {
        ADD, REMOVE, CHANGE;
    }

}
