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

package com.teotigraphix.libgdx.config;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.controller.ICaustkApplicationProvider;
import com.teotigraphix.caustk.service.IInjectorService;
import com.teotigraphix.libgdx.application.ApplicationRegistry;
import com.teotigraphix.libgdx.application.IApplicationRegistry;
import com.teotigraphix.libgdx.controller.ApplicationController;
import com.teotigraphix.libgdx.controller.ControllerProvider;
import com.teotigraphix.libgdx.controller.IApplicationController;
import com.teotigraphix.libgdx.controller.IControllerProvider;
import com.teotigraphix.libgdx.model.ApplicationModel;
import com.teotigraphix.libgdx.model.IApplicationModel;
import com.teotigraphix.libgdx.service.InjectorService;

public abstract class CausticRuntimeModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ICaustkApplicationProvider.class).to(ApplicationProvider.class).in(Singleton.class);
        bind(IApplicationModel.class).to(ApplicationModel.class).in(Singleton.class);
        bind(IApplicationController.class).to(ApplicationController.class).in(Singleton.class);
        bind(IControllerProvider.class).to(ControllerProvider.class).in(Singleton.class);
        bind(IInjectorService.class).to(InjectorService.class).in(Singleton.class);
        bind(IApplicationRegistry.class).to(ApplicationRegistry.class).in(Singleton.class);

        //bind(IScreenManager.class).to(ScreenManager.class).in(Singleton.class);

        configurePlatformRequirements();

        configureApplicationRequirements();
    }

    /**
     * The platform specific injections for either JavaFX or Android.
     */
    protected abstract void configurePlatformRequirements();

    /**
     * Application injections.
     * <p>
     * Required;
     * <ul>
     * <li>ICaustkConfiguration</li>
     * <li>ResourceBundle</li>
     * </ul>
     */
    protected abstract void configureApplicationRequirements();

}
