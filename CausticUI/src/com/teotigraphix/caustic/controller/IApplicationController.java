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

package com.teotigraphix.caustic.controller;

import com.google.inject.ImplementedBy;
import com.teotigraphix.caustic.internal.controller.application.ApplicationController;
import com.teotigraphix.caustic.router.IRouterClient;

@ImplementedBy(ApplicationController.class)
public interface IApplicationController extends IRouterClient {

    public static final String DEVICE_ID = "application";

    /**
     * Loads a project from the workspace.
     * <p>
     * <ul>
     * <li><strong>Message:</strong> /Controller/app/load_project
     * [load_project].</li>
     * <li><strong>Param0:</strong> absolute_path to save image.</li>
     * </ul>
     */
    public static final String COMMAND_LOAD_PROJECT = "load_project";

}
