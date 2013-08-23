
package com.teotigraphix.libgdx.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.controller.ICaustkController;

@Singleton
public class ControllerProvider implements IControllerProvider {

    @Inject
    ICaustkApplicationProvider provider;

    public ControllerProvider() {
    }

    @Override
    public ICaustkController get() {
        return provider.get().getController();
    }

}
