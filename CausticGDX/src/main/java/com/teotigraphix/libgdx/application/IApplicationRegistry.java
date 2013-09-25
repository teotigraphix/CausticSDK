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

import java.util.List;

import com.teotigraphix.caustk.controller.IControllerComponent;
import com.teotigraphix.libgdx.controller.ICaustkMediator;
import com.teotigraphix.libgdx.model.ICaustkModel;

public interface IApplicationRegistry extends IControllerComponent {

    List<ICaustkModel> getModels();

    List<ICaustkMediator> getMediators();

    /**
     * Registers a model against the application controller.
     * <p>
     * Clients do not call this method, all registration happens automatically.
     * 
     * @param model The model to register.
     */
    void registerModel(ICaustkModel model);

    /**
     * Calls {@link ICaustkModel#onRegister()} on all registered models.
     * <p>
     * Main application calls this method in the startup sequence.
     */
    void registerModels();

    /**
     * Registers a mediator against the application controller.
     * <p>
     * Clients do not call this method, all registration happens automatically.
     * 
     * @param mediator The mediator to register.
     */
    void registerMeditor(ICaustkMediator mediator);

    /**
     * Calls {@link ICaustkMediator#onRegisterObservers()} on all registered
     * mediators.
     * <p>
     * Main application calls this method in the startup sequence.
     */
    //void registerMediatorObservers();

    /**
     * Calls {@link ICaustkMediator#onRegister()} on all registered mediators.
     * <p>
     * Main application calls this method in the startup sequence.
     */
    void registerMeditors();

}
