
package com.teotigraphix.libraryeditor.config;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.application.ApplicationProvider;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.ICaustkConfiguration;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        // Binds our resource bundle that contains localized Strings
        //        bind(ResourceBundle.class).annotatedWith(Names.named(Constants.I18L_RESOURCES)).toInstance(
        //                ResourceBundle.getBundle(MainApplication.class.getName()));

        bind(ICaustkConfiguration.class).to(ApplicationConfiguration.class).in(Singleton.class);
        bind(ICaustkApplicationProvider.class).to(ApplicationProvider.class).in(Singleton.class);

    }

}
