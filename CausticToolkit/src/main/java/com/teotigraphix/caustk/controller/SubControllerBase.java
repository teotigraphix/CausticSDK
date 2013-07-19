
package com.teotigraphix.caustk.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerChange;
import com.teotigraphix.caustk.project.IProjectManager.ProjectManagerChangeKind;
import com.teotigraphix.caustk.project.Project;

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
                            saveState(object.getProject());
                        } else if (object.getKind() == ProjectManagerChangeKind.LOAD) {
                            loadState(object.getProject());
                        } else if (object.getKind() == ProjectManagerChangeKind.CREATE) {
                            createProject(object.getProject());
                        } else if (object.getKind() == ProjectManagerChangeKind.EXIT) {
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
        model = getController().getSerializeService().fromString(data, getModelType());
    }

    protected void saveState(Project project) {
        String data = getController().getSerializeService().toString(model);
        project.put(getModelType().getName(), data);
    }

    protected void projectExit(Project project) {

    }
}
