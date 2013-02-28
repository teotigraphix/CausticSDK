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

package com.teotigraphix.caustic.internal.controller;

import roboguice.inject.ContextSingleton;
import android.os.Bundle;

import com.google.inject.Inject;
import com.teotigraphix.caustic.controller.ICausticController;
import com.teotigraphix.caustic.core.ICausticEngine;
import com.teotigraphix.caustic.song.IWorkspace;

@ContextSingleton
public class CausticController implements ICausticController {

    @Inject
    IWorkspace workspace;

    ICausticEngine engine;

    public CausticController() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        engine = workspace.getRack().getEngine();
    }

    @Override
    public void onStart() {
        engine.onStart();
    }

    @Override
    public void onStop() {
        engine.onStop();
    }

    @Override
    public void onPause() {
        engine.onPause();
    }

    @Override
    public void onResume() {
        engine.onResume();
    }

    @Override
    public void onDestroy() {
        engine.onDestroy();
    }

    @Override
    public void onRestart() {
        engine.onRestart();
    }

}
