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

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IRack;
import com.teotigraphix.caustk.controller.core.CaustkController;
import com.teotigraphix.caustk.controller.core.Rack;

/**
 * Base class for an application state that uses the IRack.
 * 
 * @author Michael Schmalle
 */
public abstract class RackApplicationState extends ApplicationModelState {

    private static final long serialVersionUID = -6095586546318899840L;

    private Rack rack;

    public IRack getRack() {
        return rack;
    }

    @Override
    public void setController(ICaustkController controller) {
        super.setController(controller);
        rack.setController(controller);
        ((CaustkController)controller).setRack(rack);
        rack.create();
    }

    public RackApplicationState(ICaustkController controller) {
        super();
        setId(getClass().getName());
        rack = new Rack(controller);
        setController(controller);
    }

}
