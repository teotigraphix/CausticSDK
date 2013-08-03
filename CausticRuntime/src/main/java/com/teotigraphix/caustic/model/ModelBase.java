
package com.teotigraphix.caustic.model;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustic.mediator.MediatorBase;
import com.teotigraphix.caustic.model.IApplicationModel.OnApplicationModelRun;
import com.teotigraphix.caustk.application.Dispatcher;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerChange;
import com.teotigraphix.caustk.project.IProjectManager.ProjectManagerChangeKind;

public class ModelBase implements ICaustkModel {

    private ICaustkController controller;

    private IDispatcher dispatcher;

    public final ICaustkController getController() {
        return controller;
    }

    /**
     * The model's {@link IDispatcher} for local event dispatching.
     */
    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    /**
     * Triggers and event through this model's {@link #getDispatcher()}.
     * 
     * @param event The event to dispatch.
     */
    protected void trigger(Object event) {
        dispatcher.trigger(event);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    // @Inject
    public ModelBase(ICaustkApplicationProvider provider) {
        controller = provider.get().getController();
        dispatcher = new Dispatcher();

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

        controller.getDispatcher().register(OnModelRegister.class,
                new EventObserver<OnModelRegister>() {
                    @Override
                    public void trigger(OnModelRegister object) {
                        onRegister();
                    }
                });

        controller.getDispatcher().register(OnApplicationModelRun.class,
                new EventObserver<OnApplicationModelRun>() {
                    @Override
                    public void trigger(OnApplicationModelRun object) {
                        onShow();
                    }
                });

    }

    /**
     * The last phase of startup, where mediators have register, create ui and
     * are ready to get the final update from model events.
     * <p>
     * All state the has been deserialized from the project session will get
     * "executed" in the model here, the mediators will catch the model events
     * and update the user interface according to the update logic.
     */
    protected void onShow() {
    }

    public void onRegister() {
    }

    protected void onProjectSave() {
    }

    protected void onProjectLoad() {
    }

    protected void onProjectCreate() {
    }

    /**
     * Fired just before the {@link MediatorBase#onRegister()} is called.
     */
    public static class OnModelRegister {

    }
}
