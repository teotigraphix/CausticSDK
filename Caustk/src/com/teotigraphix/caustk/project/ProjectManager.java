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

package com.teotigraphix.caustk.project;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IControllerAware;

/**
 * The base implementation of the {@link IProjectManager} API.
 * 
 * @author Michael Schmalle
 */
public class ProjectManager implements IProjectManager, IControllerAware {

    private static final String FILE_SETTINGS = ".settings";

    private static final String DIR_PROJECTS = "projects";

    //-------------------------------------------------------------------------
    // Protected :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // controller
    //----------------------------------

    private ICaustkController controller;

    /**
     * Returns the application controller.
     */
    protected final ICaustkController getController() {
        return controller;
    }

    //--------------------------------------------------------------------------
    // IProjectManager API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // applicationRoot
    //----------------------------------

    @Override
    public final File getApplicationRoot() {
        return controller.getApplicationRoot();
    }

    //----------------------------------
    // projectDirectory
    //----------------------------------

    @Override
    public final File getProjectDirectory() {
        return new File(controller.getApplicationRoot(), DIR_PROJECTS);
    }

    @Override
    public final File getDirectory(String relativepath) {
        File directory = new File(getProjectDirectory(), relativepath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    /**
     * Returns the absolute File location of the <code>projects</code> directory
     * located within the {@link #getApplicationRoot()}.
     * 
     * @return The absolute {@link File}.
     */
    protected final File getAbsoluteProjectDirectory() {
        return getDirectory(DIR_PROJECTS);
    }

    //----------------------------------
    // sessionPreferences
    //----------------------------------

    private File sessionPreferencesFile;

    private SessionPreferences sessionPreferences;

    @Override
    public final SessionPreferences getSessionPreferences() {
        return sessionPreferences;
    }

    //----------------------------------
    // project
    //----------------------------------

    private Project project;

    @Override
    public final Project getProject() {
        return project;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ProjectManager() {
    }

    //-------------------------------------------------------------------------
    // IControllerAware API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void onAttach(ICaustkController controller) {
        this.controller = controller;

        getController().getLogger().log("ProjectManager", "Initialize, setup/load .settings");

        sessionPreferencesFile = new File(controller.getApplicationRoot(), FILE_SETTINGS);

        if (!sessionPreferencesFile.exists()) {
            try {
                FileUtils.writeStringToFile(sessionPreferencesFile, "");

                getController().getLogger().log("ProjectManager",
                        "Created new .settings file: " + sessionPreferencesFile.getAbsolutePath());

                sessionPreferences = new SessionPreferences();

                ProjectUtils.saveProjectPreferences(getController(), sessionPreferences,
                        sessionPreferencesFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (sessionPreferencesFile.exists()) {
                sessionPreferences = controller.getSerializeService().fromFile(
                        sessionPreferencesFile, SessionPreferences.class);
                getController().getLogger().log("ProjectManager",
                        "Loaded .settings file: " + sessionPreferencesFile.getAbsolutePath());
            }
        }
    }

    @Override
    public void onDetach() {
    }

    //-------------------------------------------------------------------------
    // IProjectManager API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public boolean isProject(File file) {
        return ProjectUtils.isProject(getAbsoluteProjectDirectory(), file);
    }

    @Override
    public Project createProject(String relativePath) throws IOException {
        return createProject(new File(relativePath));
    }

    @Override
    public Project createProject(File file) throws IOException {
        if (file.getName().contains("."))
            throw new IOException("Project is not a directory");

        project = ProjectUtils.createProject(controller, file);

        getController().getLogger().log("ProjectManager",
                "Created Project - " + project.getAbsolutDirectory());

        controller.trigger(new OnProjectManagerChange(project, ProjectManagerChangeKind.Create));

        // save the new Project
        finalizeSaveComplete();

        project.setInitializing(false);

        return project;
    }

    @Override
    public Project load(File directory) throws IOException {
        if (directory.getName().contains("."))
            throw new IOException("Project is not a directory");

        if (directory.isAbsolute())
            throw new IOException("Project direcotry must not be absolute");

        File absoluteDir = getDirectory(directory.getPath());

        getController().getLogger().log("ProjectManager", "Load - " + absoluteDir);

        project = controller.getSerializeService().fromFile(new File(absoluteDir, ".project"),
                Project.class);
        project.setController(controller);

        project.open();

        // all state objects are created here
        controller.trigger(new OnProjectManagerChange(project, ProjectManagerChangeKind.Load));

        // all clients can now act on the deserialized state objects (IControllerComponent)
        controller.trigger(new OnProjectManagerChange(project,
                ProjectManagerChangeKind.LoadComplete));

        return project;
    }

    @Override
    public void save() throws IOException {
        getController().getLogger().log("ProjectManager", "Save - " + project.getStateFile());
        getController().getLogger().log("ProjectManager", "Save - " + sessionPreferencesFile);

        // saves the relative path e.g. 'UntitledProject' in the 'projects/' directory
        sessionPreferences.put("lastProject", project.getDirectory().getPath());
        // set last modified date
        project.getInfo().setModified(new Date());
        // observers will save their data into the Project if implemented
        controller.trigger(new OnProjectManagerChange(project, ProjectManagerChangeKind.Save));

        controller.trigger(new OnProjectManagerChange(project,
                ProjectManagerChangeKind.SaveComplete));

        // save the .project and .settings files now that the state is stable
        finalizeSaveComplete();
    }

    @Override
    public void clear() {
        project.close();
        controller.trigger(new OnProjectManagerChange(project,
                ProjectManagerChangeKind.CloseComplete));
        project = null;
    }

    @Override
    public void exit() throws IOException {
        save();
        Project oldProject = project;
        project.close();
        project = null;
        controller.trigger(new OnProjectManagerChange(oldProject, ProjectManagerChangeKind.Exit));
    }

    //-------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    /**
     * After all observers have handled the
     * {@link ProjectManagerChangeKind#Save} event, this method will write the
     * <code>.project</code> and <code>.settings</code> files to disk.
     * 
     * @throws IOException
     */
    protected void finalizeSaveComplete() throws IOException {
        getController().getLogger().log("ProjectManager",
                "Save Complete, now saving project json file");

        String data = controller.getSerializeService().toPrettyString(project);
        FileUtils.writeStringToFile(project.getStateFile(), data);

        ProjectUtils.saveProjectPreferences(getController(), sessionPreferences,
                sessionPreferencesFile);
    }
}
