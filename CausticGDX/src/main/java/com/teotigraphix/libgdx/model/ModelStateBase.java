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

package com.teotigraphix.libgdx.model;

import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkController;

public class ModelStateBase implements ICaustkModelState {

    private transient Class<? extends IControllerModel> modelType;

    protected final Class<? extends IControllerModel> getModelType() {
        return modelType;
    }

    protected final void setModelType(Class<? extends IControllerModel> value) {
        modelType = value;
    }

    private transient ICaustkController controller;

    protected final ICaustkController getController() {
        return controller;
    }

    protected final IDispatcher getDispatcher() {
        return controller.getComponent(modelType).getDispatcher();
    }

    public ModelStateBase() {

    }

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
    }

}
