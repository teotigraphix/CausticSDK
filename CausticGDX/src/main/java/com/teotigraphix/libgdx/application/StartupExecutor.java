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

package com.teotigraphix.libgdx.application;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkApplicationProvider;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.core.IApplicationHandler;
import com.teotigraphix.caustk.service.IInjectorService;
import com.teotigraphix.libgdx.controller.IApplicationController;
import com.teotigraphix.libgdx.model.IApplicationModel;

/**
 * The main instrumentation class for application startup of the game.
 */
public class StartupExecutor {

    final Set<Module> modules = new HashSet<Module>();

    @Inject
    ICaustkApplicationProvider application;

    @Inject
    IInjectorService injectorService;

    @Inject
    IApplicationController applicationController;

    @Inject
    IApplicationMediator applicationMediator;

    @Inject
    IApplicationModel applicationModel;

    private ICaustkApplication caustkApplication;

    private ICaustkController controller;

    private Injector injector;

    Injector getInjector() {
        return injector;
    }

    ICaustkController getController() {
        return controller;
    }

    public StartupExecutor() {
    }

    /*
     * - resolve caustic and application directory
     * - collection modules
     * - create application Injector using collected modules
     * - inject executor and game
     * - assign sound generator, caustic root, application root to configuration
     */
    public void create(IGame game) {
        File root = new File(Gdx.files.getExternalStoragePath());
        File causticDirectory = new File(root.getAbsolutePath());
        File applicationDirectory = new File(root, game.getAppName());

        @SuppressWarnings("unchecked")
        final Class<StartupExecutor> clazz = (Class<StartupExecutor>)getClass();
        final StartupExecutor instance = this;

        final Set<Module> additionalModules = new HashSet<Module>();
        modules.add(new AbstractModule() {
            @Override
            protected void configure() {
                bind(clazz).toInstance(instance);
            }
        });

        // Propagates initialization of additional modules to the specific
        // subclass of this Application instance.
        modules.addAll(additionalModules);

        // Creates an injector with all of the required modules.
        injector = Guice.createInjector(modules);

        // Injects all fields annotated with @Inject into this IGame instance.
        injector.injectMembers(instance);
        injector.injectMembers(game); // just need the injector

        caustkApplication = application.get();
        controller = caustkApplication.getController();
        controller.addComponent(IInjectorService.class, injectorService);

        caustkApplication.getConfiguration().setSoundGenerator(game.getSoundGenerator());
        caustkApplication.getConfiguration().setCausticStorage(causticDirectory);
        caustkApplication.getConfiguration().setApplicationRoot(applicationDirectory);

        caustkApplication.setApplicationHandler(new IApplicationHandler() {
            @Override
            public void commitCreate() {
            }

            @Override
            public void commitSave() {
            }

            @Override
            public void commitClose() {
            }
        });

        // create app directory, initialize controller and sub components
        // initialize projectmanager, create or load last project state
        caustkApplication.create();
    }

    public final void addModule(Module module) {
        modules.add(module);
    }
}
