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

package com.teotigraphix.gdx.app;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.core.CaustkProject;
import com.teotigraphix.gdx.controller.IPreferenceManager;

@Singleton
public class ApplicationModel extends ApplicationComponent implements IApplicationModel {

    private static final String TAG = "ApplicationModel";

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private CaustkProject project;

    protected String APP_PREFERENCES = null;

    //--------------------------------------------------------------------------
    // Inject :: Variables
    //--------------------------------------------------------------------------

    private ApplicationPreferences applicationPreferences;

    @Inject
    private IPreferenceManager preferenceManager;

    @Inject
    private IFileManager fileManager;

    @Override
    @Inject
    public void setApplication(ICaustkApplication application) {
        super.setApplication(application);
        APP_PREFERENCES = application.getApplicationId() + "_applicationModel";
        applicationPreferences = new ApplicationPreferences(getPreferences());
    }

    @Inject
    private IApplicationStateHandlers applicationStates;

    //--------------------------------------------------------------------------
    // Public :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // preferenceId
    //----------------------------------

    @Override
    protected String getPreferenceId() {
        return APP_PREFERENCES;
    }

    //----------------------------------
    // applicationPreferences
    //----------------------------------

    @Override
    public ApplicationPreferences getApplicationPreferences() {
        return applicationPreferences;
    }

    //----------------------------------
    // project
    //----------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public <T extends CaustkProject> T getProject() {
        return (T)project;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends CaustkProject> void setProject(T project) throws IOException {
        T oldProject = (T)this.project;
        if (oldProject != null) {
            closeProject(oldProject);
        }

        log(TAG, "setProject() - " + project.getNativePath());
        this.project = project;

        if (!project.isCreated()) {
            createProject(project);
        } else {
            loadProject(project);
        }
    }

    @Override
    public void newProject(File projectLocation) throws IOException {
        CaustkProject project = fileManager.createProject(projectLocation);
        setProject(project);
    }

    @Override
    public void loadProject(File projectFile) throws IOException {
        CaustkProject project = fileManager.loadProject(projectFile);
        setProject(project);
    }

    @Override
    public void saveProjectAs(String projectName) throws IOException {
        saveProjectAs(new File(fileManager.getProjectsDirectory(), projectName));
    }

    /**
     * @param projectLocation The project directory, getName() returns the
     *            project name to copy.
     * @throws IOException
     */
    public void saveProjectAs(File projectLocation) throws IOException {
        File srcDir = project.getDirectory();
        File destDir = projectLocation;
        FileUtils.copyDirectory(srcDir, destDir);

        // load the copied project discreetly
        final File oldProjectFile = new File(projectLocation, srcDir.getName() + ".prj");
        File newProjectFile = new File(projectLocation, destDir.getName() + ".prj");

        CaustkProject newProject = fileManager.readProject(oldProjectFile);
        newProject.setRack(getApplication().getRack()); // needed for deserialization
        newProject.rename(newProjectFile);

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                // XXX This is NOTE DELETING
                FileUtils.deleteQuietly(oldProjectFile);
            }
        });

        loadProject(newProject.getFile());
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ApplicationModel() {
    }

    @Override
    public void save() {
        preferenceManager.save();
        saveProject(project);
    }

    @Override
    public void dispose() {
        save();
    }

    private void createProject(CaustkProject project) throws IOException {
        log(TAG, "createProject()");
        applicationStates.onProjectCreate(project);
        fileManager.setStartupProject(project);
        project.save();
    }

    private void loadProject(CaustkProject project) {
        log(TAG, "loadProject()");
        fileManager.setStartupProject(project);
        applicationStates.onProjectLoad(project);
    }

    private void saveProject(CaustkProject project) {
        log(TAG, "saveProject()");
        applicationStates.onProjectSave(project);
        try {
            project.save();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void closeProject(CaustkProject project) {
        log(TAG, "closeProject()");
        applicationStates.onProjectClose(project);
    }

}
