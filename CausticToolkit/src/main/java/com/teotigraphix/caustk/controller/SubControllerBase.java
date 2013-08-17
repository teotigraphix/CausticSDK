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

import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerChange;
import com.teotigraphix.caustk.project.IProjectManager.ProjectManagerChangeKind;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.project.ProjectManager;

/**
 * A {@link SubControllerBase} implementation is part of the
 * {@link ICaustkController} API and manages it's own model that will get
 * serialized during the {@link ProjectManager}'s project load/save phases.
 */
public abstract class SubControllerBase {

    //----------------------------------
    // controller
    //----------------------------------

    private ICaustkController controller;

    public final ICaustkController getController() {
        return controller;
    }

    //----------------------------------
    // model
    //----------------------------------

    private SubControllerModel model;

    protected SubControllerModel getInternalModel() {
        return model;
    }

    //----------------------------------
    // modelType
    //----------------------------------

    protected abstract Class<? extends SubControllerModel> getModelType();

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    protected void resetModel() {
        try {
            model = createModel(controller);
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

    public SubControllerBase(ICaustkController controller) {
        this.controller = controller;

        resetModel();

        controller.getDispatcher().register(OnProjectManagerChange.class,
                new EventObserver<OnProjectManagerChange>() {
                    @Override
                    public void trigger(OnProjectManagerChange object) {
                        if (object.getKind() == ProjectManagerChangeKind.SAVE) {
                            //System.out.println(getModelType().getSimpleName() + " >> SAVE");
                            saveState(object.getProject());
                        } else if (object.getKind() == ProjectManagerChangeKind.SAVE_COMPLETE) {
                            //System.out.println(getModelType().getSimpleName() + " >> SAVE_COMPLETE");
                        } else if (object.getKind() == ProjectManagerChangeKind.LOAD) {
                            //System.out.println(getModelType().getSimpleName() + " >> LOAD");
                            loadState(object.getProject());
                        } else if (object.getKind() == ProjectManagerChangeKind.CREATE) {
                            //System.out.println(getModelType().getSimpleName() + " >> CREATE");
                            createProject(object.getProject());
                        } else if (object.getKind() == ProjectManagerChangeKind.EXIT) {
                            //System.out.println(getModelType().getSimpleName() + " >> EXIT");
                            projectExit(object.getProject());
                        }
                    }
                });
    }

    //--------------------------------------------------------------------------
    // State
    //--------------------------------------------------------------------------

    private SubControllerModel createModel(ICaustkController controller)
            throws NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<? extends SubControllerModel> constructor = getModelType().getConstructor(
                ICaustkController.class);
        return constructor.newInstance(controller);
    }

    protected void createProject(Project project) {

    }

    protected void loadState(Project project) {
        String data = project.getString(getModelType().getName());
        // if the data does not exist, API update, just create the model
        if (data == null) {
            resetModel();
            return;
        }
        model = getController().getSerializeService().fromString(data, getModelType());
    }

    protected void saveState(Project project) {
        String data = getController().getSerializeService().toString(model);
        project.put(getModelType().getName(), data);
    }

    protected void projectExit(Project project) {

    }
}
