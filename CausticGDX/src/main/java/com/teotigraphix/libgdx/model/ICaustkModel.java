
package com.teotigraphix.libgdx.model;

import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;

/**
 * A model that is {@link ICaustkController} aware and registered in the
 * application with the {@link Injector} binder as a {@link Singleton}.
 * <p>
 * Model startup phase; 
 * <ul>
 * <li>{@link #onRegister()}</li>
 * <li>{@link #onShow()}</li>
 * </ul>
 */
public interface ICaustkModel {
    
    boolean isInitialized();
    
    /**
     * The model's local dispatcher.
     * <p>
     * Most if not all events dispatched from a model are local, any that are
     * dispatched through the {@link ICaustkController#getDispatcher()} will be
     * documented on the model's event class.
     */
    IDispatcher getDispatcher();

    /**
     * The model's registration phase where state is created from deserialzed
     * project state.
     * <p>
     * Mediators have not had their {@link ICaustkMediator#onRegister()} called,
     * the {@link #onShow()} method is where model's update deserialized state.
     */
    void onRegister();

    /**
     * The last phase of startup, where mediators have register, create ui and
     * are ready to get the final update from model events.
     * <p>
     * All state the has been deserialized from the project session will get
     * "executed" in the model here, the mediators will catch the model events
     * and update the user interface according to the update logic.
     * <p>
     * Any logic that gets executed here should not depend on an state changes
     * from within this phase.
     */
    void onShow();

    void save();
}
