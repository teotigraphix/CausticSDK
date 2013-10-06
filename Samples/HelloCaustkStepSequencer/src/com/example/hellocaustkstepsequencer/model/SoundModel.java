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

import java.io.File;

import com.teotigraphix.caustk.controller.IRack;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * @author Michael Schmalle
 */
public class SoundModel {

    private IRack rack;

    private BasslineTone part1;

    public BasslineTone getPart1() {
        return part1;
    }

    private BasslineTone part2;

    public BasslineTone getPart2() {
        return part2;
    }

    public SoundModel(IRack rack) {
        this.rack = rack;
    }

    public void initialize() {
        try {
            part1 = rack.getSoundSource().createTone("part1", BasslineTone.class);
            part2 = rack.getSoundSource().createTone("part2", BasslineTone.class);
        } catch (CausticException e) {
            e.printStackTrace();
        }

        rack.getSoundMixer().getChannel(0).setDelaySend(0.5f);
    }

    public void onAttach() {
    }

    public void loadPreset(int index, String name) {
        Tone tone = rack.getSoundSource().getTone(index);
        File presetFile = RuntimeUtils.getCausticPresetsFile("bassline", name);
        tone.getSynth().loadPreset(presetFile.getAbsolutePath());
    }
}
