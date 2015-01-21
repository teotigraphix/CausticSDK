
package com.teotigraphix.caustk.gdx.app;

import com.teotigraphix.caustk.gdx.app.controller.IFileManager;
import com.teotigraphix.caustk.gdx.app.controller.IFileModel;
import com.teotigraphix.caustk.node.RackNode;

public abstract class AbstractProjectModelAPI {

    private CaustkApplication application;

    private ProjectModel projectModel;

    protected ProjectModel getProjectModel() {
        return projectModel;
    }

    public AbstractProjectModelAPI(ProjectModel projectModel) {
        this.projectModel = projectModel;
        application = projectModel.getApplication();
    }

    protected void save() {
        // XXX not right
        projectModel.getApplication().save();
    }

    protected RackNode getRackNode() {
        return projectModel.getProject().getRackNode();
    }

    protected ProjectProperties getProperties() {
        return projectModel.getProperties();
    }

    protected IFileManager getFileManager() {
        return application.getFileManager();
    }

    protected IFileModel getFileModel() {
        return application.getFileModel();
    }

    protected void post(Object event) {
        projectModel.getEventBus().post(event);
    }

    protected Project getProject() {
        return projectModel.getProject();
    }

    public abstract void restore(ProjectState state);
}
