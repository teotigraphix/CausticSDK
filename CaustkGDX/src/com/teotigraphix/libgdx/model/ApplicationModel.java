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

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.controller.ICaustkApplicationProvider;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.libgdx.application.ApplicationRegistry;
import com.teotigraphix.libgdx.application.IApplicationRegistry;
import com.teotigraphix.libgdx.dialog.IDialogManager;
import com.teotigraphix.libgdx.scene2d.IScreenProvider;
import com.teotigraphix.libgdx.screen.IScreen;

@Singleton
public class ApplicationModel extends CaustkModelBase implements IApplicationModel {

    private static final String TAG = "ApplicationModel";

    @Inject
    IScreenProvider screenProvider;

    @Inject
    IDialogManager dialogManager;

    private Kryo kryo;

    private IApplicationRegistry applicationRegistry;

    protected boolean deleteCausticFile = true;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    @Override
    public IDialogManager getDialogManager() {
        return dialogManager;
    }

    @Override
    public IScreen getCurrentScreen() {
        return screenProvider.getScreen();
    }

    //----------------------------------
    // state
    //----------------------------------

    private ApplicationModelState state;

    @Override
    public final <T extends ApplicationModelState> T getState(Class<T> stateType) {
        return stateType.cast(state);
    }

    public final <T extends ApplicationModelState> void setState(T value) {
        state = value;
    }

    //----------------------------------
    // stateType
    //----------------------------------

    protected Class<? extends ApplicationModelState> stateType;

    @Override
    public void setStateType(Class<? extends ApplicationModelState> value) {
        stateType = value;
    }

    @Override
    public Class<? extends ApplicationModelState> getStateType() {
        return stateType;
    }

    //----------------------------------
    // project
    //----------------------------------

    @Override
    public final boolean isFirstRun() {
        return project.isFirstRun();
    }

    private Project project;

    private Project pendingProject;

    @Override
    public Project getProject() {
        return project;
    }

    void setProject(Project value) {
        project = value;
        if (isFirstRun()) {
            initializeProject(project);
            trigger(new OnApplicationModelPhaseChange(ApplicationModelPhase.InitializeProject));
        } else {
            reloadProject(project);
            trigger(new OnApplicationModelPhaseChange(ApplicationModelPhase.ReloadProject));
        }
    }

    @Override
    public String[] getProjectsAsArray() {
        List<File> projects = getController().getProjectManager().listProjects();
        String[] result = new String[projects.size()];
        for (int i = 0; i < projects.size(); i++) {
            result[i] = projects.get(i).getName();
        }
        return result;
    }

    //----------------------------------
    // initialized
    //----------------------------------

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
    public void pushScreen(int screenId) {
        screenProvider.getScreen().getGame().setScreen(screenId);
    }

    @Override
    public String getName() {
        return "TODO Resources";//resourceBundle.getString("APP_TITLE");
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    @Inject
    public ApplicationModel(ICaustkApplicationProvider provider) {
        super();
        applicationRegistry = new ApplicationRegistry();
        createKryo();
    }

    //--------------------------------------------------------------------------
    // Overridden :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void onRegister() {
        getController().getLogger().model("ApplicationModel", "onRegister()");

        // register all application level models, ApplicationModel
        // any models declared on the app's ApplicationMediator
        // all others are lazy loaded
        applicationRegistry.onRegisterModels();
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void registerModel(ICaustkModel model) {
        applicationRegistry.registerModel(model);
    }

    @Override
    public void create() throws CausticException {
        // create or load the last Project
        pendingProject = ApplicationModelUtils.createOrLoadLastProject(getController());
        if (pendingProject == null)
            throw new CausticException("Project creation/load failed");
    }

    @Override
    public Project createNewProject(String projectName) throws CausticException {
        if (state != null) {
            disposeState(state);
        }

        Project project = ApplicationModelUtils.createNewProject(getController(), projectName);
        setProject(project);

        trigger(new OnApplicationModelNewProjectComplete(project));

        return project;
    }

    @Override
    public Project loadProject(String projectName) throws IOException {
        if (state != null) {
            disposeState(state);
        }

        Project project = ApplicationModelUtils.loadProject(getController(), projectName);
        setProject(project);

        trigger(new OnApplicationModelNewProjectComplete(project));

        return project;
    }

    @Override
    public void run() {
        // loads or creates application state, if this was bound to
        // a project change event, we wouldn't need to call it here,
        // it would happen automatically based on the project's initialized (first run) prop
        setProject(pendingProject);

        setInitialized(true);
    }

    @Override
    public void save() {
        try {
            ApplicationModelUtils.saveApplicationState(this, kryo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDirty(false);
    }

    //--------------------------------------------------------------------------
    //
    //--------------------------------------------------------------------------

    private void createKryo() {
        kryo = ApplicationModelUtils.createKryo();
    }

    protected void initializeProject(Project project) {
        getController().getLogger().view("ApplicationMediator",
                "Create new State - " + ApplicationModelUtils.getProjectBinaryFile(project));

        ApplicationModelState state = null;

        try {
            state = stateType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        state.setController(getController());
        state.create();

        setState(state);

        try {
            ApplicationModelUtils.saveApplicationState(this, kryo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void reloadProject(Project project) {
        getController().getLogger().view("ApplicationMediator",
                "Load last State - " + ApplicationModelUtils.getProjectBinaryFile(project));

        try {
            ApplicationModelState state = ApplicationModelUtils.loadApplicationState(this, kryo);

            state.setController(getController());
            state.update();

            ApplicationModelUtils.loadSongBytesIntoRack(getController(), project, state
                    .getSongFile().getData());

            setState(state);

        } catch (CausticException e) {
            getController().getLogger().err(TAG, "CausticException", e);
        } catch (IOException e) {
            getController().getLogger().err(TAG, "IOException", e);
        }
    }

    private void disposeState(ApplicationModelState state) {
        state.setController(null);
        getController().setRack(null);
        // will call native clearrack
        state.dispose();
        this.state = null;
    }

}
