
package com.teotigraphix.caustic.ui.controller;

import javafx.scene.layout.Pane;

import com.teotigraphix.caustic.mediator.MediatorBase;

public abstract class FXControllerBase extends MediatorBase {

    public FXControllerBase() {
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();
        // XXX for now this mimics what a normal non FX mediator does
        // all UI components are initialized and listened to here
        create(null);
    }

    public abstract void create(Pane root);

}
