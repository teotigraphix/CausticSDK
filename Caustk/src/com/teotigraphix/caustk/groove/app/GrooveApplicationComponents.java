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

package com.teotigraphix.caustk.groove.app;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.gdx.app.controller.DialogManager;
import com.teotigraphix.caustk.gdx.app.controller.FileManager;
import com.teotigraphix.caustk.gdx.app.controller.FileModel;
import com.teotigraphix.caustk.gdx.app.controller.IDialogManager;
import com.teotigraphix.caustk.gdx.app.controller.IFileManager;
import com.teotigraphix.caustk.gdx.app.controller.IFileModel;
import com.teotigraphix.caustk.gdx.app.controller.IPreferenceManager;
import com.teotigraphix.caustk.gdx.app.controller.PreferenceManager;
import com.teotigraphix.caustk.gdx.app.controller.command.CommandManager;
import com.teotigraphix.caustk.gdx.app.controller.command.ICommandManager;
import com.teotigraphix.caustk.gdx.app.ui.ContainerMap;
import com.teotigraphix.caustk.gdx.app.ui.IContainerMap;

public class GrooveApplicationComponents implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(ICommandManager.class).to(CommandManager.class).in(Singleton.class);
        binder.bind(IDialogManager.class).to(DialogManager.class).in(Singleton.class);
        binder.bind(IFileModel.class).to(FileModel.class).in(Singleton.class);
        binder.bind(IFileManager.class).to(FileManager.class).in(Singleton.class);
        binder.bind(IPreferenceManager.class).to(PreferenceManager.class).in(Singleton.class);
        binder.bind(IContainerMap.class).to(ContainerMap.class).in(Singleton.class);

    }

}
