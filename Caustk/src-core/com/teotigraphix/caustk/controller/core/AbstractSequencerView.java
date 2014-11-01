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

import com.teotigraphix.caustk.controller.daw.CursorClipProxy;
import com.teotigraphix.caustk.controller.helper.Scales;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode.Resolution;
import com.teotigraphix.gdx.controller.IViewManager;
import com.teotigraphix.gdx.controller.ViewBase;

public class AbstractSequencerView extends ViewBase {

    private Resolution[] noteResolutions;

    private float[] resolutions;

    private int selectedIndex;

    private Scales scales;

    private CursorClipProxy clip;

    private int offsetX;

    private int offsetY;

    private int rows;

    private int cols;

    public final Resolution getNoteResolution() {
        return noteResolutions[selectedIndex];
    }

    public final float getResolution() {
        return resolutions[selectedIndex];
    }

    public CursorClipProxy getClip() {
        return clip;
    }

    public Scales getScales() {
        return scales;
    }

    /**
     * Adjust the step gate time
     */
    public void onStepGate(int index) {
        selectedIndex = 7 - index;
        clip.setStepGate(getResolution());
    }

    /**
     * Returns the offset into the pattern.
     * <p>
     * If the grid has 8 pads(columns), and each pad represents a 1/16th note,
     * the view would have 2 offsets(views) representing 1 measure in a pattern.
     * <p>
     * The first offset holds steps 0-7, the second offset holds steps 8-15.
     */
    public int getOffsetX() {
        return offsetX;
    }

    protected void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    /**
     * Returns the root midi note offset of the sequencer, IE 36 that is
     * displayed.
     */
    public int getOffsetY() {
        return offsetY;
    }

    protected void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public AbstractSequencerView(Scales scales, int rows, int cols) {
        super();
        this.rows = rows;
        this.cols = cols;
        //  1/4    1/4t     1/8      1/8t    1/16      1/16t    1/32      1/32t
        // [1.0, 0.6666667, 0.5, 0.33333334, 0.25, 0.16666667, 0.125, 0.083333336]
        this.noteResolutions = new Resolution[] {
                Resolution.Quater, null, Resolution.Eigth, null, Resolution.Sixteenth, null,
                Resolution.ThritySecond, null
        };

        this.resolutions = new float[] {
                1f, 2f / 3f, 1f / 2f, 1f / 3f, 1f / 4f, 1f / 6f, 1f / 8f, 1f / 12f
        };
        this.selectedIndex = 4;
        this.scales = scales;

        this.offsetX = 0;
        this.offsetY = 0;

    }

    @Override
    public void attachTo(IViewManager viewManager) {
        super.attachTo(viewManager);

        this.clip = new CursorClipProxy(cols, rows);
        this.clip.setViewManager(viewManager);
        this.clip.setStepGate(resolutions[selectedIndex]);
    }

    @Override
    public void onActivate() {
        super.onActivate();
    }

    protected boolean isInXRange(int x) {
        return x >= offsetX && x < offsetX + clip.getStepSize(); //1/4
    }

    public void drawGrid() {
        // TODO Auto-generated method stub

    }
}
