
package com.teotigraphix.gdx.app;

import java.io.IOException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.core.CaustkProject;
import com.teotigraphix.gdx.controller.IPreferenceManager;

@Singleton
public class ApplicationModel extends ApplicationComponent implements IApplicationModel {

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
        APP_PREFERENCES = application.getApplicationId() + "/applicationModel";
        applicationPreferences = new ApplicationPreferences(getPreferences());
    }

    @Inject
    private IApplicationStates applicationStates;

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

        this.project = project;

        if (!project.isCreated()) {
            createProject(project);
        } else {
            loadProject(project);
        }
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

    @Override
    protected void construct() {
    }

    private void createProject(CaustkProject project) throws IOException {
        applicationStates.onProjectCreate(project);
        project.save();
    }

    private void loadProject(CaustkProject project) {
        fileManager.setStartupProject(project);
        applicationStates.onProjectLoad(project);
    }

    private void saveProject(CaustkProject project) {
        applicationStates.onProjectSave(project);
        try {
            project.save();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void closeProject(CaustkProject project) {
        applicationStates.onProjectClose(project);
    }

}
