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

package com.teotigraphix.libgdx.controller;

import com.teotigraphix.libgdx.model.ICaustkModel;
import com.teotigraphix.libgdx.screen.IScreen;

/**
 * The mediator listens to model and application events, updates it's managed
 * user interface accordingly.
 * <p>
 * Mediator startup phase;
 * <ul>
 * <li>{@link #onRegisterObservers()}</li>
 * <li>{@link #onRegister()}</li>
 * </ul>
 * The mediator's {@link #onRegister()} is called after all model's have been
 * registered.
 */
public interface ICaustkMediator {

    /**
     * Called before the application controller has it's start() invoked.
     * <p>
     * Register all application and model events.
     * <p>
     * All injections are complete and access to framework and application
     * models are non <code>null</code>.
     * <p>
     * The main application and project state has not been loaded yet.
     */
    //void onRegisterObservers();

    /**
     * Access to framework and application state will be non <code>null</code>.
     * <p>
     * The {@link #onRegister()} method is called after the
     * {@link ICaustkModel#onRegister()}.
     * <p>
     * Register all user interface component listeners.
     */
    void onRegister(IScreen screen);

    void dispose();

    /**
     * @param screen The parent screen.
     * @see IScreen#create()
     */
    void create(IScreen screen);

    /**
     * @param screen The parent screen.
     * @see IScreen#show()
     */
    void onShow(IScreen screen);

}
