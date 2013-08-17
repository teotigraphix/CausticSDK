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

import java.io.IOException;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CtkDebug;

/**
 * @author Michael Schmalle
 */
public class CaustkApplication implements ICaustkApplication {

    //--------------------------------------------------------------------------
    // Public ICaustkApplication API
    //--------------------------------------------------------------------------

    //----------------------------------
    // configuration
    //----------------------------------

    private ICaustkConfiguration configuration;

    @Override
    public final ICaustkConfiguration getConfiguration() {
        return configuration;
    }

    //----------------------------------
    // controller
    //----------------------------------

    private ICaustkController controller;

    @Override
    public final ICaustkController getController() {
        return controller;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     * 
     * @param configuration The main application's configuration instance.
     */
    public CaustkApplication(ICaustkConfiguration configuration) {
        this.configuration = configuration;

        controller = getConfiguration().createController(this);
    }

    //--------------------------------------------------------------------------
    // Public ICaustkApplication API
    //--------------------------------------------------------------------------

    @Override
    public void initialize() {
        CtkDebug.log("~Application.initialize()");
        // creates all sub components of the controller
        controller.initialize();
        getController().getDispatcher().trigger(new OnApplicationInitialize());
    }

    @Override
    public void start() {
        CtkDebug.log("~Application.start()");
        controller.start();
        getController().getDispatcher().trigger(new OnApplicationStart());
    }

    @Override
    public void save() throws IOException {
        CtkDebug.log("~Application.save()");
        controller.save();
        getController().getDispatcher().trigger(new OnApplicationSave());
    }

    @Override
    public void close() {
        CtkDebug.log("~Application.close()");
        controller.close();
        getController().getDispatcher().trigger(new OnApplicationClose());
    }

}
