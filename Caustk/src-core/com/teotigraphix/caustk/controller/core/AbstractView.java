////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.controller.core;

import com.teotigraphix.caustk.controller.daw.Model;

@SuppressWarnings("unused")
public class AbstractView {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private Model model;

    private AbstractControlSurface surface;

    private boolean canScrollLeft;

    private boolean canScrollRight;

    private boolean canScrollUp;

    private boolean canScrollDown;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // model
    //----------------------------------

    public Model getModel() {
        return model;
    }

    //----------------------------------
    // surface
    //----------------------------------

    protected AbstractControlSurface getSurface() {
        return surface;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public AbstractView(Model model) {
        this.model = model;
        this.surface = null;

        this.canScrollLeft = true;
        this.canScrollRight = true;
        this.canScrollUp = true;
        this.canScrollDown = true;
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    public void attachTo(AbstractControlSurface surface) {
        this.surface = surface;
    }

    public void onActivate() {
    }

    public void drawGrid() {
    }

    public void onGridNote(int note, int velocity) {
    }

    public void updateDevice() {
        //        Object m = this.surface.getActiveMode ();
        //        if (m != null)
        //        {
        //            m.updateDisplay ();
        //            m.updateFirstRow ();
        //            m.updateSecondRow ();
        //        }
        //        this.updateButtons ();
        //        this.updateArrows ();
    }
}
