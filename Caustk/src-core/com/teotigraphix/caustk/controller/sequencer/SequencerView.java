
package com.teotigraphix.caustk.controller.sequencer;

import com.badlogic.gdx.graphics.Color;
import com.teotigraphix.caustk.controller.core.AbstractSequencerView;
import com.teotigraphix.caustk.controller.daw.Colors;
import com.teotigraphix.caustk.controller.daw.Model;
import com.teotigraphix.caustk.controller.helper.ButtonEvent;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode.Resolution;

public class SequencerView extends AbstractSequencerView {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private int displayColumns;

    private int displayRows;

    private int numOctaves = 12;

    private int midiBase = 36;

    private int[] noteMap;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // pads
    //----------------------------------

    public void setGridLayout(int displayColumns, int displayRows) {
        this.displayColumns = displayColumns;
        this.displayRows = displayRows;
    }

    //----------------------------------
    // offsetX
    //----------------------------------

    @Override
    protected void setOffsetX(int offsetX) {
        PatternNode patternNode = getModel().getMachineBank().getSelectedPattern();
        int totalStepsInMeasure = Resolution.toSteps(getNoteResolution())
                * patternNode.getNumMeasures();

        // last to first
        if (offsetX == totalStepsInMeasure) {
            offsetX = 0; // round robin
        }

        // first to last
        if (offsetX < 0)
            offsetX = totalStepsInMeasure + offsetX; // round robin

        super.setOffsetX(offsetX);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * The sequencer automatically creates 128 rows for all possible MIDI notes.
     * 
     * @param model
     * @param displayColums Number of columns in the 'grid' eg 8 or 16.
     */
    public SequencerView(Model model, int displayColumns, int displayRows) {
        super(model, 128, displayColumns);
        // the displayColumns is the view, not the internal calc above of 128
        this.displayColumns = displayColumns;
        this.displayRows = displayRows;

        setOffsetY(midiBase);
        // XXX This would call out to the main app to set the scroll position of a 
        // piano roll or something
        getClip().scrollTo(0, midiBase);
    }

    //--------------------------------------------------------------------------
    // Overridden Public :: Methods
    //--------------------------------------------------------------------------

    /**
     * @see #updateScale()
     */
    @Override
    public void onActivate() {
        updateScale();
        super.onActivate();
    }

    @Override
    public void onGridNote(int note, int velocity) {
        if (velocity == 0)
            return;
        int numRows = noteMap.length;
        int index = note - 36;
        int x = index % numRows;
        int y = (int)Math.floor(index / numRows);

        // shift the step based on the offset
        x += getOffsetX(); // ADDED

        // toggleStep (int x, int y, int insertVelocity)
        getClip().toggleStep(x, noteMap[y], velocity, getNoteResolution());
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    /**
     * Scrolls the the offsetX, ignoring any bounds checking.
     * 
     * @param offsetX The offset in multiplies of the number of columns.
     */
    public void scrollToX(int offsetX) {
        super.setOffsetX(offsetX);
    }

    public void onOctaveUp(ButtonEvent event) {
        if (event.isDown()) {
            scrollUp(event);
        }
    }

    public void onOctaveDown(ButtonEvent event) {
        if (event.isDown()) {
            scrollDown(event);
        }
    }

    public void drawGridItem(int x, int y) {
        boolean isKeyboardEnabled = true; //t != null && t.canHoldNotes;

        int step = getClip().getCurrentStep();
        int hiStep = isInXRange(step) ? step % displayColumns : -1;

        int row = noteMap[y];

        boolean isSet = this.getClip().getStep(x + getOffsetX(), row, getNoteResolution()) != null;
        boolean hilite = x == hiStep;

        if (isKeyboardEnabled) {

            Color color = isSet ? (hilite ? Model.PUSH_COLOR2_GREEN_HI : Model.PUSH_COLOR2_BLUE)
                    : hilite ? Model.PUSH_COLOR2_GREEN_HI : getScales().getSequencerColor(
                            this.noteMap, y);

            if (hilite && isSet) {
                color = Colors.Orange.getColor();
            }

            getSurface().getPads().lightEx(x, y, color);

        } else {

            getSurface().getPads().lightEx(x, y, Model.PUSH_COLOR2_BLACK);
        }
    }

    @Override
    public void drawGrid() {
        //System.err.println("drawGrid()");
        for (int x = 0; x < displayColumns; x++) {
            for (int y = 0; y < displayRows; y++) {
                drawGridItem(x, y);
            }
        }
    }

    public void scrollRight(ButtonEvent event) {
        this.setOffsetX(getOffsetX() + getClip().getStepSize());
        this.getClip().scrollStepsPageForward();
    }

    public void scrollLeft(ButtonEvent event) {
        int newOffset = getOffsetX() - getClip().getStepSize();
        if (newOffset < 0)
            setOffsetX(newOffset); // round robin
        else {
            setOffsetX(newOffset);
            getClip().scrollStepsPageBackwards();
        }
    }

    public void scrollDown(ButtonEvent event) {
        setOffsetY(Math.max(0, this.getOffsetY() - numOctaves));
        updateScale();
        String text = getScales().getSequencerRangeText(noteMap[0], noteMap[displayRows - 1]);
        System.out.println("scrollDown()" + text);

        // this.surface.getDisplay ().showNotification (
        //    "          " + this.scales.getSequencerRangeText (this.noteMap[0], this.noteMap[7]));
    }

    public void scrollUp(ButtonEvent event) {
        setOffsetY(Math.min(this.getClip().getRowSize() - numOctaves, this.getOffsetY()
                + numOctaves));
        updateScale();
        String text = this.getScales().getSequencerRangeText(noteMap[0],
                this.noteMap[displayRows - 1]);
        System.out.println("scrollUp()" + text);
        //this.surface.getDisplay ().showNotification (
        //      "          " + this.getScales().getSequencerRangeText (this.noteMap[0], this.noteMap[7]));
    }

    protected void updateScale() {
        //var t = this.model.getCurrentTrackBank().getSelectedTrack();
        boolean can = true;
        // the vertical rows as notes in the current scale
        noteMap = can ? getScales().getSequencerMatrix(displayRows, getOffsetY()) : getScales()
                .getEmptyMatrix();
    }
}
