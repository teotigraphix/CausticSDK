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

package com.teotigraphix.libgdx.model;

import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.libgdx.controller.ICaustkMediator;

/**
 * A model that is {@link ICaustkController} aware and registered in the
 * application with the {@link Injector} binder as a {@link Singleton}.
 * <p>
 * Model startup phase;
 * <ul>
 * <li>{@link #onRegister()}</li>
 * <li>{@link #onShow()}</li>
 * </ul>
 */
public interface ICaustkModel extends IDispatcher {

    /**
     * The model's registration phase where state is created from deserialzed
     * project state.
     * <p>
     * Mediators have not had their {@link ICaustkMediator#onRegister()} called,
     * the {@link #onShow()} method is where model's update deserialized state.
     */
    void onRegister();

    /**
     * The last phase of startup, where mediators have register, create ui and
     * are ready to get the final update from model events.
     * <p>
     * All state the has been deserialized from the project session will get
     * "executed" in the model here, the mediators will catch the model events
     * and update the user interface according to the update logic.
     * <p>
     * Any logic that gets executed here should not depend on an state changes
     * from within this phase.
     */
    void onShow();
}
