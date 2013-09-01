
package com.teotigraphix.libgdx.config;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.controller.ICaustkApplicationProvider;
import com.teotigraphix.caustk.service.IInjectorService;
import com.teotigraphix.libgdx.controller.ApplicationController;
import com.teotigraphix.libgdx.controller.ControllerProvider;
import com.teotigraphix.libgdx.controller.IApplicationController;
import com.teotigraphix.libgdx.controller.IControllerProvider;
import com.teotigraphix.libgdx.model.ApplicationModel;
import com.teotigraphix.libgdx.model.IApplicationModel;
import com.teotigraphix.libgdx.service.InjectorService;

public abstract class CausticRuntimeModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ICaustkApplicationProvider.class).to(ApplicationProvider.class).in(Singleton.class);
        bind(IApplicationModel.class).to(ApplicationModel.class).in(Singleton.class);
        bind(IApplicationController.class).to(ApplicationController.class).in(Singleton.class);
        bind(IControllerProvider.class).to(ControllerProvider.class).in(Singleton.class);
        bind(IInjectorService.class).to(InjectorService.class).in(Singleton.class);

        //bind(IScreenManager.class).to(ScreenManager.class).in(Singleton.class);

        configurePlatformRequirements();

        configureApplicationRequirements();
    }

    /**
     * The platform specific injections for either JavaFX or Android.
     */
    protected abstract void configurePlatformRequirements();

    /**
     * Application injections.
     * <p>
     * Required;
     * <ul>
     * <li>ICaustkConfiguration</li>
     * <li>ResourceBundle</li>
     * </ul>
     */
    protected abstract void configureApplicationRequirements();

}
