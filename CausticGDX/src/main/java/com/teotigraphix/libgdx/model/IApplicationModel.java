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

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.project.Project;

public interface IApplicationModel extends ICaustkModel {

    boolean isInitialized();

    void setInitialized(boolean value);

    Project getProject();

    void setProject(Project value);

    /**
     * Returns the application's name.
     */
    String getName();

    void setScreen(int screenId);

    /**
     * Returns whether the application state is dirty.
     */
    boolean isDirty();

    /**
     * Calls {@link #setDirty(boolean)} with true.
     */
    void setDirty();

    /**
     * Sets the application state dirty.
     * 
     * @param value true sets dirty, false resets state after save.
     */
    void setDirty(boolean value);

    void registerModel(ICaustkModel model);

    //--------------------------------------------------------------------------
    // Events
    //--------------------------------------------------------------------------

    /**
     * Dispatched when the Application has become dirty or was cleaned by a
     * save.
     * 
     * @see ApplicationModel#getDispatcher()
     */
    public static class OnApplicationModelDirtyChanged {

        private boolean dirty;

        public final boolean isDirty() {
            return dirty;
        }

        public OnApplicationModelDirtyChanged(boolean dirty) {
            this.dirty = dirty;
        }
    }

    /**
     * Dispatched from {@link ICaustkController}
     */
    public static class OnApplicationModelInitialize {
        private IApplicationModel model;

        public final IApplicationModel getModel() {
            return model;
        }

        public OnApplicationModelInitialize(IApplicationModel model) {
            this.model = model;
        }
    }

}
