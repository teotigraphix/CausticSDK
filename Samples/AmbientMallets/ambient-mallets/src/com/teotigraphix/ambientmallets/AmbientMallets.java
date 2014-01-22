////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.ambientmallets;

import com.teotigraphix.ambientmallets.model.ProjectModel;
import com.teotigraphix.ambientmallets.model.SoundModel;
import com.teotigraphix.ambientmallets.screen.MainScreen;
import com.teotigraphix.caustk.core.ISoundGenerator;
import com.teotigraphix.gdx.GdxApplication;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class AmbientMallets extends GdxApplication {

    public static final int MAIN_SCREEN = 0;

    public AmbientMallets(ISoundGenerator soundGenerator) {
        super("Ambient Mallets", soundGenerator);
        System.out.println("AmbientMallets: " + "construct()");
    }

    @Override
    protected void onRegisterModels() {
        System.out.println("AmbientMallets: " + "onRegisterModels()");
        getModelRegistry().put(SoundModel.class, new SoundModel());
        getModelRegistry().put(ProjectModel.class, new ProjectModel());
    }

    @Override
    protected void onRegisterScreens() {
        System.out.println("AmbientMallets: " + "onRegisterScreens()");
        getScreenManager().addScreen(MAIN_SCREEN, MainScreen.class);
    }

    @Override
    protected void onCreate() {
        System.out.println("AmbientMallets: " + "onCreate()");
        getScreenManager().setScreen(MAIN_SCREEN);
    }

}
