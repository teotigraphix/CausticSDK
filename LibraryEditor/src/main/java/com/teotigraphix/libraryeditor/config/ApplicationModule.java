
package com.teotigraphix.libraryeditor.config;

import java.io.File;
import java.util.ResourceBundle;

import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.teotigraphix.caustic.config.JavaFXRuntimeModule;
import com.teotigraphix.caustk.application.CaustkConfigurationBase;
import com.teotigraphix.caustk.application.ICaustkConfiguration;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.libraryeditor.LibraryEditorApplication;

public class ApplicationModule extends JavaFXRuntimeModule {

    @Override
    protected void configureApplicationRequirements() {
        // Application
        bind(ICaustkConfiguration.class).to(ApplicationConfiguration.class).in(Singleton.class);

        // Binds our resource bundle that contains localized Strings
        bind(ResourceBundle.class).annotatedWith(Names.named("resources")).toInstance(
                ResourceBundle.getBundle(LibraryEditorApplication.class.getName()));
    }

    public static class ApplicationConfiguration extends CaustkConfigurationBase {

        @Override
        public String getApplicationId() {
            return "libraryeditor";
        }

        @Override
        public void setCausticStorage(File value) {
            super.setCausticStorage(value);
            RuntimeUtils.STORAGE_ROOT = value.getAbsolutePath();
        }

        public ApplicationConfiguration() {
            super();
        }
    }
}
