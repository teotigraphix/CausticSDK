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


public interface IApplicationStateHandlers {

    /**
     * Loads the last project state.
     * <p>
     * After the {@link Project} is deserialized, the
     * {@link IApplicationModel#setProject(Project)} is called.
     * 
     * @throws IOException Cannot reload or create a project in the
     *             application's directory.
     */
    void startup() throws IOException;

    /**
     * Called when Scene has been created and user interface behaviors can
     * listen to model changes to get the current view state.
     */
    void startUI();

    void onProjectCreate(Project project);

    void onProjectLoad(Project project);

    void onProjectSave(Project project);

    void onProjectClose(Project project);
}
