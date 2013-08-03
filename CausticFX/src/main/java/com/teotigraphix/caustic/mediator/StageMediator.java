
package com.teotigraphix.caustic.mediator;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.model.IApplicationModel;
import com.teotigraphix.caustic.model.IApplicationModel.OnApplicationModelDirtyChanged;
import com.teotigraphix.caustic.model.IStageModel;

@Singleton
public class StageMediator extends MediatorBase implements ICaustkMediator {

    @Inject
    IStageModel stageModel;

    @Inject
    IApplicationModel applicationModel;

    public StageMediator() {
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();
        applicationModel.getDispatcher().register(OnApplicationModelDirtyChanged.class,
                new EventObserver<OnApplicationModelDirtyChanged>() {
                    @Override
                    public void trigger(OnApplicationModelDirtyChanged object) {
                        onApplicationModelDirtyChangedHandler();
                    }
                });
    }

    protected void onApplicationModelDirtyChangedHandler() {
        stageModel.refreshTitle();
    }

    //--------------------------------------------------------------------------
    // Project :: Events
    //--------------------------------------------------------------------------

    @Override
    protected void onProjectCreate() {
        String projectName = getController().getProjectManager().getProject().getFile().getPath();
        stageModel.setTitle(projectName);
    }

    @Override
    protected void onProjectLoad() {
        String projectName = getController().getProjectManager().getProject().getFile().getPath();
        stageModel.setTitle(projectName);
    }

    @Override
    public void onRegister() {
        // TODO Auto-generated method stub

    }
}
