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

import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkConfiguration;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.core.CaustkApplication;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.Rack;
import com.teotigraphix.caustk.workstation.ICaustkFactory;

/**
 * The {@link CaustkApplicationActivity} adds the {@link ICaustkController}
 * support out of the box without needing to use Guice for injections as the
 * CaustkGDX framework needs.
 * <p>
 * From this activity, there is access to the {@link ICaustkController} and
 * {@link Rack} for all sound needs.
 */
public abstract class CaustkApplicationActivity extends CaustkActivity {

    private ICaustkApplication application;

    protected ICaustkApplication getCaustkApplication() {
        return application;
    }

    @Override
    protected abstract int getActivationKey();

    /**
     * Returns the factory used to create all Caustk components.
     */
    protected ICaustkFactory getFactory() {
        return application.getFactory();
    }

    /**
     * Returns the single {@link ICaustkController} instance for the audio
     * session.
     */
    protected ICaustkController getController() {
        return application.getController();
    }

    /**
     * Returns the single instance of the {@link Rack}.
     */
    protected IRack getRack() {
        return application.getRack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        File causticStorageRoot = Environment.getExternalStorageDirectory();

        try {
            // create the application and run it
            application = CaustkApplication.startAndRun(getGenerator(), causticStorageRoot,
                    new File(causticStorageRoot, "TestApp"));
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    /**
     * Subclasses need to create the {@link ICaustkConfiguration} implementation
     * for the application.
     */
    protected abstract ICaustkConfiguration createConfiguration();
}
