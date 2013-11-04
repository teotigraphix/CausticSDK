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
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.live.ICaustkFactory;
import com.teotigraphix.caustk.live.MachineType;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.tone.BasslineTone;
import com.teotigraphix.caustk.rack.tone.RackTone;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * @author Michael Schmalle
 */
public class SoundModel {

    private ICaustkFactory factory;

    public IRack getRack() {
        return factory.getRack();
    }

    @SuppressLint("UseSparseArrays")
    private Map<Integer, BasslineTone> tones = new HashMap<Integer, BasslineTone>();

    public Collection<BasslineTone> getTones() {
        return tones.values();
    }

    public BasslineTone getTone(int index) {
        return tones.get(index);
    }

    public SoundModel(ICaustkFactory factory) {
        this.factory = factory;
    }

    public void initialize() {

        try {
            BasslineTone part1 = (BasslineTone)factory.createRackTone("part1",
                    MachineType.Bassline, 0);
            BasslineTone part2 = (BasslineTone)factory.createRackTone("part2",
                    MachineType.Bassline, 1);

            tones.put(0, part1);
            tones.put(1, part2);

        } catch (CausticException e) {
            e.printStackTrace();
        }

        getTone(0).getMixer().setDelaySend(0.5f);
    }

    public void onAttach() {
    }

    public void loadPreset(int index, String name) throws IOException {
        RackTone tone = getTone(index);
        File presetFile = RuntimeUtils.getCausticPresetsFile(MachineType.Bassline, name);
        tone.getSynth().loadPreset(presetFile.getAbsolutePath());
    }

}
