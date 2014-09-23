
package com.teotigraphix.caustk.controller.daw;

import com.teotigraphix.caustk.node.machine.sequencer.NoteNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode.Resolution;

public class CursorClipProxy {

    private int stepSize;

    private int rowSize;

    private int step = 4;

    private Model model;

    private float stepGate;

    public CursorClipProxy(Model model, int stepSize, int rowSize) {
        this.model = model;
        this.stepSize = stepSize;
        this.rowSize = rowSize;
        this.step = -1;

        // Clip     createCursorClip (final int gridWidth, final int gridHeight)
        //this.clip = host.createCursorClip (this.stepSize, this.rowSize);
    }

    /**
     * Sets the step gate time when new notes are addd to the pattern.
     * 
     * @param length
     */
    public void setStepGate(float stepGate) {
        this.stepGate = stepGate;
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getStepSize() {
        return stepSize;
    }

    public int getCurrentStep() {
        return step;
    }

    public void setCurrentStep(int step) {
        this.step = step;
        // System.out.println("step=" + step);
    }

    public NoteNode getStep(int x, int pitch, Resolution resolution) {
        return model.getMachineBank().getSelectedPattern()
                .getNote(Resolution.toBeat(x, resolution), pitch);
    }

    public void toggleStep(int step, int note, int velocity, Resolution resolution) {
        //System.out.println("CursorClipProxy.toggleStep() step:" + step + ", gate:" + stepGate
        //        + ", note:" + note + ",  velo:" + (float)velocity / 127);
        model.getMachineBank().getSelectedPattern()
                .toggleStep(step, stepGate, note, velocity, resolution);
    }

    public void scrollTo(int step, int row) {
        // XXX the grid pads can scroll, really nothing in the UI that can scroll yet
        //this.clip.scrollToKey (row);
        //this.clip.scrollToStep (step);
    }

    public void scrollStepsPageForward() {
        // TODO Auto-generated method stub

    }

    public void scrollStepsPageBackwards() {
        // TODO Auto-generated method stub

    }

}
