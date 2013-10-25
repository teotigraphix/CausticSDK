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

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.teotigraphix.caustk.controller.ICaustkApplication.OnCausticApplicationStateChange;
import com.teotigraphix.libgdx.controller.CaustkMediator;
import com.teotigraphix.libgdx.model.ApplicationModelState;
import com.teotigraphix.libgdx.model.IApplicationModel;
import com.teotigraphix.libgdx.model.IApplicationModel.OnApplicationModelPhaseChange;

/**
 * A bas application mediator for application state, first run and load logic.
 * 
 * @author Michael Schmalle
 * @see #firstRun(ApplicationModelState)
 * @see #onLoad()
 */
public abstract class ApplicationMediatorBase extends CaustkMediator implements
        IApplicationMediator {

    @Inject
    protected IApplicationModel applicationModel;

    public ApplicationMediatorBase() {
    }

    @Override
    public void onRegister() {

        applicationModel.register(OnApplicationModelPhaseChange.class,
                new EventObserver<OnApplicationModelPhaseChange>() {
                    @Override
                    public void trigger(OnApplicationModelPhaseChange object) {
                        switch (object.getPhase()) {
                            case InitializeProject:
                                onInitializeProject();
                                break;

                            case ReloadProject:
                                onReloadProject();
                                break;
                        }
                    }
                });

        getController().register(OnCausticApplicationStateChange.class,
                new EventObserver<OnCausticApplicationStateChange>() {
                    @Override
                    public void trigger(OnCausticApplicationStateChange object) {
                        switch (object.getKind()) {
                            case Create:
                                break;
                            case Run:
                                break;
                            case Save:
                                applicationModel.save();
                                break;
                            case Close:
                                break;
                        }
                    }
                });
    }

    protected void onInitializeProject() {
    }

    protected void onReloadProject() {
    }

}
