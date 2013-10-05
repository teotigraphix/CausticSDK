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

import java.io.File;
import java.io.IOException;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.controller.ICaustkApplication.OnCausticApplicationStateChange;
import com.teotigraphix.caustk.controller.ICaustkApplicationProvider;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerChange;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.libgdx.application.IApplicationMediator;
import com.teotigraphix.libgdx.model.ApplicationModel;
import com.teotigraphix.libgdx.model.IApplicationModel;

/**
 * Mediates the {@link ApplicationModel}.
 */
@Singleton
public class ApplicationController implements IApplicationController {

    @Inject
    private IApplicationModel applicationModel;

    @Inject
    private IApplicationMediator applicationMediator;

    private ICaustkController controller;

    @Override
    public ICaustkController getController() {
        return controller;
    }

    @Inject
    public ApplicationController(ICaustkApplicationProvider provider) {

        controller = provider.get().getController();

        // this is the only place ProjectManager events are listened to
        controller.register(OnProjectManagerChange.class,
                new EventObserver<OnProjectManagerChange>() {
                    @Override
                    public void trigger(OnProjectManagerChange object) {
                        switch (object.getKind()) {
                            case Save:
                                applicationModel.setDirty(false);
                                applicationMediator.save();
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
                                break;

                            case Save:
                                break;

                            case Close:
                                break;
                        }
                    }
                });
    }

    @Override
    public void create() {

        // - ICaustkController.create()
        // - IApplicationHandler.create()
        // - dispatch(Create)
        // - we are here now

        String path = getController().getProjectManager().getSessionPreferences()
                .getString("lastProject");

        Project project = null;

        try {
            if (path == null) {
                project = getController().getProjectManager().createProject(
                        new File("UntitledProject"));
            } else {
                project = getController().getProjectManager().load(new File(path));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        applicationModel.setProject(project);

        applicationMediator.create();

        applicationMediator.onRegister();

        // last call in the startup chain
        // Models/Mediators will hear no events until this call
        applicationModel.onRegister();
    }

}
