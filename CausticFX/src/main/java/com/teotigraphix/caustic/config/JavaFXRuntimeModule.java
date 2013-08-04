
package com.teotigraphix.caustic.config;

import com.google.inject.Singleton;
import com.teotigraphix.caustic.application.AppPreferenceManager;
import com.teotigraphix.caustic.application.IApplicationPreferences;
import com.teotigraphix.caustic.application.IPreferenceManager;
import com.teotigraphix.caustic.application.PreferenceManager;
import com.teotigraphix.caustic.model.IStageModel;
import com.teotigraphix.caustic.model.StageModel;

public abstract class JavaFXRuntimeModule extends CausticRuntimeModule {

    @Override
    protected void configurePlatformRequirements() {
        bind(IStageModel.class).to(StageModel.class).in(Singleton.class);
        bind(IPreferenceManager.class).to(PreferenceManager.class).in(Singleton.class);
        bind(IApplicationPreferences.class).to(AppPreferenceManager.class).in(Singleton.class);
    }

    @Override
    protected abstract void configureApplicationRequirements();

}
