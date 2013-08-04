
package com.teotigraphix.caustic.mediator;

import com.teotigraphix.caustic.model.ICaustkModel;

/**
 * The mediator listens to model and application events, updates it's managed
 * user interface accordingly.
 * <p>
 * Mediator startup phase;
 * <ul>
 * <li>{@link #onRegisterObservers()}</li>
 * <li>{@link #onRegister()}</li>
 * </ul>
 * The mediator's {@link #onRegister()} is called after all model's have been
 * registered.
 */
public interface ICaustkMediator {

    /**
     * Called before the application controller has it's start() invoked.
     * <p>
     * Register all application and model events.
     * <p>
     * All injections are complete and access to framework and application
     * models are non <code>null</code>.
     * <p>
     * The main application and project state has not been loaded yet.
     */
    void onRegisterObservers();

    /**
     * Access to framework and application state will be non <code>null</code>.
     * <p>
     * The {@link #onRegister()} method is called after the
     * {@link ICaustkModel#onRegister()}.
     * <p>
     * Register all user interface component listeners.
     */
    void onRegister();

}
