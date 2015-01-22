
package com.teotigraphix.caustk.gdx.app;

import java.io.File;

import com.google.inject.Singleton;
import com.teotigraphix.caustk.gdx.app.api.ExportAPI;
import com.teotigraphix.caustk.gdx.app.api.MachineAPI;
import com.teotigraphix.caustk.gdx.app.api.ProjectAPI;
import com.teotigraphix.caustk.gdx.app.controller.ViewManager;

@Singleton
public abstract class ProjectModel extends ApplicationComponent implements IProjectModel {

    @SuppressWarnings("unused")
    private static final String TAG = "ProjectModel";

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private ProjectState state;

    private ProjectAPI projectAPI;

    private MachineAPI machineAPI;

    private ExportAPI exportAPI;

    //--------------------------------------------------------------------------
    // Public Property :: API
    //--------------------------------------------------------------------------

    protected <T extends Project> T getProject() {
        return getProjectAPI().getProject();
    }

    //----------------------------------
    // machineAPI
    //----------------------------------

    @Override
    public ProjectAPI getProjectAPI() {
        return projectAPI;
    }

    @Override
    public MachineAPI getMachineAPI() {
        return machineAPI;
    }

    @Override
    public ExportAPI getExportAPI() {
        return exportAPI;
    }

    //----------------------------------
    // properties
    //----------------------------------

    @Override
    public ProjectProperties getProperties() {
        return getProject().getProperties();
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
        projectAPI = new ProjectAPI(this);
        machineAPI = new MachineAPI(this);
        exportAPI = new ExportAPI(this);
    }

    //--------------------------------------------------------------------------
    // Public Method :: API
    //--------------------------------------------------------------------------

    /**
     * Called from App.render(), SceneManager.preRender()/setScene(),
     * Scene.start().
     * 
     * @param state
     */
    public void restore(ProjectState state) {
        this.state = state;

        ((ViewManager)getApplication().getViewManager()).restore(state);

        projectAPI.restore(state);
        machineAPI.restore(state);
        exportAPI.restore(state);
    }

    @Override
    public void onEvent(Object kind) {
        ((ViewManager)getApplication().getViewManager()).onEvent(kind);
    }

}
