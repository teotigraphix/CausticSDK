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

package com.teotigraphix.caustk.gs.machine;

import com.teotigraphix.caustk.gs.machine.part.bassline.BasslineMachineSound;
import com.teotigraphix.caustk.gs.machine.part.sound.BasslinePatch;
import com.teotigraphix.caustk.gs.machine.part.sound.BasslinePatch.SynthProperty;

/*
 * Part1 - Bassline
 * Part2 - Bassline
 */
public class BasslineMachine extends GrooveMachine {

    public BasslineMachine() {
    }

    @Override
    protected void createComponentParts() {
        setSound(new BasslineMachineSound(this));
    }

    public float getSynthProperty(int channel, SynthProperty property) {
        BasslinePatch patch = (BasslinePatch)getSound().getParts().get(channel).getPatch();
        return patch.getSynthProperty(property);
    }

    public void setSynthProperty(int channel, SynthProperty property, float value) {
        BasslinePatch patch = (BasslinePatch)getSound().getParts().get(channel).getPatch();
        patch.setSynthProperty(property, value);
    }

}
