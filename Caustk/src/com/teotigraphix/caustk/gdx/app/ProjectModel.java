
package com.teotigraphix.caustk.gdx.app;

import java.io.File;
import java.io.IOException;

import com.google.inject.Singleton;
import com.teotigraphix.caustk.gdx.app.api.ExportAPI;
import com.teotigraphix.caustk.gdx.app.api.MachineAPI;
import com.teotigraphix.caustk.gdx.app.controller.ViewManager;

@Singleton
public abstract class ProjectModel extends ApplicationComponent implements IProjectModel,
        IProjectModelWrite {

    private static final String TAG = "ProjectModel";

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private Project project;

    private ProjectState state;

    private MachineAPI machineAPI;

    private ExportAPI exportAPI;

    //--------------------------------------------------------------------------
    // Public Property :: API
    //--------------------------------------------------------------------------

    //----------------------------------
    // machineAPI
    //----------------------------------

    @Override
    public MachineAPI getMachineAPI() {
        return machineAPI;
    }

    public ExportAPI getExportAPI() {
        return exportAPI;
    }

    //----------------------------------
    // properties
    //----------------------------------

    @Override
    public ProjectProperties getProperties() {
        return project.getProperties();
    }

    //----------------------------------
    // project
    //----------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Project> T getProject() {
        return (T)project;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Project> void setProject(T project) throws IOException {
        T oldProject = (T)this.project;
        if (oldProject != null) {
            closeProject(oldProject);
        }

        log(TAG, "setProject() - " + project.getFile());
        this.project = project;

        if (!project.isCreated()) {
            createProject(project);
        } else {
            loadProject(project);
        }
    }

    @Override
    public File getProjectDirectory() {
        return getProject().getDirectory();
    }

    //----------------------------------
    // state
    //----------------------------------

    public ProjectState getState() {
        return state;
    }

    //----------------------------------
    // dirty
    //----------------------------------

    @Override
    public boolean isDirty() {
        return getApplication().isDirty();
    }

    @Override
    public void setDirty() {
        getApplication().setDirty();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ProjectModel() {
        machineAPI = new MachineAPI(this);
        exportAPI = new ExportAPI(this);
    }

    //--------------------------------------------------------------------------
    // Public Method :: API
    //--------------------------------------------------------------------------

    public void restore(ProjectState state) {
        this.state = state;

        ((ViewManager)getApplication().getViewManager()).restore(state);

        machineAPI.restore(state);
        exportAPI.restore(state);
    }

    @Override
    public void onEvent(Object kind) {
        ((ViewManager)getApplication().getViewManager()).onEvent(kind);
    }

    @Override
    public void save() throws IOException {
        saveProject(getProject());
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    private void createProject(Project project) throws IOException {
        log(TAG, "createProject()");
        getApplication().getApplicationStates().onProjectCreate(project);
        getApplication().getFileManager().setStartupProject(project);
        project.save();
    }

    private void loadProject(Project project) {
        log(TAG, "loadProject()");
        getApplication().getFileManager().setStartupProject(project);
        getApplication().getApplicationStates().onProjectLoad(project);
    }

    private void saveProject(Project project) throws IOException {
        log(TAG, "saveProject()");
        getApplication().getApplicationStates().onProjectSave(project);
        project.save();
    }

    private void closeProject(Project project) {
        log(TAG, "closeProject()");
        getApplication().getApplicationStates().onProjectClose(project);
    }

}
