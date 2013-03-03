
package com.teotigraphix.caustic.test;

import roboguice.RoboGuice;
import android.app.Application;

import com.google.inject.Module;

public class CausticSDKApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        createModule();
    }

    protected void createModule() {
        Module module = createApplicationModule();
        if (module != null) {
            loadApplicationModule(this, module);
        }
    }

    private void loadApplicationModule(Application application, Module module) {
        RoboGuice.setBaseApplicationInjector(application, RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(application), module);
    }

    protected Module createApplicationModule() {
        return new DefaultCausticModule();
    }
}
