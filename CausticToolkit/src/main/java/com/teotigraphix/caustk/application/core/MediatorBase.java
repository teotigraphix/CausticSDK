
package com.teotigraphix.caustk.application.core;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerChange;
import com.teotigraphix.caustk.project.IProjectManager.ProjectManagerChangeKind;

// Mediators never dispatch events!, only listen and act with logic
// that could eventually be put in a Command
public class MediatorBase {
    private ICaustkController controller;

    public final ICaustkController getController() {
        return controller;
    }

    //    protected IDispatcher getDispatcher() {
    //        return controller.getDispatcher();
    //    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    // @Inject
    public MediatorBase(ICaustkApplicationProvider provider) {
        controller = provider.get().getController();

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

    public void initialize() {
    }

    public void onRegister() {
    }

    protected void onProjectSave() {
    }

    protected void onProjectLoad() {
    }

    protected void onProjectCreate() {
    }
}
