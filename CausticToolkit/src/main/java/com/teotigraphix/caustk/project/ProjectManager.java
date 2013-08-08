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
import com.teotigraphix.caustk.core.CtkDebug;

/**
 * The project manager manages the single project loaded for an application.
 * <p>
 * The manager will have a root directory passed to it when it is created. All
 * project related files are stored within this directory.
 */
public class ProjectManager implements IProjectManager {

    private ICaustkController controller;

    private File projectDirectory;

    private File sessionPreferencesFile;

    //----------------------------------
    // sessionPreferences
    //----------------------------------

    private SessionPreferences sessionPreferences;

    @Override
    public SessionPreferences getSessionPreferences() {
        return sessionPreferences;
    }

    //----------------------------------
    // applicationRoot
    //----------------------------------

    /**
     * The root application directory, all {@link Project}s are stored in the
     * <code>applicationRoot/projects</code> directory.
     */
    //private File applicationRoot;

    @Override
    public File getApplicationRoot() {
        return controller.getConfiguration().getApplicationRoot();
    }

    @Override
    public File getDirectory(String path) {
        File directory = new File(getApplicationRoot(), path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    //----------------------------------
    // project
    //----------------------------------

    private Project project;

    @Override
    public Project getProject() {
        return project;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ProjectManager(ICaustkController controller) {
        this.controller = controller;
    }

    //-------------------------------------------------------------------------
    // IProjectManager API
    //--------------------------------------------------------------------------

    @Override
    public void initialize() {
        CtkDebug.log("IProjectManager.initialize()");

        File applicationRoot = controller.getConfiguration().getApplicationRoot();
        projectDirectory = new File(applicationRoot, "projects");
        sessionPreferencesFile = new File(applicationRoot, ".settings");

        if (!sessionPreferencesFile.exists()) {
            try {
                FileUtils.writeStringToFile(sessionPreferencesFile, "");
                sessionPreferences = new SessionPreferences();
                CtkDebug.log("Created new .settings file: "
                        + sessionPreferencesFile.getAbsolutePath());
                saveProjectPreferences();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (sessionPreferencesFile.exists()) {
                sessionPreferences = controller.getSerializeService().fromFile(
                        sessionPreferencesFile, SessionPreferences.class);
                CtkDebug.log("Loaded .settings file: " + sessionPreferencesFile.getAbsolutePath());
            }
        }
    }

    @Override
    public boolean isProject(File file) {
        if (file.isAbsolute())
            return file.exists();
        return toProjectFile(file).exists();
    }

    @Override
    public void exit() throws IOException {
        save();
        Project oldProject = project;
        project.close();
        project = null;
        controller.getDispatcher().trigger(
                new OnProjectManagerChange(oldProject, ProjectManagerChangeKind.EXIT));
    }

    @Override
    public void save() throws IOException {
        // XXX project manager project.getFile absolute path doubled up
        CtkDebug.log("IProjectManager.save(): " + project.getFile().getPath());

        sessionPreferences.put("lastProject", project.getFile().getPath());
        // set modified
        project.getInfo().setModified(new Date());

        //System.out.println(">> SAVE");
        controller.getDispatcher().trigger(
                new OnProjectManagerChange(project, ProjectManagerChangeKind.SAVE));

        //System.out.println(">> SAVE_COMPLETE");
        controller.getDispatcher().trigger(
                new OnProjectManagerChange(project, ProjectManagerChangeKind.SAVE_COMPLETE));

        finalizeSaveComplete();
    }

    protected void finalizeSaveComplete() throws IOException {
        //System.out.println(">> SAVE_COMPLETE flushProjectFile()");
        CtkDebug.log("IProjectManager; Save Complete, now saving project json file");

        String data = controller.getSerializeService().toPrettyString(project);
        FileUtils.writeStringToFile(project.getFile(), data);

        saveProjectPreferences();
    }

    private void saveProjectPreferences() throws IOException {
        String data = controller.getSerializeService().toPrettyString(sessionPreferences);
        FileUtils.writeStringToFile(sessionPreferencesFile, data);
    }

    @Override
    public Project load(File file) throws IOException {
        file = toProjectFile(file);
        if (!file.exists())
            throw new IOException("Project file does not exist");

        CtkDebug.log("IProjectManager.load():" + file.getAbsolutePath());

        project = controller.getSerializeService().fromFile(file, Project.class);
        project.open();
        controller.getDispatcher().trigger(
                new OnProjectManagerChange(project, ProjectManagerChangeKind.LOAD));
        return project;
    }

    @Override
    public Project create(File projectFile) throws IOException {
        project = new Project();
        project.setInitializing(true);
        project.setFile(new File(projectDirectory, projectFile.getName()));
        project.setInfo(createInfo());
        project.open();
        CtkDebug.log("IProjectManager.create(): " + project.getFile().getAbsolutePath());
        controller.getDispatcher().trigger(
                new OnProjectManagerChange(project, ProjectManagerChangeKind.CREATE));

        // create the projects resources directory which is the same name
        File directory = new File(projectDirectory, project.getName());
        FileUtils.forceMkdir(directory);

        // save the new Project
        finalizeSaveComplete();

        // adding a LOAD event here to keep consistent with all startup.
        // whether a project is created or loaded on start, mediators will always
        // get a load event at start.
        controller.getDispatcher().trigger(
                new OnProjectManagerChange(project, ProjectManagerChangeKind.LOAD));

        project.setInitializing(false);

        return project;
    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    private ProjectInfo createInfo() {
        ProjectInfo info = new ProjectInfo();
        info.setName("Untitled Project");
        info.setAuthor("Untitled Author");
        info.setCreated(new Date());
        info.setModified(new Date());
        info.setDescription("A new project");
        return info;
    }

    private File toProjectFile(File file) {
        if (file.isAbsolute())
            return file;
        return new File(projectDirectory, file.getPath());
    }

}
