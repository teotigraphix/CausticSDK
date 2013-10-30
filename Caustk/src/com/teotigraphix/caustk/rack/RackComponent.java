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

package com.teotigraphix.caustk.rack;

import com.teotigraphix.caustk.controller.ICausticLogger;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;

/**
 * Default implementation of the {@link IRackComponent} API.
 * 
 * @author Michael Schmalle
 */
public class RackComponent implements IRackComponent {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    protected final ICausticLogger getLogger() {
        return getController().getLogger();
    }

    protected final IDispatcher getGlobalDispatcher() {
        return rack.getGlobalDispatcher();
    }

    protected final ICaustkController getController() {
        return rack.getController();
    }

    //--------------------------------------------------------------------------
    // IRackComponent API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // rack
    //----------------------------------

    private Rack rack;

    @Override
    public final IRack getRack() {
        return rack;
    }

    @Override
    public void setRack(IRack value) {
        rack = (Rack)value;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public RackComponent() {
    }

    //--------------------------------------------------------------------------
    // IRackComponent API :: Properties
    //--------------------------------------------------------------------------

    @Override
    public void beatChange(int measure, float beat) {
    }

    //--------------------------------------------------------------------------
    // IRestore API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void restore() {
    }
}
