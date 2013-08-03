
package com.teotigraphix.caustic.mediator;

import java.io.IOException;

import javafx.scene.layout.Pane;

import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;

public abstract class DesktopMediatorBase extends MediatorBase implements ICaustkMediator {

    @Inject
    protected GuiceFXMLLoader loader;

    public abstract void create(Pane root);

    protected Pane load(String resource) {
        Pane instance = null;
        try {
            instance = loader.load(getClass().getResource(resource)).getRoot();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instance;
    }
}
