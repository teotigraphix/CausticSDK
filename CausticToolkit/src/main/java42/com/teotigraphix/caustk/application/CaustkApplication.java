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
    public ICaustkConfiguration getConfiguration() {
        return configuration;
    }

    //----------------------------------
    // controller
    //----------------------------------

    private ICaustkController controller;

    @Override
    public ICaustkController getController() {
        return controller;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     * 
     * @param configuration The main application's configuration instance.
     */
    public CaustkApplication(ICaustkConfiguration configuration) {
        this.configuration = configuration;

        // all Preset data gets loaded here
        controller = getConfiguration().createController(this);
    }

    //--------------------------------------------------------------------------
    // Public IGooveBoxApplication API
    //--------------------------------------------------------------------------

    @Override
    public void initialize() {
        // creates all sub components of the systemController
        controller.initialize();
        // now we create the parts and the rest of the application audio
        configure();
        getController().getDispatcher().trigger(new OnApplicationInitialize());
    }

    @Override
    public void start() {
        controller.start();
        getController().getDispatcher().trigger(new OnApplicationStart());
    }

    @Override
    public void save() throws IOException {
        controller.save();
        getController().getDispatcher().trigger(new OnApplicationSave());
    }

    @Override
    public void close() {
        controller.close();
        getController().getDispatcher().trigger(new OnApplicationClose());
    }

    /**
     * Called by {@link #initialize()} after all {@link ICaustkController}
     * components have been created.
     */
    protected void configure() {
    }

}
