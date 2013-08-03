
package com.teotigraphix.caustic.mediator;

import java.io.IOException;

import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;

import javafx.scene.layout.Pane;

public abstract class DesktopMediatorBase extends MediatorBase {

    @Inject
    protected GuiceFXMLLoader loader;

    @Inject
    public DesktopMediatorBase(ICaustkApplicationProvider provider) {
        super(provider);
    }

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
