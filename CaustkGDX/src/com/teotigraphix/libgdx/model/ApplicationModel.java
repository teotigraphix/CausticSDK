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
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.libgdx.application.ApplicationRegistry;
import com.teotigraphix.libgdx.application.IApplicationRegistry;
import com.teotigraphix.libgdx.scene2d.IScreenProvider;

@Singleton
public class ApplicationModel extends CaustkModelBase implements IApplicationModel {

    private IApplicationRegistry applicationRegistry;

    private ApplicationModelState state;

    @SuppressWarnings("unchecked")
    public final <T extends ApplicationModelState> T getState() {
        return (T)state;
    }

    public final <T extends ApplicationModelState> void setState(T value) {
        state = value;
    }

    private Project project;

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public void setProject(Project value) {
        project = value;
        // creates new state or deserializes the saved state
        getController().trigger(new OnApplicationModelProjectChange(project));

        state.registerObservers();

        // all models need to reset here
        if (project.isFirstRun()) {
            // reload User into the LibraryModel
        }

        getController().trigger(new OnApplicationModelProjectLoadComplete(project));
    }

    @Inject
    IScreenProvider screenProvider;

    @Override
    public String getName() {
        return "TODO Resources";//resourceBundle.getString("APP_TITLE");
    }

    private boolean initialized = false;

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setInitialized(boolean value) {
        if (initialized)
            return;
        initialized = value;
        getController().trigger(new OnApplicationModelInitialize(this));
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
        getController().getLogger().model("ApplicationModel", "dirty: " + value);
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
        applicationRegistry = new ApplicationRegistry();
    }

    // called from ApplicationController right after Project is created/Loaded
    // and after the ApplicationMediator.onRegister() is called.
    @Override
    public void onRegister() {
        getController().getLogger().model("ApplicationModel", "onRegister()");

        // register all application level models, ApplicationModel
        // any models declared on the app's ApplicationMediator
        // all others are lazy loaded
        applicationRegistry.onRegisterModels();

        setInitialized(true);
    }

    @Override
    public void registerModel(ICaustkModel model) {
        applicationRegistry.registerModel(model);
    }

}
