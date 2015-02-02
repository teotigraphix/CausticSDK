
package com.teotigraphix.caustk.gdx.app;

import com.teotigraphix.caustk.core.ICaustkLogger;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.core.internal.CaustkRuntime;
import com.teotigraphix.caustk.gdx.app.controller.IFileManager;
import com.teotigraphix.caustk.gdx.app.controller.IFileModel;
import com.teotigraphix.caustk.gdx.app.controller.command.ICommandManager;
import com.teotigraphix.caustk.node.RackInstance;

public abstract class AbstractProjectModelAPI {

    private CaustkApplication application;

    private ProjectModel projectModel;

    protected ProjectModel getProjectModel() {
        return projectModel;
    }

    public AbstractProjectModelAPI(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    protected final CaustkApplication getApplication() {
        if (application == null)
            application = (CaustkApplication)CaustkRuntime.getInstance().getApplication();
        return application;
    }

    protected IApplicationStateHandlers getApplicationStates() {
        return getApplication().getApplicationStates();
    }

    protected final ICaustkLogger getLogger() {
        return getApplication().getLogger();
    }

    protected ICaustkRack getRack() {
        return projectModel.getProject().getRack();
    }

    protected RackInstance getRackInstance() {
        return projectModel.getProject().getRackInstance();
    }

    protected ProjectProperties getProperties() {
        return projectModel.getProperties();
    }

    protected IFileManager getFileManager() {
        return getApplication().getFileManager();
    }

    protected IFileModel getFileModel() {
        return getApplication().getFileModel();
    }

    protected void post(Object event) {
        projectModel.getEventBus().post(event);
    }

    //    protected Project getProject() {
    //        return projectModel.getProject();
    //    }

    protected final ICommandManager getCommandManager() {
        return getApplication().getCommandManager();
    }

    public abstract void restore(ProjectState state);
}
