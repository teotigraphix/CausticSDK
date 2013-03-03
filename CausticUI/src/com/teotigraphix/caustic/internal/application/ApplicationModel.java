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

package com.teotigraphix.caustic.internal.application;

import roboguice.event.EventManager;
import roboguice.inject.ContextSingleton;
import android.app.Activity;

import com.google.inject.Inject;
import com.teotigraphix.caustic.application.IApplicationModel;
import com.teotigraphix.caustic.controller.IApplicationPreferences;
import com.teotigraphix.caustic.song.IWorkspace;

@ContextSingleton
public class ApplicationModel implements IApplicationModel {

    private boolean mIsInitialized = false;

    private IWorkspace workspace;

    @Inject
    IApplicationPreferences applicationPreferences;

    @Override
    public boolean isInitilized() {
        return mIsInitialized;
    }

    @Override
    public void setIsInitialized() {
        mIsInitialized = true;
        //getEventManager().fire(new OnApplicationInitialized());
    }

    @Override
    public Activity getActivity() {
        return null;//workspace.getActivity();
    }

    @Override
    public IWorkspace getWorkspace() {
        return workspace;
    }

    @Override
    public EventManager getEventManager() {
        return null; //workspace.getEventManager();
    }

    @Override
    public IApplicationPreferences getApplicationPreferences() {
        return applicationPreferences;
    }

}
