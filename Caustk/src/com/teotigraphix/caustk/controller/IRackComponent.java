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

import com.teotigraphix.caustk.controller.command.ICommand;
import com.teotigraphix.caustk.controller.command.ICommandManager;
import com.teotigraphix.caustk.core.IRestore;

/**
 * The {@link IRackComponent} API specifies a component that can be added to the
 * {@link IRack}.
 * 
 * @author Michael Schmalle
 */
public interface IRackComponent extends IRestore {

    /**
     * Returns the main {@link IRack}.
     */
    IRack getRack();

    /**
     * Registers observers against the {@link IDispatcher} API on dispatchers.
     * <p>
     * Rack components also register their {@link ICommand}s on the
     * {@link ICommandManager} during this phase.
     * <p>
     * Can also be a good time to add the component's API to the
     * {@link ICaustkController#addComponent(Class, Object)} map.
     */
    void registerObservers();

    /**
     * Removes the observers and perform any other clean up needed, similar to a
     * dispose() method.
     */
    void unregisterObservers();

}
