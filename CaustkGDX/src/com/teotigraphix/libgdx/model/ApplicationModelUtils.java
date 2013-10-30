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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.libgdx.application.CausticSongFile;

/**
 * @author Michael Schmalle
 */
public final class ApplicationModelUtils {

    private static boolean deleteCausticFile = true;

    static Project createOrLoadLastProject(ICaustkController controller) {
        String path = controller.getProjectManager().getLastProject();

        Project project = null;

        try {
            if (path == null) {
                project = createNewProject(controller, "UntitledProject");
            } else {
                project = loadProject(controller, path);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return project;
    }

    static Project createNewProject(ICaustkController controller, String projectName) {
        Project project = null;
        try {
            project = controller.getProjectManager().createProject(new File(projectName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return project;
    }

    static Project loadProject(ICaustkController controller, String projectPath) throws IOException {
        Project project = controller.getProjectManager().load(new File(projectPath));
        return project;
    }

    //--------------------------------------------------------------------------
    // Loading song data
    //--------------------------------------------------------------------------

    static void loadSongBytesIntoRack(ICaustkController controller, Project project, byte[] data)
            throws IOException, CausticException {
        if (project == null)
            throw new IllegalStateException("Project must not be null");

        // save a temp .caustic file to load the binary data into the native core
        File absoluteCausticFile = getTempCausticFile(project);
        FileUtils.writeByteArrayToFile(absoluteCausticFile, data);

        // only load the song into the core memory, we already have
        // the object graph mirrored in the Rack
        controller.getRack().loadSongRaw(absoluteCausticFile);

        // remove the temp file
        FileUtils.deleteQuietly(absoluteCausticFile);
    }

    //--------------------------------------------------------------------------
    // State serialization
    //--------------------------------------------------------------------------

    static ApplicationModelState loadApplicationState(IApplicationModel applicationModel, Kryo kryo)
            throws CausticException, FileNotFoundException {
        File file = ApplicationModelUtils.getProjectBinaryFile(applicationModel.getProject());

        ApplicationModelState state = null;
        try {
            // read the full state back into memory
            state = readState(file, applicationModel.getStateType(), kryo);
        } catch (CausticException e) {
            throw e;
        } catch (FileNotFoundException e) {
            throw e;
        }
        return state;
    }

    static ApplicationModelState readState(File file,
            Class<? extends ApplicationModelState> stateType, Kryo kryo) throws CausticException,
            FileNotFoundException {
        ApplicationModelState state = null;
        try {
            Input input = new Input(new FileInputStream(file));
            state = kryo.readObject(input, stateType);
            input.close();
        } catch (FileNotFoundException e) {
            throw e;
        }

        if (state == null)
            throw new CausticException("Application state failed to load");

        return state;
    }

    static void saveApplicationState(IApplicationModel applicationModel, Kryo kryo)
            throws IOException {
        final Project project = applicationModel.getProject();
        final File file = ApplicationModelUtils.getProjectBinaryFile(project);
        final File causticFile = ApplicationModelUtils.getTempCausticFile(project);

        ApplicationModelState state = applicationModel.getState(ApplicationModelState.class);

        state.save();
        applicationModel.getController().getRack().saveSongAs(causticFile);

        Output output = new Output(new FileOutputStream(file));

        byte[] byteArray = FileUtils.readFileToByteArray(causticFile);
        CausticSongFile songFile = new CausticSongFile(causticFile.getName(), byteArray);
        state.setSongFile(songFile);

        kryo.writeObject(output, state);

        output.close();

        if (deleteCausticFile)
            FileUtils.deleteQuietly(causticFile);

    }

    //--------------------------------------------------------------------------
    // File Util
    //--------------------------------------------------------------------------

    static File getTempCausticFile(Project project) {
        String projectName = project.getName();
        File file = project.getAbsoluteResource(projectName + "Audio.caustic");
        return file;
    }

    static File getProjectBinaryFile(Project project) {
        File file = project.getAbsoluteResource(project.getName() + ".bin");
        return file;
    }

}
