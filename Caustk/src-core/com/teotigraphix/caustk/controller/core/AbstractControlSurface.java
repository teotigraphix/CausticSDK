
package com.teotigraphix.caustk.controller.core;

import com.teotigraphix.caustk.controller.helper.AbstractGrid;
import com.teotigraphix.caustk.controller.sequencer.SequencerView;

public abstract class AbstractControlSurface {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private AbstractGrid pads;

    private SequencerView sequencerView;

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

    /**
     * Returns the current sequencer based of the selected machine.
     */
    public SequencerView geSequencerView() {
        return sequencerView; // XXX Would be in views[] hardcoded for now
    }

    public void setSequencerView(SequencerView sequencerView) {
        this.sequencerView = sequencerView;
        sequencerView.attachTo(this);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public AbstractControlSurface() {
    }

    public abstract void flush();

}
