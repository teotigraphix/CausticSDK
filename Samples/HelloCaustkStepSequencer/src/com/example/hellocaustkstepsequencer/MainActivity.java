////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.example.hellocaustkstepsequencer;

import android.os.Bundle;

import com.example.hellocaustkstepsequencer.controller.ApplicationController;
import com.teotigraphix.caustk.controller.ICaustkConfiguration;
import com.teotigraphix.caustk.controller.core.CaustkConfigurationBase;
import com.teotigraphix.caustk.core.CaustkApplicationActivity;

/**
 * A simple Bassline step sequencer example.
 * <p>
 * Functionality;
 * <ul>
 * <li>Sets up a {@link IBassline} synth.</li>
 * <li>Simple stop and play transport controls.</li>
 * <li>Simple one pattern, one measure, 16 step sequencer.</li>
 * <li>Loads a preset from the Applications' private directory.</li>
 * <li>Adds delay to the mixer for the bassline.</li>
 * <li>Add a {@link IPhaserEffect} to the Bassline.</li>
 * <li>Add a {@link IParametricEQEffect} to the Bassline.</li>
 * <li>Power a beat LED.</li>
 * </ul>
 * 
 * @author Michael Schmalle
 */
public class MainActivity extends CaustkApplicationActivity {

    ApplicationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new ApplicationController(getRack());
        controller.startup(this);
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
            setApplicationId("stepsequencer");
            setApplicationTitle("StepSequencerExample");
        }
    }

}
