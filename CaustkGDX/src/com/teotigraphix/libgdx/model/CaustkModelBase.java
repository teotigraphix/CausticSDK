
package com.teotigraphix.libgdx.model;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.controller.core.Dispatcher;
import com.teotigraphix.libgdx.controller.IApplicationController;

public abstract class CaustkModelBase implements ICaustkModel {

    private IDispatcher dispatcher;

    private ICaustkController controller;

    protected final ICaustkController getController() {
        return controller;
    }

    //    //----------------------------------
    //    // state
    //    //----------------------------------
    //
    //    private ModelState state;
    //
    //    public void setState(ModelState value) {
    //        state = value;
    //        getController().getComponent(IInjectorService.class).inject(state);
    //    }
    //
    //    public ModelState getState() {
    //        return state;
    //    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    private IApplicationController applicationController;

    @Inject
    public void setApplicationController(IApplicationController applicationController) {
        this.applicationController = applicationController;
        dispatcher = new Dispatcher();
        controller = applicationController.getController();
    }

    protected final IApplicationController getApplicationController() {
        return applicationController;
    }

    @Override
    public <T> void register(Class<T> event, EventObserver<T> observer) {
        dispatcher.register(event, observer);
    }

    @Override
    public void unregister(EventObserver<?> observer) {
        dispatcher.unregister(observer);
    }

    @Override
    public void trigger(Object event) {
        dispatcher.trigger(event);
    }

    @Override
    public void clear() {
        dispatcher.clear();
    }

    @Override
    public void onRegister() {
    }

    public abstract static class ModelState {

    }

}
