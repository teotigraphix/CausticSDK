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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.io.FileUtils;

import com.google.inject.Inject;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.libgdx.controller.CaustkMediator;
import com.teotigraphix.libgdx.model.ApplicationModelState;
import com.teotigraphix.libgdx.model.IApplicationModel;

/**
 * A bas application mediator for application state, first run and load logic.
 * 
 * @author Michael Schmalle
 * @see #firstRun(ApplicationModelState)
 * @see #onLoad()
 */
public abstract class ApplicationMediatorBase extends CaustkMediator implements
        IApplicationMediator {

    private static final String TAG = "ApplicationMediatorBase";

    @Inject
    protected IApplicationModel applicationModel;

    protected Class<? extends ApplicationModelState> stateType;

    private boolean isFirstRun;

    public final boolean isFirstRun() {
        return isFirstRun;
    }

    @Override
    public void create() {
        File file = getProjectBinaryFile();
        if (file.exists()) {
            getController().getLogger().view("ApplicationMediator", "Load last State - " + file);
            try {
                loadApplicationState(file, stateType);
            } catch (CausticException e) {
                e.printStackTrace();
            }
        } else {
            isFirstRun = true;

            getController().getLogger().view("ApplicationMediator", "Create new State - " + file);

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
    }

    @Override
    public void onRegister() {
        onLoad();
    }

    /**
     * Called during {@link #create()} when the state does not exist on disk.
     * 
     * @param state The current application state after creation or
     *            deserialization.
     */
    protected void firstRun(ApplicationModelState state) {
    }

    /**
     * Called form {@link #onRegister()} after the state has been create or
     * deserialized.
     */
    protected void onLoad() {
    }

    @Override
    public void run() {
        onRun();
        isFirstRun = false;
    }

    protected void onRun() {

    }

    @Override
    public void save() {
        saveApplicationState(getProjectBinaryFile(), applicationModel.getState());
    }

    protected ApplicationModelState loadApplicationState(File file,
            Class<? extends ApplicationModelState> stateType) throws CausticException {
        ApplicationModelState state = null;
        try {

            // read the full state back into memory
            readState(file);

            // save a temp .caustic file to load the binary data into the native core
            File absoluteCausticFile = getTempCausticFile();
            FileUtils.writeByteArrayToFile(absoluteCausticFile, applicationModel.getState()
                    .getSongFile().getData());

            // only load the song into the core memory, we already have
            // the object graph mirrored in the Rack
            getController().getRack().getSoundSource().loadSongRaw(absoluteCausticFile);

            // remove the temp file
            FileUtils.deleteQuietly(absoluteCausticFile);

        } catch (IOException e) {
            getController().getLogger().err(TAG, "IOException", e);
        } catch (CausticException e) {
            throw e;
        }
        return state;
    }

    protected void saveApplicationState(File file, ApplicationModelState state) {
        try {
            final File causticFile = getTempCausticFile();

            state.save();

            getController().getRack().getSoundSource().saveSongAs(causticFile);

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));

            byte[] byteArray = FileUtils.readFileToByteArray(causticFile);
            CausticSongFile songFile = new CausticSongFile(causticFile.getName(), byteArray);
            state.setSongFile(songFile);

            out.writeObject(state);

            out.close();

            FileUtils.deleteQuietly(causticFile);

        } catch (FileNotFoundException e) {
            getController().getLogger().err(TAG, "FileNotFoundException", e);
        } catch (IOException e) {
            getController().getLogger().err(TAG, "IOException", e);
        }
    }

    private void readState(File file) throws CausticException {
        ObjectInputStream in = null;
        ApplicationModelState state = null;
        try {
            in = new ObjectInputStream(new FileInputStream(file));
            state = stateType.cast(in.readObject());
            in.close();
        } catch (StreamCorruptedException e) {
            getController().getLogger().err(TAG, "StreamCorruptedException", e);
        } catch (FileNotFoundException e) {
            getController().getLogger().err(TAG, "FileNotFoundException", e);
        } catch (IOException e) {
            getController().getLogger().err(TAG, "IOException", e);
        } catch (ClassNotFoundException e) {
            getController().getLogger().err(TAG, "ClassNotFoundException", e);
        }

        if (state == null)
            throw new CausticException("Application state failed to load");

        // load the application model with the deserialized state
        state.setController(getController());
        applicationModel.setState(state);
    }

    protected File getTempCausticFile() {
        String projectName = applicationModel.getProject().getName();
        File file = applicationModel.getProject()
                .getAbsoluteResource(projectName + "Audio.caustic");
        return file;
    }

    protected File getProjectBinaryFile() {
        Project project = applicationModel.getProject();
        File file = project.getAbsoluteResource(project.getName() + ".bin");
        return file;
    }
}
