
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

}
