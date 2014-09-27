
package com.teotigraphix.caustk.controller.core;

import com.teotigraphix.caustk.controller.helper.AbstractGrid;
import com.teotigraphix.caustk.controller.sequencer.SequencerView;
import com.teotigraphix.caustk.core.ICaustkRack;

public abstract class AbstractControlSurface {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private AbstractGrid pads;

    private SequencerView sequencerView;

    private ICaustkRack rack;

    protected ICaustkRack getRack() {
        return rack;
    }

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

    public AbstractControlSurface(ICaustkRack rack) {
        this.rack = rack;
    }

    public abstract void flush();

    public abstract void onArrowUp(boolean isDown);

    public abstract void onArrowRight(boolean isDown);

    public abstract void onArrowLeft(boolean isDown);

    public abstract void onArrowDown(boolean isDown);
}
