
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

    public AbstractControlSurface getSurface() {
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
