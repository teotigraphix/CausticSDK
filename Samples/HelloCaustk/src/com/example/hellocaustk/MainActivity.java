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

package com.example.hellocaustk;

import android.os.Bundle;

import com.teotigraphix.caustk.core.CaustkActivity;
import com.teotigraphix.caustk.core.osc.OutputPanelMessage;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.core.osc.RackMessage;

public class MainActivity extends CaustkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // creates the sound generator
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // /caustic/create [machine_type] [machine_name] [machine_index]? 
        RackMessage.CREATE.send(getGenerator(), "subsynth", "mysubsynth", 0);

        // /caustic/[machine_index]/pattern_sequencer/note_data [start] [pitch] [velocity] [end] [flags] 
        PatternSequencerMessage.NOTE_DATA.send(getGenerator(), 0, 0f, 60, 0.65f, 0.5f, 0);
        PatternSequencerMessage.NOTE_DATA.send(getGenerator(), 0, 1f, 60, 1f, 1.5f, 0);

        // /caustic/outputpanel/bpm [value] 
        OutputPanelMessage.BPM.send(getGenerator(), 75f);
        // /caustic/outputpanel/play [value] 
        OutputPanelMessage.PLAY.send(getGenerator(), 1);
    }

    @Override
    protected int getActivationKey() {
        // Expires 120-01-2013
        return 0x0C12EE33;
    }
}
