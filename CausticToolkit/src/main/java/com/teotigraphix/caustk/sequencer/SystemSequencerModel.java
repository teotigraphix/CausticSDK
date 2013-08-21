////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.sequencer;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerModel;
import com.teotigraphix.caustk.core.osc.OutputPanelMessage;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.OnSongSequencerTempoChange;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;

/**
 * Serialized - v1.0
 * <ul>
 * <li><code>isPlaying</code> - Sequencer playing.</li>
 * <li><code>sequencerMode</code> - {@link SequencerMode} pattern/song</li>
 * <li><code>tempo</code> - Sequencer bpm.</li>
 * </ul>
 */
public class SystemSequencerModel extends SubControllerModel {

    //--------------------------------------------------------------------------
    // Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // isPlaying
    //----------------------------------

    private boolean isPlaying = false;

    public void setIsPlaying(boolean value) {
        isPlaying = value;
        OutputPanelMessage.PLAY.send(getController(), isPlaying ? 1 : 0);
    }

    public final boolean isPlaying() {
        return isPlaying;
    }

    //----------------------------------
    // sequencerMode
    //----------------------------------

    private SequencerMode sequencerMode = SequencerMode.PATTERN;

    public final SequencerMode getSequencerMode() {
        return sequencerMode;
    }

    public final void setSequencerMode(SequencerMode value) {
        sequencerMode = value;
        OutputPanelMessage.MODE.send(getController(), sequencerMode.getValue());
    }

    //----------------------------------
    // tempo
    //----------------------------------

    private float tempo = 120;

    public void setTempo(float value) {
        tempo = value;
        OutputPanelMessage.BPM.send(getController(), tempo);
        getController().getDispatcher().trigger(new OnSongSequencerTempoChange(value));
    }

    public float getTempo() {
        return tempo;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public SystemSequencerModel() {
    }

    public SystemSequencerModel(ICaustkController controller) {
        super(controller);
    }

    @Override
    public void wakeup(ICaustkController controller) {
        super.wakeup(controller);

        setTempo(getTempo());
        setSequencerMode(getSequencerMode());
    }
}
