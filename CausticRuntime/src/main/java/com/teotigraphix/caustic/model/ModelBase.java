
package com.teotigraphix.caustic.model;

import com.google.inject.Inject;
import com.teotigraphix.caustic.controller.IApplicationController;
import com.teotigraphix.caustk.application.Dispatcher;
import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkController;

public abstract class ModelBase implements ICaustkModel {

    private ICaustkController controller;

    private IDispatcher dispatcher;

    private IApplicationController applicationController;

    protected final IApplicationController getApplicationController() {
        return applicationController;
    }

    protected final ICaustkController getController() {
        return controller;
    }

    /**
     * The model's {@link IDispatcher} for local event dispatching.
     */
    @Override
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

    @Inject
    public void setApplicationController(IApplicationController applicationController) {
        this.applicationController = applicationController;
        this.controller = applicationController.getController();
        dispatcher = new Dispatcher();
        applicationController.registerModel(this);
    }

    public ModelBase() {
    }

    @Override
    public abstract void onShow();

    @Override
    public abstract void onRegister();
}
