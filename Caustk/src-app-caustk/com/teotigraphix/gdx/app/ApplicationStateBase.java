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

package com.teotigraphix.gdx.app;

import java.io.IOException;

import com.google.inject.Inject;
import com.teotigraphix.caustk.core.CaustkProject;

public abstract class ApplicationStateBase extends ApplicationComponent implements
        IApplicationState, IApplicationStateHandlers {

    private static final String TAG = "ApplicationStateBase";

    @Inject
    private IApplicationModel applicationModel;

    @Inject
    private IFileManager fileManager;

    @Override
    protected String getPreferenceId() {
        throw new IllegalStateException();
    }

    public ApplicationStateBase() {
    }

    // Called from ApplicationController.startup() before startScene() and startUI()
    // everything needs to be synchronous, no app events
    @Override
    public void startup() throws IOException {
        getApplication().getEventBus().register(this);

        log(TAG, "startup()");

        fileManager.setupApplicationDirectory();

        CaustkProject project = null;

        log(TAG, "Find last project path from preferences.");

        project = fileManager.createOrLoadStartupProject();

        // get onProjectCreate() and onProjectLoad() callbacks from ApplicationModel
        applicationModel.setProject(project);
    }

    @Override
    public abstract void onProjectCreate(CaustkProject project);

    @Override
    public abstract void onProjectLoad(CaustkProject project);

    @Override
    public abstract void onProjectSave(CaustkProject project);

    @Override
    public abstract void onProjectClose(CaustkProject project);

    @Override
    public void startUI() {
        log(TAG, "startUI()");
        onStartUI();
    }

    @Override
    public void restartUI() {
        log(TAG, "restartUI()");

        try {
            applicationModel.setProject(applicationModel.getProject());
        } catch (IOException e) {
            e.printStackTrace();
        }

        onRestartUI();
    }

    protected abstract void onStartUI();

    protected abstract void onRestartUI();
}
