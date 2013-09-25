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

package com.teotigraphix.libgdx.controller;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.controller.ICaustkApplication.OnCausticApplicationStateChange;
import com.teotigraphix.caustk.controller.ICaustkApplicationProvider;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerChange;
import com.teotigraphix.libgdx.application.ApplicationRegistry;
import com.teotigraphix.libgdx.application.IApplicationRegistry;
import com.teotigraphix.libgdx.model.ApplicationModel;
import com.teotigraphix.libgdx.model.IApplicationModel;
import com.teotigraphix.libgdx.model.ICaustkModel;

/**
 * Mediates the {@link ApplicationModel}.
 */
@Singleton
public class ApplicationController implements IApplicationController {

    @Inject
    private IApplicationModel applicationModel;

    private IApplicationRegistry applicationRegistry;

    private ICaustkController controller;

    @Override
    public ICaustkController getController() {
        return controller;
    }

    @Inject
    public ApplicationController(ICaustkApplicationProvider provider) {
        applicationRegistry = new ApplicationRegistry();

        controller = provider.get().getController();
        controller.register(OnProjectManagerChange.class,
                new EventObserver<OnProjectManagerChange>() {
                    @Override
                    public void trigger(OnProjectManagerChange object) {
                        switch (object.getKind()) {
                            case Save:
                                applicationModel.setDirty(false);
                                break;

                            case CloseComplete:
                                break;
                            case Create:
                                break;
                            case Exit:
                                break;
                            case Load:
                                break;
                            case LoadComplete:
                                break;
                            case SaveComplete:
                                break;
                        }
                    }
                });

        controller.register(OnCausticApplicationStateChange.class,
                new EventObserver<OnCausticApplicationStateChange>() {
                    @Override
                    public void trigger(OnCausticApplicationStateChange object) {
                        switch (object.getKind()) {
                            case Create:
                                // register all application level models, ApplicationModel
                                // any models declared on the app's ApplicationMediator
                                // all others are lazy loaded
                                applicationRegistry.registerModels();

                                // register all application level mediators
                                applicationRegistry.registerMeditors();

                                for (ICaustkModel model : applicationRegistry.getModels()) {
                                    CtkDebug.log("    Show " + model.getClass().getSimpleName());
                                    model.onShow();
                                }

                                break;

                            case Save:
                                break;

                            case Close:
                                break;
                        }
                    }
                });
    }

}
