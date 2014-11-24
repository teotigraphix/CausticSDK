
package com.teotigraphix.gdx.app;

import com.teotigraphix.gdx.controller.IFileManager;
import com.teotigraphix.gdx.controller.IFileModel;

public abstract class AbstractProductModelAPI {

    private ProjectModel projectModel;

    protected ProjectModel getProjectModel() {
        return projectModel;
    }

    public AbstractProductModelAPI(ProjectModel projectModel) {
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
