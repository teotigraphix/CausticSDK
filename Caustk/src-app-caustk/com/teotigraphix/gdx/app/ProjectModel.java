
package com.teotigraphix.gdx.app;

import java.io.IOException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.controller.IFileManager;
import com.teotigraphix.gdx.controller.IFileModel;
import com.teotigraphix.gdx.controller.IViewManager;

@Singleton
public class ProjectModel extends ApplicationComponent implements IProjectModel, IProjectModelWrite {

    private static final String TAG = "ProjectModel";

    @Inject
    private IApplicationStateHandlers applicationStates;

    @Inject
    private IFileManager fileManager;

    @Inject
    private IViewManager viewManager;

    @Inject
    private IFileModel fileModel;

    private ProjectModelMachineAPI machineAPI;

    public IFileManager getFileManager() {
        return fileManager;
    }

    public IFileModel getFileModel() {
        return fileModel;
    }

    @Override
    public ProjectModelMachineAPI getMachineAPI() {
        return machineAPI;
    }

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private Project project;

    private ProjectState state;

    public ProjectState getState() {
        return state;
    }

    //----------------------------------
    // project
    //----------------------------------

    @Override
    public ProjectProperties getProperties() {
        return project.getProperties();
    }

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
    public void save() {
        saveProject(getProject());
    }

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

    private void saveProject(Project project) {
        log(TAG, "saveProject()");
        applicationStates.onProjectSave(project);
        try {
            project.save();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void closeProject(Project project) {
        log(TAG, "closeProject()");
        applicationStates.onProjectClose(project);
    }

    //--------------------------------------------------------------------------
    // Public Property :: API
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ProjectModel() {
        machineAPI = new ProjectModelMachineAPI(this);
    }

    @Override
    public void restore(ProjectState state) {
        this.state = state;

        viewManager.restore(state);

        getEventBus().post(new ProjectModelEvent(ProjectModelEventKind.SceneViewChange, this));
        getEventBus().post(new ProjectModelEvent(ProjectModelEventKind.ViewChange, this));

        machineAPI.restore(state);
    }

}
