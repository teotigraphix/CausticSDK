
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

    ProjectPreferences getProjectPreferences();

    /**
     * Creates a {@link Project} from a <code>.caustic</code> file.
     * <p>
     * This method will NOT save the {@link Project}. This allows for clients to
     * add data entries before the initial save.
     * 
     * @param projectFile the relative path and name of the <code>.ctk</code>
     *            file without the <code>project</code> sub directory. This is
     *            the file path located within the projects app directory.
     * @return
     * @throws IOException
     */
    Project create(File projectFile) throws IOException;

    /**
     * Loads a project from disk using the <code>.ctk</code> project format.
     * <p>
     * The state is the same as if this {@link Project} was loaded from the
     * {@link #load(File)} method.
     * 
     * @param file The project <code>.ctk</code> file.
     * @return A fully loaded <code>.ctk</code> project state.
     * @throws IOException
     */
    Project load(File file) throws IOException;

    /**
     * Saves a {@link Project} to disk using the project's file location.
     * 
     * @throws IOException
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
    public static class OnProjectCreate extends ProjectEvent {
        public OnProjectCreate(Project project) {
            super(project);
        }
    }

    /**
     * Dispatched when a project has been loaded and has been deserialzed.
     * 
     * @see ICaustkController#getDispatcher()
     */
    public static class OnProjectLoad extends ProjectEvent {
        public OnProjectLoad(Project project) {
            super(project);
        }
    }

    /**
     * @see ICaustkController#getDispatcher()
     */
    public static class OnProjectLoadComplete extends ProjectEvent {
        public OnProjectLoadComplete(Project project) {
            super(project);
        }
    }

}
