////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.internal.controller.activity;

import roboguice.inject.ContextSingleton;
import android.app.Activity;

import com.teotigraphix.caustic.controller.IActivityController;
import com.teotigraphix.caustic.internal.command.project.LoadProjectCommand;
import com.teotigraphix.caustic.internal.command.startup.RegisterMainLayoutCommand;
import com.teotigraphix.caustic.internal.router.BaseRouterClient;

/**
 * Base class for all controllers decorating an {@link Activity}.
 */
@ContextSingleton
public abstract class ActivityController extends BaseRouterClient implements IActivityController {

    public ActivityController() {
    }

    @Override
    protected void registerCommands() {
        addCommand(REGISTER_MAIN_LAYOUT, RegisterMainLayoutCommand.class);
        addCommand(LOAD_PROJECT, LoadProjectCommand.class);
    }
}
