
package com.teotigraphix.gdx.groove.app;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.app.ApplicationController;
import com.teotigraphix.gdx.app.ApplicationModel;
import com.teotigraphix.gdx.app.IApplicationController;
import com.teotigraphix.gdx.app.IApplicationModel;
import com.teotigraphix.gdx.controller.DialogManager;
import com.teotigraphix.gdx.controller.FileManager;
import com.teotigraphix.gdx.controller.FileModel;
import com.teotigraphix.gdx.controller.IDialogManager;
import com.teotigraphix.gdx.controller.IFileManager;
import com.teotigraphix.gdx.controller.IFileModel;
import com.teotigraphix.gdx.controller.IPreferenceManager;
import com.teotigraphix.gdx.controller.PreferenceManager;
import com.teotigraphix.gdx.controller.command.CommandManager;
import com.teotigraphix.gdx.controller.command.ICommandManager;
import com.teotigraphix.gdx.groove.ui.ContainerMap;
import com.teotigraphix.gdx.groove.ui.IContainerMap;

public class GrooveApplicationComponents implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(ICommandManager.class).to(CommandManager.class).in(Singleton.class);
        binder.bind(IDialogManager.class).to(DialogManager.class);
        binder.bind(IFileModel.class).to(FileModel.class);
        binder.bind(IFileManager.class).to(FileManager.class).in(Singleton.class);
        binder.bind(IPreferenceManager.class).to(PreferenceManager.class).in(Singleton.class);
        binder.bind(IApplicationController.class).to(ApplicationController.class)
                .in(Singleton.class);
        binder.bind(IApplicationModel.class).to(ApplicationModel.class).in(Singleton.class);

        binder.bind(IContainerMap.class).to(ContainerMap.class).in(Singleton.class);

    }

}
