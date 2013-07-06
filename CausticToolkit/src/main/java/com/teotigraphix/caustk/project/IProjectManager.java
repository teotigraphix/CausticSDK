
package com.teotigraphix.caustk.project;

import java.io.File;
import java.io.IOException;

import com.teotigraphix.caustk.controller.ICaustkController;

public interface IProjectManager {

    /**
     * The root application directory, all {@link Project}s are stored in the
     * <code>applicationRoot/projects</code> directory.
     */
    File getApplicationRoot();

    /**
     * Returns the current {@link Project} instantiated by {@link #create(File)}
     * or {@link #load(File)}.
     */
    Project getProject();

    /**
     * Returns the single {@link SessionPreferences} instance for the project
     * session.
     */
    SessionPreferences getSessionPreferences();

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
     * @see OnProjectManagerCreate
     */
    Project create(File projectFile) throws IOException;

    /**
     * Loads a project from disk using the <code>.ctk</code> project format.
     * <p>
     * 
     * @param file The project <code>.ctk</code> file.
     * @return A fully loaded <code>.ctk</code> project state.
     * @throws IOException Project file does not exist
     * @see OnProjectManagerLoad
     */
    Project load(File file) throws IOException;

    /**
     * Saves a {@link Project} to disk using the project's file location.
     * 
     * @throws IOException
     * @see OnProjectManagerSave
     * @see OnProjectManagerSaveComplete
     */
    void save() throws IOException;

    /**
     * Returns whether the project exists and is a valid caustk project.
     * 
     * @param file The project {@link File} relative to the projects directory
     *            of the application.
     */
    boolean isProject(File file);

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

    /**
     * Dispatched when a project has been created and is getting registered with
     * the system for the first time.
     * 
     * @see ICaustkController#getDispatcher()
     */
    public static class OnProjectManagerCreate extends ProjectEvent {
        public OnProjectManagerCreate(Project project) {
            super(project);
        }
    }

    /**
     * Dispatched when a project has been loaded and has been deserialzed.
     * 
     * @see ICaustkController#getDispatcher()
     */
    public static class OnProjectManagerLoad extends ProjectEvent {
        public OnProjectManagerLoad(Project project) {
            super(project);
        }
    }

    /**
     * @see ICaustkController#getDispatcher()
     */
    public static class OnProjectManagerLoadComplete extends ProjectEvent {
        public OnProjectManagerLoadComplete(Project project) {
            super(project);
        }
    }

    /**
     * Dispatched when the project manager is about to save state.
     * <p>
     * Clients can listen to this event and save their state as necessary.
     * 
     * @see ICaustkController#getDispatcher()
     */
    public static class OnProjectManagerSave extends ProjectEvent {
        public OnProjectManagerSave(Project project) {
            super(project);
        }
    }

    /**
     * Dispatched when the project manager has completely saved all state.
     * 
     * @see ICaustkController#getDispatcher()
     */
    public static class OnProjectManagerSaveComplete extends ProjectEvent {
        public OnProjectManagerSaveComplete(Project project) {
            super(project);
        }
    }
}
