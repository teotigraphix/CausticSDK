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

package com.teotigraphix.caustk.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.application.Dispatcher;
import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerChange;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.project.ProjectManager;

/**
 * A {@link ControllerComponent} implementation is part of the
 * {@link ICaustkController} API and manages it's own model that will get
 * serialized during the {@link ProjectManager}'s project load/save phases.
 */
public abstract class ControllerComponent implements IControllerComponent {

    //----------------------------------
    // controller
    //----------------------------------

    private ICaustkController controller;

    public final ICaustkController getController() {
        return controller;
    }

    private IDispatcher dispatcher;

    @Override
    public final IDispatcher getDispatcher() {
        return dispatcher;
    }

    //----------------------------------
    // model
    //----------------------------------

    private ControllerComponentState state;

    protected ControllerComponentState getInternalState() {
        return state;
    }

    //----------------------------------
    // modelType
    //----------------------------------

    protected abstract Class<? extends ControllerComponentState> getStateType();

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ControllerComponent(ICaustkController controller) {
        this.controller = controller;
        dispatcher = new Dispatcher();
        resetState();
    }

    /**
     * Called from the client, signaling all controllers have been created.
     */
    @Override
    public void onRegister() {

        controller.getDispatcher().register(OnProjectManagerChange.class,
                new EventObserver<OnProjectManagerChange>() {
                    @Override
                    public void trigger(OnProjectManagerChange object) {

                        switch (object.getKind()) {
                            case SAVE:
                                saveState(object.getProject());
                                break;

                            case SAVE_COMPLETE:
                                break;

                            case LOAD:
                                loadState(object.getProject());
                                break;

                            case CREATE:
                                createProject(object.getProject());
                                break;

                            case EXIT:
                                projectExit(object.getProject());
                                break;

                            case CLOSE_COMPLETE:
                                closeProject(object.getProject());
                                state = null;
                                break;

                            case LOAD_COMPLETE:
                                loadComplete(object.getProject());
                                break;

                        }
                    }
                });
    }

    protected void loadComplete(Project project) {
        // TODO Auto-generated method stub

    }

    protected void closeProject(Project project) {

    }

    //--------------------------------------------------------------------------
    // State
    //--------------------------------------------------------------------------

    protected void resetState() {
        try {
            state = createState(controller);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private ControllerComponentState createState(ICaustkController controller)
            throws NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<? extends ControllerComponentState> constructor = getStateType()
                .getConstructor(ICaustkController.class);
        return constructor.newInstance(controller);
    }

    protected void createProject(Project project) {

    }

    protected void loadState(Project project) {
        String data = project.getString(getStateType().getName());
        // if the data does not exist, API update, just create the model
        if (data == null) {
            resetState();
            // simulate the model wakeup() so it happens everytime a model is loaded
            state.wakeup(getController());
        } else {
            state = getController().getSerializeService().fromString(data, getStateType());
        }
    }

    protected void saveState(Project project) {
        String data = getController().getSerializeService().toString(state);
        project.put(getStateType().getName(), data);
    }

    protected void projectExit(Project project) {

    }
}
