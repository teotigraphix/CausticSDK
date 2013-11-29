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

package com.example.hellocaustkcontroller;

import android.os.Bundle;

import com.teotigraphix.caustk.controller.ICaustkConfiguration;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.core.CaustkConfigurationBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CaustkApplicationActivity;
import com.teotigraphix.caustk.rack.ISystemSequencer.SequencerMode;
import com.teotigraphix.caustk.rack.tone.SubSynthTone;
import com.teotigraphix.caustk.workstation.MachineType;

/**
 * An example showing how to use the {@link ICaustkController} without using
 * Guice injections.
 */
public class MainActivity extends CaustkApplicationActivity {

    private SubSynthTone subsynth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // we will now make the Caustic heart beat with the CaustkController API
        try {

            // creates a SubSynth at index 0 in the Caustic rack
            subsynth = (SubSynthTone)getFactory().createRackTone("mysubsynth",
                    MachineType.SubSynth, 0);

            // add the notes to the pattern sequencer
            subsynth.getPatternSequencer().addNote(60, 0f, 0.5f, 0.65f, 0);
            subsynth.getPatternSequencer().addNote(60, 1f, 1.5f, 1f, 0);
            //
            //            // set the tempo
            getRackSet().getSequencer().setBPM(70f);
            //
            //            // play the pattern sequencer
            getRackSet().getSequencer().play(SequencerMode.Pattern);

        } catch (CausticException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);
    }

    @Override
    protected String getApplicationName() {
        return "CaustkController";
    }

    @Override
    protected int getActivationKey() {
        // Expires 12-01-2013
        return 0x0C12EE33;
    }

    @Override
    protected ICaustkConfiguration createConfiguration() {
        return new ApplicationConfiguration();
    }

    public static class ApplicationConfiguration extends CaustkConfigurationBase {
        @Override
        protected void initialize() {
            setApplicationId("controller");
            setApplicationTitle("ControllerExample");
        }
    }

}
