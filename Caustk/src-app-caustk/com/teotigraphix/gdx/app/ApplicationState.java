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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.controller.IViewManager;

@Singleton
public class ApplicationState extends ApplicationStateBase {

    private static final String TAG = "ApplicationStateImpl";

    @Inject
    private IApplicationModel applicationModel;

    @Inject
    private IViewManager viewManager;

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
    // project
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

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void onStartUI() {
        log(TAG, "onStartUI()");
        viewManager.onStartUI();
    }

    @Override
    protected void onRestartUI() {
        log(TAG, "onRestartUI()");
        getApplication().startScene();
        viewManager.onRestartUI();
    }

}
