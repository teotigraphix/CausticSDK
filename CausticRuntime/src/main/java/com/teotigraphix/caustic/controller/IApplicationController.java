
package com.teotigraphix.caustic.controller;

import java.io.IOException;

import com.teotigraphix.caustic.mediator.ICaustkMediator;
import com.teotigraphix.caustic.model.ICaustkModel;
import com.teotigraphix.caustk.controller.ICaustkController;

public interface IApplicationController {

    ICaustkController getController();

    void start() throws IOException;

    void show();

    /**
     * Registers a model against the application controller.
     * <p>
     * Clients do not call this method, all registration happens automatically.
     * 
     * @param model The model to register.
     */
    void registerModel(ICaustkModel model);

    /**
     * Calls {@link ICaustkModel#onRegister()} on all registered models.
     * <p>
     * Main application calls this method in the startup sequence.
     */
    void registerModels();

    void registerMeditor(ICaustkMediator mediator);

    void registerMediatorObservers();

    void registerMeditors();

}
