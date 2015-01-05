
package com.teotigraphix.caustk.gdx.app;

import com.teotigraphix.caustk.gdx.controller.IFileManager;
import com.teotigraphix.caustk.gdx.controller.IFileModel;

public abstract class AbstractProjectModelAPI {

    private ProjectModel projectModel;

    protected ProjectModel getProjectModel() {
        return projectModel;
    }

    public AbstractProjectModelAPI(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    protected ProjectProperties getProperties() {
        return projectModel.getProperties();
    }

    protected IFileManager getFileManager() {
        return projectModel.getFileManager();
    }

    protected IFileModel getFileModel() {
        return projectModel.getFileModel();
    }

    protected void post(Object event) {
        projectModel.getEventBus().post(event);
    }

    protected Project getProject() {
        return projectModel.getProject();
    }

    public abstract void restore(ProjectState state);
}
