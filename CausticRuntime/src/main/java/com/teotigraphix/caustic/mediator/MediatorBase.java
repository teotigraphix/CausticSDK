
package com.teotigraphix.caustic.mediator;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerChange;
import com.teotigraphix.caustk.project.IProjectManager.ProjectManagerChangeKind;

// Mediators never dispatch events!, only listen and act with logic
// that could eventually be put in a Command
public class MediatorBase {

    private ICaustkController controller;

    protected void setController(ICaustkController value) {
        controller = value;
    }

    public final ICaustkController getController() {
        return controller;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    // for FXMLControllers
    public MediatorBase() {
    }

    // @Inject
    public MediatorBase(ICaustkApplicationProvider provider) {
        setController(provider.get().getController());
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

        controller.getDispatcher().register(OnMediatorRegister.class,
                new EventObserver<OnMediatorRegister>() {
                    @Override
                    public void trigger(OnMediatorRegister object) {
                        onRegister();
                    }
                });
    }

    /**
     * Called before the application controller has it's start() invoked.
     */
    public void preinitialize() {
        registerObservers();
    }

    /**
     * @see OnMediatorRegister
     */
    public void onRegister() {
    }

    protected void onProjectSave() {
    }

    protected void onProjectLoad() {
    }

    protected void onProjectCreate() {
    }

    /**
     * Fired at the very end of the startup sequence, all application state is
     * final and the window is just about to show.
     */
    public static class OnMediatorRegister {

    }
}
