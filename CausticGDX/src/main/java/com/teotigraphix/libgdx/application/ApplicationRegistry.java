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

import java.util.ArrayList;
import java.util.List;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.libgdx.controller.ICaustkMediator;
import com.teotigraphix.libgdx.model.ICaustkModel;

public class ApplicationRegistry implements IApplicationRegistry {

    private List<ICaustkModel> models = new ArrayList<ICaustkModel>();

    @Override
    public List<ICaustkModel> getModels() {
        return models;
    }

    private List<ICaustkMediator> mediators = new ArrayList<ICaustkMediator>();

    @Override
    public List<ICaustkMediator> getMediators() {
        return mediators;
    }

    @Override
    public void registerMeditor(ICaustkMediator mediator) {
        if (mediators.contains(mediator)) {
            CtkDebug.warn("ApplicationController already contains " + mediator);
            return;
        }
        mediators.add(mediator);
    }

    @Override
    public void registerMeditors() {
        CtkDebug.log("ApplicationController Register Mediators");
        for (ICaustkMediator mediator : mediators) {
            CtkDebug.log("   Register; " + mediator.getClass().getSimpleName());
            mediator.onRegister(null); // No screen means ApplicationMediator
        }
    }

    @Override
    public void registerModel(ICaustkModel model) {
        if (models.contains(model)) {
            CtkDebug.warn("ApplicationController already contains " + model);
            return;
        }
        models.add(model);
    }

    @Override
    public void registerModels() {
        CtkDebug.log("ApplicationController Register Models");
        for (ICaustkModel model : models) {
            CtkDebug.log("   Register; " + model.getClass().getSimpleName());
            model.onRegister();
        }
    }

    // No op

    @Override
    public void onRegister() {
    }

    @Override
    public <T> void register(Class<T> event, EventObserver<T> observer) {
    }

    @Override
    public void unregister(EventObserver<?> observer) {
    }

    @Override
    public void trigger(Object event) {
    }

    @Override
    public void clear() {
    }

}
