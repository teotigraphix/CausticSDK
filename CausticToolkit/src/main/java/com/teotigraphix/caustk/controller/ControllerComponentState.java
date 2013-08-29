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

package com.teotigraphix.caustk.controller;

import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.service.ISerializeService;

/**
 * The {@link ControllerComponentState} is the base class for all models of sub
 * controllers that wish to be serialized automatically during the framework
 * save phases.
 * <p>
 * These model's of the sub contoller's are serialized into the project state.
 * Each project will have it's own model instance serialized, so anything save
 * in these models should be on a per project base.
 * 
 * @see ControllerComponent
 */
public class ControllerComponentState implements ISerialize {

    transient ICaustkController controller;

    protected ICaustkController getController() {
        return controller;
    }

    /**
     * Called from {@link ISerializeService}, the controller gets set in
     * {@link #wakeup(ICaustkController)}.
     */
    public ControllerComponentState() {
    }

    /**
     * Called when explicitly creating and instance in {@link ControllerComponent}
     * .
     * 
     * @param controller
     */
    public ControllerComponentState(ICaustkController controller) {
        this.controller = controller;
    }

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
    }

}
