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

package com.teotigraphix.caustk.gdx.app;

import java.io.IOException;

import com.google.inject.Singleton;
import com.teotigraphix.caustk.gdx.controller.ViewManager;

@Singleton
public class ApplicationState extends ApplicationComponent implements IApplicationState,
        IApplicationStateHandlers {

    private static final String TAG = "ApplicationStateImpl";

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private String APP_PREFERENCES;

    @Override
    protected String getPreferenceId() {
        return APP_PREFERENCES;
    }

    //--------------------------------------------------------------------------
    // Public :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    //
    //----------------------------------

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ApplicationState() {
        super();
    }

    //--------------------------------------------------------------------------
    // Public IStateModel :: Methods
    //--------------------------------------------------------------------------

    // Called from ApplicationController.startup() before startScene() and
    // startUI()
    // everything needs to be synchronous, no app events
    @Override
    public void startup() throws IOException {
        getApplication().getEventBus().register(this);

        log(TAG, "startup()");

        getApplication().getFileManager().setupApplicationDirectory();

        Project project = null;

        log(TAG, "Find last project path from preferences.");

        project = getApplication().getFileManager().createOrLoadStartupProject();

        // get onProjectCreate() and onProjectLoad() callbacks from
        // ApplicationModel
        getApplication().getProjectModelWrittable().setProject(project);
    }

    @Override
    public void startUI() {
        log(TAG, "startUI()");
        onStartUI();
    }

    @Override
    public void onProjectCreate(Project project) {
        log(TAG, "onProjectCreate()");
        project.onInitialize();
        project.onCreate();
    }

    @Override
    public void onProjectLoad(Project project) {
        log(TAG, "onProjectLoad()");
        project.onInitialize();
        project.onLoad();
    }

    @Override
    public void onProjectSave(Project project) {
        log(TAG, "onProjectSave()");
        project.onSave();
    }

    @Override
    public void onProjectClose(Project project) {
        log(TAG, "onProjectClose()");
        project.onClose();
        // When a project is closed, the application must restart the UI
        onRestartUI();
    }

    // --------------------------------------------------------------------------
    // Overridden Protected :: Methods
    // --------------------------------------------------------------------------

    protected void onStartUI() {
        log(TAG, "onStartUI()");
        ((ViewManager)getApplication().getViewManager()).onStartUI();
    }

    protected void onRestartUI() {
        log(TAG, "onRestartUI()");
        getApplication().startScene();
        ((ViewManager)getApplication().getViewManager()).onRestartUI();
    }

}
