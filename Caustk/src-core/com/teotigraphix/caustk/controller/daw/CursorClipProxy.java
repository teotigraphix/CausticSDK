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

package com.teotigraphix.caustk.controller.daw;

import com.teotigraphix.caustk.node.machine.sequencer.NoteNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode.Resolution;
import com.teotigraphix.gdx.controller.IViewManager;

public class CursorClipProxy {

    private int stepSize;

    private int rowSize;

    private int step = 4;

    private float stepGate;

    public CursorClipProxy(int stepSize, int rowSize) {
        this.stepSize = stepSize;
        this.rowSize = rowSize;
        this.step = -1;

        // Clip     createCursorClip (final int gridWidth, final int gridHeight)
        //this.clip = host.createCursorClip (this.stepSize, this.rowSize);
    }

    IViewManager viewManager;

    public void setViewManager(IViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public IViewManager getViewManager() {
        return viewManager;
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
        return viewManager.getSelectedMachinePattern().getNote(Resolution.toBeat(x, resolution),
                pitch);
    }

    public void toggleStep(int step, int note, int velocity, Resolution resolution) {
        //System.out.println("CursorClipProxy.toggleStep() step:" + step + ", gate:" + stepGate
        //        + ", note:" + note + ",  velo:" + (float)velocity / 127);
        viewManager.getSelectedMachinePattern().toggleStep(step, stepGate, note, velocity,
                resolution);
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
