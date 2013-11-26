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

package com.teotigraphix.libgdx.model;

import java.io.IOException;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.libgdx.dialog.IDialogManager;
import com.teotigraphix.libgdx.screen.IScreen;

public interface IApplicationModel extends ICaustkModel {

    //--------------------------------------------------------------------------
    // Properties
    //--------------------------------------------------------------------------

    ICaustkController getController();

    IScreen getCurrentScreen();

    IDialogManager getDialogManager();

    /**
     * Returns the last project's relative path, <code>null</code> if this is
     * the first start of the application.
     */
    String getLastProject();

    //----------------------------------
    // state
    //----------------------------------

    void setStateType(Class<? extends ApplicationModelState> classType);

    Class<? extends ApplicationModelState> getStateType();

    <T extends ApplicationModelState> T getState(Class<T> stateType);

    //----------------------------------
    // initialized
    //----------------------------------

    /**
     * Whether the application model has initialized or reloaded the initial
     * {@link Project} state.
     */
    boolean isInitialized();

    /**
     * @param value
     * @see OnApplicationModelPhaseChange
     * @see ApplicationModelPhase#Initialize
     */
    void setInitialized(boolean value);

    //----------------------------------
    // project
    //----------------------------------

    boolean isFirstRun();

    Project getProject();

    String[] getProjectsAsArray();

    //----------------------------------
    // dirty
    //----------------------------------

    /**
     * Returns whether the application state is dirty.
     */
    boolean isDirty();

    /**
     * Calls {@link #setDirty(boolean)} with true.
     */
    void setDirty();

    /**
     * Sets the application state dirty.
     * 
     * @param value true sets dirty, false resets state after save.
     */
    void setDirty(boolean value);

    /**
     * Returns the application's name.
     */
    String getName();

    //--------------------------------------------------------------------------
    // Methods
    //--------------------------------------------------------------------------

    void create() throws CausticException;

    Project createNewProject(String projectName) throws CausticException;

    Project loadProject(String projectName) throws IOException;

    void run();

    void save();

    void pushScreen(int screenId);

    void registerModel(ICaustkModel model);

    //--------------------------------------------------------------------------
    // Events
    //--------------------------------------------------------------------------

    public enum ApplicationModelPhase {

        InitializeProject,

        ReloadProject,

        /**
         * Dispatched after the initialize or reload project is fired.
         * <p>
         * This phase allows application mediators to hook into the
         * {@link IRack} and other application singletons regardless of project
         * state.
         */
        Initialize;
    }

    public static class OnApplicationModelPhaseChange {

        private ApplicationModelPhase phase;

        public final ApplicationModelPhase getPhase() {
            return phase;
        }

        //        private final Project project;
        //
        //        public final Project getProject() {
        //            return project;
        //        }

        public OnApplicationModelPhaseChange(ApplicationModelPhase phase) {
            this.phase = phase;
        }
    }

    public static class OnApplicationModelNewProjectComplete {

        private final Project project;

        public final Project getProject() {
            return project;
        }

        public OnApplicationModelNewProjectComplete(Project project) {
            this.project = project;
        }
    }

    /**
     * Dispatched when the Application has become dirty or was cleaned by a
     * save.
     * 
     * @see ApplicationModel#getDispatcher()
     */
    public static class OnApplicationModelDirtyChanged {

        private boolean dirty;

        public final boolean isDirty() {
            return dirty;
        }

        public OnApplicationModelDirtyChanged(boolean dirty) {
            this.dirty = dirty;
        }
    }
}
