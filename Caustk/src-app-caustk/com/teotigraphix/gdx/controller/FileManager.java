////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.gdx.controller;

import java.io.File;
import java.io.IOException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.core.CaustkProject;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.gdx.app.ApplicationComponent;
import com.teotigraphix.gdx.app.IApplicationModel;
import com.teotigraphix.gdx.app.IProjectFactory;

@Singleton
public class FileManager extends ApplicationComponent implements IFileManager {

    public static final String LAST_PROJECT_PATH = "last-project-path";

    private static final String TAG = "FileManager";

    //--------------------------------------------------------------------------
    // Inject :: Variables
    //--------------------------------------------------------------------------

    @Inject
    private IApplicationModel applicationModel;

    @Inject
    private IProjectFactory projectFactory;

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private File applicationDirectory;

    private File projectsDirectory;

    private File tempDirectory;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    public final File getApplicationDirectory() {
        return applicationDirectory;
    }

    @Override
    public final File getProjectsDirectory() {
        return projectsDirectory;
    }

    public final File getApplicationTempDirectory() {
        return tempDirectory;
    }

    private File getStartupProjectFile() {
        String path = applicationModel.getPreferences().getString(LAST_PROJECT_PATH, null);
        if (path == null)
            return new File("");
        return new File(path);
    }

    @Override
    public void setStartupProject(CaustkProject project) {
        applicationModel.getPreferences().putString(LAST_PROJECT_PATH,
                project.getFile().getAbsolutePath());
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public FileManager() {
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    // Called from ApplicationStates.construct()
    @Override
    public void setupApplicationDirectory() {
        applicationModel.getApplication().getLogger().log(TAG, "setupApplicationDirectory()");

        // create the application projects folder if not exists
        // ExternalStorage/AppName
        applicationDirectory = RuntimeUtils.getApplicationDirectory();
        if (!applicationDirectory.exists())
            applicationDirectory.mkdirs();

        // ExternalStorage/AppName/projects
        projectsDirectory = RuntimeUtils.getApplicationProjectsDirectory();
        if (!projectsDirectory.exists())
            projectsDirectory.mkdirs();

        // ExternalStorage/AppName/.temp
        tempDirectory = RuntimeUtils.getApplicationTempDirectory();
        if (!tempDirectory.exists())
            tempDirectory.mkdirs();
    }

    @Override
    public CaustkProject createOrLoadStartupProject() throws IOException {
        applicationModel.getApplication().getLogger().log(TAG, "createOrLoadStartupProject()");

        CaustkProject project = null;

        File projectFile = getStartupProjectFile();

        if (!projectFile.exists()) {
            err(TAG, "Could not load project file " + projectFile.getAbsolutePath());
            // create new Untitled Project
            project = createProject(new File(getNextProjectName()));

        } else {
            // load existing
            project = projectFactory.readProject(projectFile, true);
        }

        return project;
    }

    @Override
    public CaustkProject loadProject(File projectFile) throws IOException {
        CaustkProject project = projectFactory.readProject(projectFile, true);
        return project;
    }

    @Override
    public CaustkProject readProject(File projectFile) throws IOException {
        CaustkProject project = projectFactory.readProject(projectFile, false);
        return project;
    }

    @Override
    public CaustkProject createProject(File projectLocation) throws IOException {
        CaustkProject project = null;
        String projectName = projectLocation.getName();
        File projectBaseDirectory = toProjectDirectory(projectName);
        File projectFile = toProjectFile(projectBaseDirectory, projectName);
        log(TAG, "  Creating new project " + projectFile.getAbsolutePath());

        project = projectFactory.createDefaultProject(projectName, projectFile);

        return project;
    }

    @Override
    public String getNextProjectName() {
        String appName = getApplication().getApplicationName();
        String name = "Untitled " + appName + " Project";

        if (!new File(projectsDirectory, name).exists())
            return name;

        File projectsDirectory = getProjectsDirectory();
        File testDirectory = null;

        for (int i = 1; i < 100; i++) {
            testDirectory = new File(projectsDirectory, name + " " + i);
            if (!testDirectory.exists())
                break;
        }

        return testDirectory.getName();
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    private File toProjectFile(File projectDirectory, String projectName) {
        return new File(projectDirectory, projectName + ".prj");
    }

    private File toProjectDirectory(String projectName) {
        File directory = new File(getProjectsDirectory(), projectName);
        // create the container directory for the project
        if (!directory.exists())
            directory.mkdirs();
        return directory;
    }

}
