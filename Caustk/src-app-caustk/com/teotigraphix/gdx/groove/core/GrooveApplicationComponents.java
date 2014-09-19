
package com.teotigraphix.gdx.groove.core;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.app.ApplicationController;
import com.teotigraphix.gdx.app.ApplicationModel;
import com.teotigraphix.gdx.app.FileManager;
import com.teotigraphix.gdx.app.IApplicationController;
import com.teotigraphix.gdx.app.IApplicationModel;
import com.teotigraphix.gdx.app.IFileManager;
import com.teotigraphix.gdx.controller.FileModel;
import com.teotigraphix.gdx.controller.IFileModel;
import com.teotigraphix.gdx.controller.IPreferenceManager;
import com.teotigraphix.gdx.controller.PreferenceManager;
import com.teotigraphix.gdx.groove.ui.ContainerMap;
import com.teotigraphix.gdx.groove.ui.IContainerMap;

public class GrooveApplicationComponents implements Module {

    @Override
    public void configure(Binder binder) {

        binder.bind(IFileModel.class).to(FileModel.class);
        binder.bind(IFileManager.class).to(FileManager.class).in(Singleton.class);
        binder.bind(IPreferenceManager.class).to(PreferenceManager.class).in(Singleton.class);
        binder.bind(IApplicationController.class).to(ApplicationController.class)
                .in(Singleton.class);
        binder.bind(IApplicationModel.class).to(ApplicationModel.class).in(Singleton.class);

        binder.bind(IContainerMap.class).to(ContainerMap.class).in(Singleton.class);

    }

}
