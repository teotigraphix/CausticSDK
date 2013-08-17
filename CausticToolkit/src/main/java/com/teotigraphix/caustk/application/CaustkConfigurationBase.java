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

package com.teotigraphix.caustk.application;

import java.io.File;

import com.teotigraphix.caustk.controller.CaustkController;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.sound.DesktopSoundGenerator;
import com.teotigraphix.caustk.sound.ISoundGenerator;

/**
 * Used in unit tests of the toolkit framework. Need the
 * {@link DesktopSoundGenerator} desktop access for tool kit tests.
 */
public abstract class CaustkConfigurationBase implements ICaustkConfiguration {

    private File applicationRoot;

    private File causticStorage;

    public CaustkConfigurationBase() {
        setCausticStorage(new File(System.getProperty("user.home")));
    }

    @Override
    public abstract String getApplicationId();

    @Override
    public File getApplicationRoot() {
        return applicationRoot;
    }

    @Override
    public void setApplicationRoot(File value) {
        applicationRoot = value;
    }

    @Override
    public File getCausticStorage() {
        return causticStorage;
    }

    @Override
    public void setCausticStorage(File value) {
        causticStorage = value;
    }

    @Override
    public ICaustkController createController(ICaustkApplication application) {
        return new CaustkController(application);
    }

    @Override
    public ISoundGenerator createSoundGenerator(ICaustkController controller) {
        return new DesktopSoundGenerator(controller);
    }

}
