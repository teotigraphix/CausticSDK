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

package com.teotigraphix.caustk.core;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;

import com.teotigraphix.caustk.controller.ICaustkConfiguration;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.core.CaustkApplication;
import com.teotigraphix.caustk.controller.core.Rack;

/**
 * The {@link CaustkApplicationActivity} adds the {@link ICaustkController}
 * support out of the box without needing to use Guice for injections as the
 * CaustkGDX framework needs.
 * <p>
 * From this activity, there is access to the {@link ICaustkController} and
 * {@link Rack} for all sound needs.
 */
public abstract class CaustkApplicationActivity extends CaustkActivity {

    private CaustkApplication application;

    @Override
    protected abstract int getActivationKey();

    /**
     * Reuturns the single {@link ICaustkController} instance for the audio
     * session.
     */
    protected ICaustkController getController() {
        return application.getController();
    }

    private Rack rack;

    /**
     * Returns the single instance of the {@link Rack}.
     */
    protected Rack getRack() {
        return rack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        File directory = Environment.getExternalStorageDirectory();

        final ICaustkConfiguration configuration = createConfiguration();
        configuration.setApplicationRoot(directory);
        configuration.setCausticStorage(directory);
        configuration.setSoundGenerator(getGenerator());

        // create the application
        application = new CaustkApplication(configuration);
        application.create();

        rack = new Rack(application.getController());
        rack.registerObservers();
    }

    /**
     * Subclasses need to create the {@link ICaustkConfiguration} implementation
     * for the application.
     */
    protected abstract ICaustkConfiguration createConfiguration();
}
