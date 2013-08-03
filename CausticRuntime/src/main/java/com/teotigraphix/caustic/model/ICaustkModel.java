
package com.teotigraphix.caustic.model;

import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkController;

/**
 * A model that is {@link ICaustkController} aware and registered in the
 * application with the {@link Injector} binder as a {@link Singleton}.
 */
public interface ICaustkModel {

    /**
     * The model's local dispatcher.
     * <p>
     * Most if not all events dispatched from a model are local, any that are
     * dispatched through the {@link ICaustkController#getDispatcher()} will be
     * documented on the model's event class.
     */
    IDispatcher getDispatcher();

    void onRegister();

    /**
     * The last phase of startup, where mediators have register, create ui and
     * are ready to get the final update from model events.
     * <p>
     * All state the has been deserialized from the project session will get
     * "executed" in the model here, the mediators will catch the model events
     * and update the user interface according to the update logic.
     */
    void onShow();
}
