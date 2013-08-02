
package com.teotigraphix.caustic.mediator;

import com.google.inject.Inject;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;

import javafx.scene.layout.Pane;

public abstract class DesktopMediatorBase extends MediatorBase {

    @Inject
    public DesktopMediatorBase(ICaustkApplicationProvider provider) {
        super(provider);
    }

    public abstract void create(Pane root);
}
