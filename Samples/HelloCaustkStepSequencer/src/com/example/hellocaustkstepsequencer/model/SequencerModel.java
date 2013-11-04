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

package com.example.hellocaustkstepsequencer.model;

import com.teotigraphix.caustk.rack.ISystemSequencer.SequencerMode;
import com.teotigraphix.caustk.rack.tone.BasslineTone;
import com.teotigraphix.caustk.rack.tone.RackTone;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent.Resolution;

/**
 * @author Michael Schmalle
 */
public class SequencerModel {

    private SoundModel soundModel;

    public SequencerModel(SoundModel soundModel) {
        this.soundModel = soundModel;
    }

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // selected
    //----------------------------------

    public void setSelected(int index, boolean selected) {
        trigger(selected, 60, index, 0.15f, 1f);
        if (listener != null) {
            listener.onStepChanged(index);
        }
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void play() {
        soundModel.getRack().getSystemSequencer().play(SequencerMode.Pattern);
    }

    public void stop() {
        soundModel.getRack().getSystemSequencer().stop();
    }

    public void trigger(boolean selected, int pitch, int step, float gate, float velocity) {
        for (RackTone tone : soundModel.getTones()) {
            float start = Resolution.toBeat(step, Resolution.SIXTEENTH);
            if (selected) {
                float end = start + gate;
                int flags = 0;
                tone.getPatternSequencer().addNote(pitch, start, end, velocity, flags);
            } else {
                tone.getPatternSequencer().removeNote(pitch, start);
            }
        }
    }

    public void setCutoff(float value) {
        BasslineTone tone = soundModel.getTone(0);
        tone.getFilter().setCutoff(value);
    }

    //--------------------------------------------------------------------------
    // Public API :: Listeners
    //--------------------------------------------------------------------------

    private IStepListener listener;

    public void setListener(IStepListener l) {
        this.listener = l;
    }

    public interface IStepListener {
        void onStepChanged(int index);
    }

    public void onAttach() {
    }
}
