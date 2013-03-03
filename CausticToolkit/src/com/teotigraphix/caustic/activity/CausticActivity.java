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

package com.teotigraphix.caustic.activity;

import roboguice.activity.RoboActivity;
import android.os.Bundle;

import com.google.inject.Inject;
import com.singlecellsoftware.causticcore.CausticCore;
import com.teotigraphix.caustic.core.ICausticEngine;

/*

 Open Application

 - onCreate()
 - onStart()
 - onResume()

 Back button to Home

 - onPause()
 - onStop()
 - onDestroy()

 Go back into application

 - onCreate()
 - onStart()
 - onResume()

 Go to another Activity

 - onPause()
 - onStop()

 Back into running App

 - onRestart()
 - onStart() [Threads are still alive in the core]
 - onResume() [Threads are told to run again to process audio data]

 */

/**
 * The {@link CausticActivity} encapsulates the {@link CausticCore} startup and
 * allows the Hello* applications to be simpler and just contain what topics are
 * being demonstrated.
 * <ul>
 * <li>Creates {@link ICausticEngine}</li>
 * <li>Implements the lifecycle events</li>
 * </ul>
 */
public abstract class CausticActivity extends RoboActivity {

    //private static final String TAG = "CausticActivity";

    @Inject
    ICausticEngineCore core;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //createModule(savedInstanceState);
        //Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        core.onCreate(savedInstanceState);
    }

    /*
     * Below are the 4 required life cycle methods the engine needs to start and
     * stop the audio thread created by the Caustic instance.
     */

    @Override
    protected void onStart() {
        core.onStart();
        //Log.d(TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onStop() {
        core.onStop();
        //Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onPause() {
        core.onPause();
        //Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onResume() {
        core.onResume();
        //Log.d(TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        core.onRestart();
        //Log.d(TAG, "onRestart()");
        super.onRestart();
    }
}
