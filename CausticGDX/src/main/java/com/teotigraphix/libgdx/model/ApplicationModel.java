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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.libgdx.scene2d.IScreenProvider;

@Singleton
public class ApplicationModel extends CaustkModel implements IApplicationModel {

    @Inject
    IScreenProvider screenProvider;

    @Override
    public String getName() {
        return "TODO Resources";//resourceBundle.getString("APP_TITLE");
    }

    //----------------------------------
    // dirty
    //----------------------------------

    private boolean dirty;

    @Override
    public final boolean isDirty() {
        return dirty;
    }

    @Override
    public final void setDirty() {
        setDirty(true);
    }

    @Override
    public final void setDirty(boolean value) {
        if (value == dirty)
            return;
        CtkDebug.model("ApplicationModel dirty: " + value);
        dirty = value;
        trigger(new OnApplicationModelDirtyChanged(dirty));
    }

    @Override
    public void setScreen(int screenId) {
        screenProvider.getScreen().getGame().setScreen(screenId);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ApplicationModel() {
        super();
    }

    //--------------------------------------------------------------------------
    // Public API
    //--------------------------------------------------------------------------

    @Override
    public void start() {
        CtkDebug.model("ApplicationModel.start() fires OnApplicationModelStart");
    }

    //--------------------------------------------------------------------------
    // Project :: Events
    //--------------------------------------------------------------------------

    @Override
    public void run() {
        CtkDebug.model(">>>>> ApplicationModel.run() fires OnApplicationModelRun");
    }

    @Override
    public void onRegister() {
    }

    @Override
    public void onShow() {
    }
}
