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

package com.teotigraphix.caustic.controller;

import com.google.inject.ImplementedBy;
import com.teotigraphix.caustic.internal.controller.SequencerController;
import com.teotigraphix.caustic.output.IOutputPanel;
import com.teotigraphix.caustic.output.IOutputPanel.Mode;

@ImplementedBy(SequencerController.class)
public interface ISequencerController {

    void setMode(IOutputPanel.Mode mode);

    void play(boolean play, Mode mode);

    void play(boolean play);

    void seek(int beat);

    public static class OnSequencerPlayEvent {
    }

    public static class OnSequencerStopEvent {
    }

    public static class OnSequencerSeekEvent {
        private int beat;

        public int getBeat() {
            return beat;
        }

        public OnSequencerSeekEvent(int beat) {
            this.beat = beat;
        }
    }

    public static class OnSequencerBeatChangeEvent {
        private int beat;

        public int getBeat() {
            return beat;
        }

        public OnSequencerBeatChangeEvent(int beat) {
            this.beat = beat;
        }
    }

    public static class OnSequencerStepChangeEvent {
        private int step;

        public int getStep() {
            return step;
        }

        public OnSequencerStepChangeEvent(int step) {
            this.step = step;
        }
    }

    public static class OnSequencerBPMChangeEvent {
        private float bpm;

        public float getBPM() {
            return bpm;
        }

        public int getIntegerBPM() {
            return (int)bpm;
        }

        public OnSequencerBPMChangeEvent(float bpm) {
            this.bpm = bpm;
        }
    }

    boolean isPlaying();

    float getBPM();

    void setBPM(float value);

}
