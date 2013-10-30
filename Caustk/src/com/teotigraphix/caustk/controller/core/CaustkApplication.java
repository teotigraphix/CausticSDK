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

package com.teotigraphix.caustk.controller.core;

import java.io.IOException;

import com.teotigraphix.caustk.controller.ICausticLogger;
import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkConfiguration;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.ICaustkFactory;
import com.teotigraphix.caustk.rack.IRack;

/**
 * @author Michael Schmalle
 */
public final class CaustkApplication implements ICaustkApplication {

    //--------------------------------------------------------------------------
    // ICaustkApplication API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // logger
    //----------------------------------

    private ICausticLogger logger;

    @Override
    public ICausticLogger getLogger() {
        return logger;
    }

    //----------------------------------
    // configuration
    //----------------------------------

    private final ICaustkConfiguration configuration;

    @Override
    public final ICaustkConfiguration getConfiguration() {
        return configuration;
    }

    //----------------------------------
    // controller
    //----------------------------------

    private final CaustkController controller;

    @Override
    public final ICaustkController getController() {
        return controller;
    }

    //----------------------------------
    // factory
    //----------------------------------

    private final ICaustkFactory factory;

    @Override
    public final ICaustkFactory getFactory() {
        return factory;
    }

    //----------------------------------
    // rack
    //----------------------------------

    private IRack rack;

    @Override
    public final IRack getRack() {
        return rack;
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

        logger = getConfiguration().createLogger();
        factory = getConfiguration().createFactory(this);

        controller = (CaustkController)factory.createController();
        rack = factory.createRack();
    }

    //--------------------------------------------------------------------------
    // Public ICaustkApplication API
    //--------------------------------------------------------------------------

    // 1
    @Override
    public void initialize() {
        getLogger().log("Application", "1) ++++++++++++++++++++++++++++++++++++++++");
        getLogger().log("Application", "initialize()");
        getConfiguration().getSoundGenerator().initialize();
        controller.initialize();
    }

    // 2
    @Override
    public final void create() {
        getLogger().log("Application", "2) ++++++++++++++++++++++++++++++++++++++++");
        getLogger().log("Application", "create()");
        // creates all sub components of the controller
        controller.create();
        fireStateChange(StateChangeKind.Create);
    }

    // 3
    @Override
    public void run() {
        getLogger().log("Application", "3) ++++++++++++++++++++++++++++++++++++++++");
        getLogger().log("Application", "run()");
        controller.run();
        fireStateChange(StateChangeKind.Run);
    }

    @Override
    public final void save() throws IOException {
        getLogger().log("Application", "++++++++++++++++++++++++++++++++++++++++");
        getLogger().log("Application", "save()");
        controller.save();
        fireStateChange(StateChangeKind.Save);
    }

    @Override
    public final void close() {
        getLogger().log("Application", "++++++++++++++++++++++++++++++++++++++++");
        getLogger().log("Application", "close()");
        controller.close();
        fireStateChange(StateChangeKind.Close);
    }

    private void fireStateChange(StateChangeKind kind) {
        getController().trigger(new OnCausticApplicationStateChange(kind));
    }

}
