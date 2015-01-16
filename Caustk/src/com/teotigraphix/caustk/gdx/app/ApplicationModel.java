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

package com.teotigraphix.caustk.gdx.app;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.gdx.controller.IFileManager;
import com.teotigraphix.caustk.gdx.controller.IPreferenceManager;

@Singleton
public class ApplicationModel extends ApplicationComponent implements IApplicationModel {

    private static final String TAG = "ApplicationModel";

    //--------------------------------------------------------------------------
    // Inject :: Variables
    //--------------------------------------------------------------------------

    private ApplicationPreferences applicationPreferences;

    @Inject
    private IPreferenceManager preferenceManager;

    @Inject
    private IProjectModel projectModel;

    @Inject
    private IFileManager fileManager;

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private boolean dirty = false;

    protected String APP_PREFERENCES = null;

    @Override
    @Inject
    public void setApplication(ICaustkApplication application) {
        super.setApplication(application);
        APP_PREFERENCES = application.getApplicationId() + "_applicationModel";
        applicationPreferences = new ApplicationPreferences(getPreferences());
    }

    protected final IProjectModelWrite getProjectModelWrittable() {
        return (IProjectModelWrite)projectModel;
    }

    //--------------------------------------------------------------------------
    // Public :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // dirty
    //----------------------------------

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty() {
        setDirty(true);
    }

    private void setDirty(boolean dirty) {
        if (dirty == this.dirty)
            return;
        this.dirty = dirty;
        getEventBus()
                .post(new ApplicationModelEvent(this, ApplicationModelEventKind.IsDirtyChange));
    }

    //----------------------------------
    // applicationPreferences
    //----------------------------------

    @Override
    public ApplicationPreferences getApplicationPreferences() {
        return applicationPreferences;
    }

    //----------------------------------
    // preferenceId
    //----------------------------------

    @Override
    protected String getPreferenceId() {
        return APP_PREFERENCES;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ApplicationModel() {
    }

    @Override
    public void save() {
        // Always save preferences
        preferenceManager.save();
        if (dirty) {
            try {
                getProjectModelWrittable().save();
                setDirty(false);
            } catch (IOException e) {
                err(TAG, "ApplicationModel.save(); failed", e);
            }
        }
    }

    @Override
    public void dispose() {
        save();
    }

    //--------------------------------------------------------------------------
    // Project API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void newProject(File projectLocation) throws IOException {
        Project project = fileManager.createProject(projectLocation);
        getProjectModelWrittable().setProject(project);
    }

    @Override
    public void loadProject(File projectFile) throws IOException {
        Project project = fileManager.loadProject(projectFile);
        getProjectModelWrittable().setProject(project);
    }

    @Override
    public File saveProjectAs(String projectName) throws IOException {
        return saveProjectAs(new File(fileManager.getProjectsDirectory(), projectName));
    }

    /**
     * @param projectLocation The project directory, getName() returns the
     *            project name to copy.
     * @throws java.io.IOException
     */
    public File saveProjectAs(File projectLocation) throws IOException {
        File srcDir = projectModel.getProject().getDirectory();
        File destDir = projectLocation;
        FileUtils.copyDirectory(srcDir, destDir);

        // load the copied project discreetly
        final File oldProjectFile = new File(projectLocation, srcDir.getName() + ".prj");
        File newProjectFile = new File(projectLocation, destDir.getName() + ".prj");

        Project newProject = fileManager.readProject(oldProjectFile);
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

        return newProjectFile;
    }

    @Override
    public File exportProject(File file, ApplicationExportType exportType) throws IOException {
        File srcDir = projectModel.getProject().getDirectory();
        File exportedFile = new File(srcDir, "exported/");
        exportedFile.mkdirs();
        // NO .caustic ext
        // XXX Can't have spaces, must replace all spaces with '-'
        exportedFile = new File(exportedFile, file.getName().replaceAll(" ", "-") + ".caustic");
        File savedFile = projectModel.getProject().getRack().getRackNode().saveSongAs(exportedFile);
        return savedFile;
    }
}
