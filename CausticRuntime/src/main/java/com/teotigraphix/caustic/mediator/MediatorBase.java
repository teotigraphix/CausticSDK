
package com.teotigraphix.caustic.mediator;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.teotigraphix.caustic.controller.IApplicationController;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerChange;
import com.teotigraphix.caustk.project.IProjectManager.ProjectManagerChangeKind;

// Mediators never dispatch events!, only listen and act with logic
// that could eventually be put in a Command
public abstract class MediatorBase implements ICaustkMediator {

    private ICaustkController controller;

    private IApplicationController applicationController;

    public final IApplicationController getApplicationController() {
        return applicationController;
    }

    public final ICaustkController getController() {
        return controller;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MediatorBase() {
    }

    @Inject
    public void setApplicationController(IApplicationController applicationController) {
        this.applicationController = applicationController;
        controller = applicationController.getController();
        applicationController.registerMeditor(this);
    }

    /**
     * Register {@link ICaustkController#getDispatcher()} events.
     * <p>
     * Called once when the controller is set.
     */
    protected void registerObservers() {
        controller.getDispatcher().register(OnProjectManagerChange.class,
                new EventObserver<OnProjectManagerChange>() {
                    @Override
                    public void trigger(OnProjectManagerChange object) {
                        if (object.getKind() == ProjectManagerChangeKind.CREATE) {
                            onProjectCreate();
                        } else if (object.getKind() == ProjectManagerChangeKind.LOAD) {
                            onProjectLoad();
                        } else if (object.getKind() == ProjectManagerChangeKind.SAVE) {
                            onProjectSave();
                        } else if (object.getKind() == ProjectManagerChangeKind.SAVE_COMPLETE) {
                        }
                    }
                });
    }

    @Override
    public void onRegisterObservers() {
        registerObservers();
    }

    /**
     * @see OnMediatorRegister
     */
    public abstract void onRegister();

    protected void onProjectSave() {
    }

    protected void onProjectLoad() {
    }

    protected void onProjectCreate() {
    }
}
