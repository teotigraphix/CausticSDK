
package com.teotigraphix.caustk.controller.core;

import com.teotigraphix.caustk.controller.helper.AbstractGrid;

public abstract class AbstractControlSurface {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private AbstractGrid pads;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // pads
    //----------------------------------

    public AbstractGrid getPads() {
        return pads;
    }

    public void setPads(AbstractGrid pads) {
        this.pads = pads;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public AbstractControlSurface() {
    }

    public abstract void flush();

}
