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

package com.teotigraphix.libgdx.application;

import com.teotigraphix.libgdx.controller.ICaustkMediator;

/**
 * The application hook for apps, implement in the custom application.
 * 
 * @see StartupExecutor
 */
public interface IApplicationMediator extends ICaustkMediator {

    /**
     * Called when the application state is created for the first time or
     * deserialized from a previous state.
     * <p>
     * This method is called before {@link #onRegister()}.
     */
    void create();

    /**
     * Called at the end of the load/start phase where all models and mediators
     * have been registered.
     */
    void run();

    void save();

}
