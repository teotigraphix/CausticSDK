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

package com.teotigraphix.caustk.tone;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustk.controller.ICaustkController;

public class RhythmSet extends Tone {

    private List<RhythmTone> tones = new ArrayList<RhythmTone>();

    public List<RhythmTone> getTones() {
        return tones;
    }

    public RhythmSet(ICaustkController controller) {
        super(ToneType.Beatbox, controller);
        createTones();
    }

    private void createTones() {
        //        // Create the default 8 tones from the Beatbox channels
        //        for (int i = 0; i < 8; i++) {
        //            // a channel wraps a Beatbox channel, this could eventually
        //            // wrap a PCMSynth channel or anything else that is  "sampler" like
        //            RhythmChannel channel = new RhythmChannel(beatbox.getSampler().getChannel(i));
        //            RhythmTone tone = new RhythmTone(channel);
        //            tones.add(tone);
        //        }
    }
}
