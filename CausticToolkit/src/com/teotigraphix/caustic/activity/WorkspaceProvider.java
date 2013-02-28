////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.activity;

import roboguice.RoboGuice;
import roboguice.inject.ContextSingleton;
import android.app.Activity;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.teotigraphix.caustic.internal.song.Workspace;
import com.teotigraphix.caustic.song.IWorkspace;

@ContextSingleton
public class WorkspaceProvider implements Provider<IWorkspace> {

    @Inject
    ICausticConfiguration configuration;

    @Inject
    Activity activity;

    private Workspace workspace;

    @Override
    public IWorkspace get() {
        if (workspace == null) {
            workspace = new Workspace(activity);
            workspace.configure(configuration);
            RoboGuice.injectMembers(activity, workspace);
        }
        return workspace;
    }

}
