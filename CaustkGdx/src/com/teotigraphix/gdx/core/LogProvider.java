
package com.teotigraphix.gdx.core;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.teotigraphix.caustk.core.ICaustkLogger;
import com.teotigraphix.gdx.app.ICaustkApplication;

public class LogProvider implements Provider<ICaustkLogger> {

    @Inject
    ICaustkApplication application;

    @Override
    public ICaustkLogger get() {
        return null;// TODO application.getLogger();
    }
}
