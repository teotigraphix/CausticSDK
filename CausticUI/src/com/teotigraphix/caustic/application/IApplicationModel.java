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

package com.teotigraphix.caustic.application;

import roboguice.event.EventManager;
import android.app.Activity;

import com.google.inject.ImplementedBy;
import com.teotigraphix.caustic.activity.IApplicationPreferences;
import com.teotigraphix.caustic.internal.application.ApplicationModel;
import com.teotigraphix.caustic.song.IWorkspace;

@ImplementedBy(ApplicationModel.class)
public interface IApplicationModel {

    boolean isInitilized();

    void setIsInitialized();

    Activity getActivity();

    /**
     * Returns the single workspace within the application.
     */
    IWorkspace getWorkspace();

    /**
     * Returns the workspaces single event dispatching manager.
     */
    EventManager getEventManager();

    IApplicationPreferences getApplicationPreferences();
}
