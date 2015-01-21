
package com.teotigraphix.caustk.gdx.app;

import java.io.IOException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.gdx.app.api.ExportAPI;
import com.teotigraphix.caustk.gdx.app.api.MachineAPI;
import com.teotigraphix.caustk.gdx.controller.IFileManager;
import com.teotigraphix.caustk.gdx.controller.IFileModel;
import com.teotigraphix.caustk.gdx.controller.IViewManager;
import com.teotigraphix.caustk.gdx.controller.ViewManager;

@Singleton
public abstract class ProjectModel extends ApplicationComponent implements IProjectModel,
        IProjectModelWrite {

    private static final String TAG = "ProjectModel";

    //--------------------------------------------------------------------------
    // Inject :: Variables
    //--------------------------------------------------------------------------

    @Inject
    private IApplicationModel applicationModel;

    @Inject
    private IApplicationStateHandlers applicationStates;

    @Inject
    private IFileManager fileManager;

    @Inject
    private IFileModel fileModel;

    @Inject
    private IViewManager viewManager;

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
    // fileManager
    //----------------------------------

    public IFileManager getFileManager() {
        return fileManager;
    }

    //----------------------------------
    // fileModel
    //----------------------------------

    public IFileModel getFileModel() {
        return fileModel;
    }

    //----------------------------------
    // viewManager
    //----------------------------------

    @Override
    public IViewManager getViewManager() {
        return viewManager;
    }

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
        return applicationModel.isDirty();
    }

    @Override
    public void setDirty() {
        applicationModel.setDirty();
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

        ((ViewManager)viewManager).restore(state);

        machineAPI.restore(state);
        exportAPI.restore(state);
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
        applicationStates.onProjectCreate(project);
        fileManager.setStartupProject(project);
        project.save();
    }

    private void loadProject(Project project) {
        log(TAG, "loadProject()");
        fileManager.setStartupProject(project);
        applicationStates.onProjectLoad(project);
    }

    private void saveProject(Project project) throws IOException {
        log(TAG, "saveProject()");
        applicationStates.onProjectSave(project);
        project.save();
    }

    private void closeProject(Project project) {
        log(TAG, "closeProject()");
        applicationStates.onProjectClose(project);
    }

}
