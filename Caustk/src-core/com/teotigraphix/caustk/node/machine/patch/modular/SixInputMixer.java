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

package com.teotigraphix.caustk.node.machine.patch.modular;

import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * 6 gain input mixer.
 */
public class SixInputMixer extends MixerBase {

    public SixInputMixer() {
    }

    public SixInputMixer(MachineNode machineNode, int bay) {
        super(machineNode, bay);
        setLabel("SixInputMixer");
    }

    @Override
    protected int getNumBays() {
        return 2;
    }

    @Override
    protected void restoreComponents() {
        super.restoreComponents();
        setGain(MixerJack.In1Gain, getGain(MixerJack.In1Gain));
        setGain(MixerJack.In2Gain, getGain(MixerJack.In2Gain));
        setGain(MixerJack.In3Gain, getGain(MixerJack.In3Gain));
        setGain(MixerJack.In4Gain, getGain(MixerJack.In4Gain));
        setGain(MixerJack.In5Gain, getGain(MixerJack.In5Gain));
        setGain(MixerJack.In6Gain, getGain(MixerJack.In6Gain));
    }
}
