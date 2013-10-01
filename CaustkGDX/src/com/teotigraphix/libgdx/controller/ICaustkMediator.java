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

/**
 * The mediator listens to model and application events, updates it's managed
 * user interface accordingly.
 * <p>
 * Mediator startup phase;
 * <ul>
 * <li>{@link #onRegister()}</li>
 * </ul>
 * The mediator's {@link #onRegister()} is called after all model's have been
 * registered.
 */
public interface ICaustkMediator {

    /**
     * Access to framework and application state will be non <code>null</code>.
     * <p>
     * The {@link #onRegister()} method is called after the
     * {@link ICaustkModel#onRegister()}.
     * <p>
     * Register all user interface component listeners.
     */
    void onRegister();

    void dispose();
}
