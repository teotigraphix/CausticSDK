
package com.teotigraphix.gdx.core;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.teotigraphix.caustk.core.ICaustkLogger;
import com.teotigraphix.gdx.app.IApplication;

public class LogProvider implements Provider<ICaustkLogger> {

    @Inject
    IApplication application;

    @Override
    public ICaustkLogger get() {
        return application.getLogger();
    }
}
