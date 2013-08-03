
package com.teotigraphix.libraryeditor.config;

import java.util.ResourceBundle;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.teotigraphix.caustic.application.ApplicationProvider;
import com.teotigraphix.caustic.controller.ApplicationController;
import com.teotigraphix.caustic.controller.IApplicationController;
import com.teotigraphix.caustic.model.ApplicationModel;
import com.teotigraphix.caustic.model.IApplicationModel;
import com.teotigraphix.caustic.model.IStageModel;
import com.teotigraphix.caustic.model.StageModel;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.ICaustkConfiguration;
import com.teotigraphix.libraryeditor.LibraryEditorApplication;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        // Core
        bind(IApplicationModel.class).to(ApplicationModel.class).in(Singleton.class);
        bind(IApplicationController.class).to(ApplicationController.class).in(Singleton.class);

        // JavaFX
        bind(IStageModel.class).to(StageModel.class).in(Singleton.class);

        // Binds our resource bundle that contains localized Strings
        bind(ResourceBundle.class).annotatedWith(Names.named("resources")).toInstance(
                ResourceBundle.getBundle(LibraryEditorApplication.class.getName()));

        // Application
        bind(ICaustkConfiguration.class).to(ApplicationConfiguration.class).in(Singleton.class);
        bind(ICaustkApplicationProvider.class).to(ApplicationProvider.class).in(Singleton.class);

    }

}
