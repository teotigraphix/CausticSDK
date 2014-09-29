
package com.teotigraphix.caustk.controller.core;

import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.controller.daw.Model;
import com.teotigraphix.caustk.controller.helper.AbstractGrid;
import com.teotigraphix.caustk.controller.helper.Scales;
import com.teotigraphix.caustk.controller.helper.Scales.ScaleSelection;
import com.teotigraphix.caustk.controller.sequencer.SequencerView;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.core.midi.NoteReference;

public abstract class AbstractControlSurface {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private Model model;

    private AbstractGrid pads;

    private AbstractDisplay display;

    private AbstractDisplay subDisplay;

    private Array<Scales> scales = new Array<Scales>();

    private Array<SequencerView> sequencers = new Array<SequencerView>();

    protected ICaustkRack getRack() {
        return model.getRack();
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

    //----------------------------------
    // display
    //----------------------------------

    public AbstractDisplay getDisplay() {
        return display;
    }

    public void setDisplay(AbstractDisplay display) {
        this.display = display;
    }

    //----------------------------------
    // subDisplay
    //----------------------------------

    public AbstractDisplay getSubDisplay() {
        return subDisplay;
    }

    public void setSubDisplay(AbstractDisplay subDisplay) {
        this.subDisplay = subDisplay;
    }

    //----------------------------------
    // scales
    //----------------------------------

    public Scales getScales() {
        return scales.get(model.getMachineBank().getSelectedMachine().getIndex());
    }

    //----------------------------------
    // sequencer
    //----------------------------------

    public SequencerView getSequencerView() {
        return sequencers.get(model.getMachineBank().getSelectedMachine().getIndex());
    }

    public SequencerView getSequencerView(int index) {
        return sequencers.get(index);
    }

    //    /**
    //     * Returns the current sequencer based of the selected machine.
    //     */
    //    public SequencerView geSequencerView() {
    //        return sequencerView; // XXX Would be in views[] hardcoded for now
    //    }
    //
    //    void setSequencerView(SequencerView sequencerView) {
    //        this.sequencerView = sequencerView;
    //        sequencerView.attachTo(this);
    //    }

    public void onMachineAdd(int index, int midiRoot) {
        int gridSize = model.getGridSize();
        int columns = model.getNumColumns();
        int rows = model.getNumRows();

        Scales scale = new Scales(midiRoot, midiRoot + (columns * columns), columns, rows);
        scale.setSelectdScale(ScaleSelection.MinorPentatonic);
        scale.setRootKey(NoteReference.D);
        scales.add(scale);

        SequencerView sequencerView = new SequencerView(model, scale, columns, rows);
        sequencerView.attachTo(this);
        sequencerView.setGridLayout(gridSize, gridSize);
        sequencerView.onActivate();
        sequencers.add(sequencerView);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public AbstractControlSurface(Model model) {
        this.model = model;
    }

    public abstract void flush();

    public abstract void flush(boolean forceRefresh);

    public abstract void onArrowUp(boolean isDown);

    public abstract void onArrowRight(boolean isDown);

    public abstract void onArrowLeft(boolean isDown);

    public abstract void onArrowDown(boolean isDown);

}
