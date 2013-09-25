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

import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.libgdx.model.ICaustkModel;

public class ApplicationRegistry implements IApplicationRegistry {

    private List<ICaustkModel> models = new ArrayList<ICaustkModel>();

    @Override
    public void registerModel(ICaustkModel model) {
        if (models.contains(model)) {
            CtkDebug.warn("ApplicationRegistry: already contains " + model);
            return;
        }
        models.add(model);
    }

    @Override
    public void onRegisterModels() {
        CtkDebug.log("ApplicationRegistry: Register Models");
        for (ICaustkModel model : models) {
            CtkDebug.log("   Register; " + model.getClass().getSimpleName());
            model.onRegister();
        }
    }
}
