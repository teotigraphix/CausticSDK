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
import com.teotigraphix.caustk.controller.ControllerComponentState;
import com.teotigraphix.caustk.tone.Tone;

/**
 * Serialized - v1.0
 * <ul>
 * <li>N/A</li>
 * </ul>
 */
public class SongSequencerModel extends ControllerComponentState {

    public SongSequencerModel() {
    }

    public SongSequencerModel(ICaustkController controller) {
        super(controller);
    }

    public void addPattern(Tone tone, int bank, int pattern, int start, int end) {

    }

    public void removePattern(Tone tone, int start, int end) {

    }

    public void clearAutomation() {

    }

    public void clearPatterns() {

    }

}
