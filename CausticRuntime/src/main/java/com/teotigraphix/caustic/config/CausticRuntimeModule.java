
package com.teotigraphix.caustic.config;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.application.ApplicationProvider;
import com.teotigraphix.caustic.controller.ApplicationController;
import com.teotigraphix.caustic.controller.IApplicationController;
import com.teotigraphix.caustic.model.ApplicationModel;
import com.teotigraphix.caustic.model.IApplicationModel;
import com.teotigraphix.caustic.screen.IScreenManager;
import com.teotigraphix.caustic.screen.ScreenManager;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;

public abstract class CausticRuntimeModule extends AbstractModule {

    @Override
    protected void configure() {
        // Core 
        bind(ICaustkApplicationProvider.class).to(ApplicationProvider.class).in(Singleton.class);
        bind(IApplicationModel.class).to(ApplicationModel.class).in(Singleton.class);
        bind(IApplicationController.class).to(ApplicationController.class).in(Singleton.class);

        bind(IScreenManager.class).to(ScreenManager.class).in(Singleton.class);

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
