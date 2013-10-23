
package com.teotigraphix.libgdx.model;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.controller.core.Dispatcher;
import com.teotigraphix.libgdx.controller.ICaustkControllerProvider;

public abstract class CaustkModelBase implements ICaustkModel {

    private IDispatcher dispatcher;

    private ICaustkController controller;

    public final ICaustkController getController() {
        return controller;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    @Inject
    public void setApplicationController(ICaustkControllerProvider provider) {
        dispatcher = new Dispatcher();
        controller = provider.get();
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
