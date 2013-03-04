////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import com.teotigraphix.caustic.activity.IApplicationConfiguration;
import com.teotigraphix.caustic.activity.IApplicationPreferences;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.part.IPart;
import com.teotigraphix.caustic.part.ISoundGenerator;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustic.rack.IRack.SongStateChangeKind;
import com.teotigraphix.caustic.rack.IRackSong;
import com.teotigraphix.caustic.service.IFileService;

/**
 * The {@link IWorkspace} API is the toplevel model for the whole Caustic part
 * framework.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IWorkspace {

    /**
     * Whether the {@link IWorkspace#startAndRun()} has successfully completed
     * its install, boot, run sequence.
     * <p>
     * This will return true after the method is finished executing.
     */
    boolean isRunning();

    //----------------------------------
    // applicationConfiguration
    //----------------------------------

    IApplicationConfiguration getApplicationConfiguration();

    //----------------------------------
    // applicationPreferences
    //----------------------------------

    IApplicationPreferences getApplicationPreferences();

    //----------------------------------
    // applicationRoot
    //----------------------------------

    /**
     * The root directory of the application.
     * <p>
     * The CausticCore is setup to work around on root directory on the sdcard.
     * This allows all core applications to share the same directory layout and
     * access permissions.
     * </p>
     */
    File getApplicationRoot();

    /**
     * Returns the name of the application root directory.
     */
    String getApplicationName();

    // TODO (mschmalle) figure out how to listen for Activity changes in the app
    /**
     * The current running {@link Activity}, this may change during the life
     * cycle of the application but, the workspace root will never change.
     * 
     * @return
     */
    //Activity getActivity();
    Application getApplication();

    /**
     * Returns the workspaces single event dispatching manager.
     */
    // EventManager getEventManager();

    /**
     * The application's rack reference that is global passed the
     * {@link Activity}'s context.
     */
    IRack getRack();

    /**
     * The {@link IWorkspace}'s current registered and acting {@link IProject}.
     */
    IProject getProject();

    /**
     * The {@link Activity#getSharedPreferences(String, int)} editor.
     */
    SharedPreferences getSharedPreferences();

    //----------------------------------
    // properties
    //----------------------------------

    /**
     * Returns the <code>workspace.properties</code> properties.
     */
    // Properties getProperties();

    /**
     * The {@link IWorkspace} centric file service.
     * <p>
     * The service establishes application file access location patterns.
     */
    IFileService getFileService();

    /**
     * The sound generator that creates {@link IPart} instances that wrap the
     * {@link IRack}'s {@link IMachine} that are either created explicitly or
     * loaded implicitly by the {@link IRack#loadSong(String)} call.
     */
    ISoundGenerator getGenerator();

    /**
     * Called the start the workspace when ready to load and start running
     * services.
     * 
     * @throws CausticException
     */
    void startAndRun() throws CausticException;

    void stopAndShutdown() throws CausticException;

    /**
     * Creates a new {@link IProject} within the
     * {@link IWorkspace#getApplicationRoot()} project area and sets that
     * {@link IProject} as the current project within the workspace.
     * 
     * @param projectFile The absolute path to the project.
     * @return A new {@link IProject} located in the {@link IWorkspace}'s
     *         <code>/sdcard/MyApp/projects</code> directory.
     */
    IProject loadProject(File projectFile);

    /**
     * Restores the project's memento state.
     */
    void restoreProjectState();

    /**
     * Saves the current {@link IProject} to disk.
     * 
     * @throws CausticException
     */
    void save() throws CausticException;

    /**
     * Deletes an {@link IProject} from disk and notifies listeners.
     * 
     * @param project The {@link IProject} to delete.
     * @throws CausticException
     */
    void deleteProject(IProject project) throws CausticException;

    //--------------------------------------------------------------------------
    //
    //--------------------------------------------------------------------------

    public static class OnRackStateChangedEvent {

        private IRackSong song;

        private SongStateChangeKind kind;

        public final IRackSong getSong() {
            return song;
        }

        public final SongStateChangeKind getKind() {
            return kind;
        }

        public OnRackStateChangedEvent(IRackSong song, SongStateChangeKind kind) {
            this.song = song;
            this.kind = kind;
        }
    }

    public static class WorkspaceEvent {
        private IWorkspace workspace;

        public final IWorkspace getWorkspace() {
            return workspace;
        }

        public WorkspaceEvent(IWorkspace workspace) {
            this.workspace = workspace;
        }
    }

    /**
     * Called from {@link IWorkspace#save()}.
     */
    public static class OnWorkspaceQuickSaveEvent extends WorkspaceEvent {
        public OnWorkspaceQuickSaveEvent(IWorkspace workspace) {
            super(workspace);
        }
    }

    /**
     * Dispatched when the {@link IWorkspace} run() method has returned and the
     * workspace is in a stable state for a global application model or mediator
     * to take control of the applications process.
     */
    public static class OnWorkspaceRunEvent extends WorkspaceEvent {
        public OnWorkspaceRunEvent(IWorkspace workspace) {
            super(workspace);
        }
    }

    public static class OnWorkspaceProjectChangeEvent extends WorkspaceEvent {
        private final IProject project;

        private final IProject oldProject;

        public IProject getProject() {
            return project;
        }

        public IProject getOldProject() {
            return oldProject;
        }

        public OnWorkspaceProjectChangeEvent(IWorkspace workspace, IProject project,
                IProject oldProject) {
            super(workspace);
            this.project = project;
            this.oldProject = oldProject;
        }
    }

    public static class OnWorkspaceShutdownEvent extends WorkspaceEvent {
        public OnWorkspaceShutdownEvent(IWorkspace workspace) {
            super(workspace);
        }
    }

}
