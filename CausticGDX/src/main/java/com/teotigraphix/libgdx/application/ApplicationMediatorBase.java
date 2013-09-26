
package com.teotigraphix.libgdx.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.google.inject.Inject;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.libgdx.controller.CaustkMediator;
import com.teotigraphix.libgdx.model.ApplicationModelState;
import com.teotigraphix.libgdx.model.IApplicationModel;

public class ApplicationMediatorBase extends CaustkMediator implements IApplicationMediator {

    @Inject
    protected IApplicationModel applicationModel;

    protected Class<? extends ApplicationModelState> stateType;

    // called from ApplicationController right after Project is created/Loaded
    // and before all Application models are registered
    @Override
    public void onRegister() {
        File file = getProjectBinaryFile();
        if (file.exists()) {
            CtkDebug.view("ApplicationMediator: Load last State - " + file);
            loadApplicationState(file, stateType);
        } else {
            CtkDebug.view("ApplicationMediator: Create new State - " + file);

            ApplicationModelState state = null;
            try {
                Constructor<? extends ApplicationModelState> constructor = stateType
                        .getConstructor(ICaustkController.class);
                state = constructor.newInstance(getController());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }

            saveApplicationState(file, state);
            applicationModel.setState(state);

            firstRun(state);
        }

        onLoad();
    }

    protected void firstRun(ApplicationModelState state) {
    }

    protected void onLoad() {
    }

    @Override
    public void save() {
        saveApplicationState(getProjectBinaryFile(), applicationModel.getState());
    }

    protected ApplicationModelState loadApplicationState(File file,
            Class<? extends ApplicationModelState> stateType) {
        ApplicationModelState state = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            state = stateType.cast(in.readObject());
            in.close();
            state.setController(getController());
            applicationModel.setState(state);

            String projectName = applicationModel.getProject().getName();
            File absoluteCausticFile = applicationModel.getProject().getAbsoluteResource(
                    projectName + "Audio.caustic");

            // only load the song into the core memory, we already have
            // the object graph mirrored in the Rack
            getController().getRack().getSoundSource().loadSongRaw(absoluteCausticFile);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CausticException e) {
            e.printStackTrace();
        }
        return state;
    }

    protected void saveApplicationState(File file, ApplicationModelState state) {
        try {
            state.save();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(state);
            out.close();

            String projectName = applicationModel.getProject().getName();
            File absoluteTargetSongFile = applicationModel.getProject().getAbsoluteResource(
                    projectName + "Audio.caustic");
            getController().getRack().saveSongAs(absoluteTargetSongFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected File getProjectBinaryFile() {
        Project project = applicationModel.getProject();
        File file = project.getAbsoluteResource(project.getName() + ".bin");
        return file;
    }
}
