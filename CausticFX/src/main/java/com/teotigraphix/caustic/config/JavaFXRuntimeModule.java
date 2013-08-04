
package com.teotigraphix.caustic.config;

import com.google.inject.Singleton;
import com.teotigraphix.caustic.model.IStageModel;
import com.teotigraphix.caustic.model.StageModel;

public abstract class JavaFXRuntimeModule extends CausticRuntimeModule {

    @Override
    protected void configurePlatformRequirements() {
        bind(IStageModel.class).to(StageModel.class).in(Singleton.class);
    }

    @Override
    protected abstract void configureApplicationRequirements();

}
