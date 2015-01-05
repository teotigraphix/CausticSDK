////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.gdx.groove.app;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.gdx.app.ApplicationController;
import com.teotigraphix.caustk.gdx.app.ApplicationModel;
import com.teotigraphix.caustk.gdx.app.IApplicationController;
import com.teotigraphix.caustk.gdx.app.IApplicationModel;
import com.teotigraphix.caustk.gdx.controller.DialogManager;
import com.teotigraphix.caustk.gdx.controller.FileManager;
import com.teotigraphix.caustk.gdx.controller.FileModel;
import com.teotigraphix.caustk.gdx.controller.IDialogManager;
import com.teotigraphix.caustk.gdx.controller.IFileManager;
import com.teotigraphix.caustk.gdx.controller.IFileModel;
import com.teotigraphix.caustk.gdx.controller.IPreferenceManager;
import com.teotigraphix.caustk.gdx.controller.PreferenceManager;
import com.teotigraphix.caustk.gdx.controller.command.CommandManager;
import com.teotigraphix.caustk.gdx.controller.command.ICommandManager;
import com.teotigraphix.caustk.gdx.groove.ui.ContainerMap;
import com.teotigraphix.caustk.gdx.groove.ui.IContainerMap;

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
