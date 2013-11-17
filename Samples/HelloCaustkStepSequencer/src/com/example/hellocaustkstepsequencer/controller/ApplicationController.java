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

package com.example.hellocaustkstepsequencer.controller;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.hellocaustkstepsequencer.MainActivity;
import com.example.hellocaustkstepsequencer.model.SequencerModel;
import com.example.hellocaustkstepsequencer.model.SoundModel;
import com.example.hellocaustkstepsequencer.view.ApplicationMediator;
import com.example.hellocaustkstepsequencer.view.ControlsMediator;
import com.example.hellocaustkstepsequencer.view.SequencerMediator;
import com.example.hellocaustkstepsequencer.view.TransportControlMediator;
import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.workstation.ICaustkFactory;

/**
 * @author Michael Schmalle
 */
public class ApplicationController {

    ICaustkApplication application;

    private ICaustkFactory factory;

    private SoundModel soundModel;

    private SequencerModel sequencerModel;

    private ApplicationMediator applicationMediator;

    private TransportControlMediator transportControlMediator;

    private SequencerMediator sequencerMediator;

    private ControlsMediator controlsMediator;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ApplicationController(ICaustkApplication application) {
        this.application = application;
        this.factory = application.getFactory();
    }

    public void startup(MainActivity activity) {

        @SuppressWarnings("unused")
        SharedPreferences preferences = activity.getSharedPreferences("state",
                Activity.MODE_PRIVATE);
        //mCurViewMode = preferences.getInt("view_mode", DAY_VIEW_MODE);

        soundModel = new SoundModel(factory);
        soundModel.initialize();

        sequencerModel = new SequencerModel(soundModel);

        // create the ui view mediators
        applicationMediator = new ApplicationMediator(activity, soundModel);
        transportControlMediator = new TransportControlMediator(activity, sequencerModel);
        sequencerMediator = new SequencerMediator(activity, sequencerModel);
        controlsMediator = new ControlsMediator(activity, sequencerModel);

        applicationMediator.onAttach();
        transportControlMediator.onAttach();
        sequencerMediator.onAttach();
        controlsMediator.onAttach();

        soundModel.onAttach();
        sequencerModel.onAttach();
    }
}
