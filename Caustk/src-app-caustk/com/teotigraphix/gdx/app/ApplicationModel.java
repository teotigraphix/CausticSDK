
package com.teotigraphix.gdx.app;

import java.io.IOException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.core.CaustkProject;

@Singleton
public class ApplicationModel extends ApplicationComponent implements IApplicationModel {

    public static final String LAST_PROJECT_PATH = "last-project-path";

    protected String APP_PREFERENCES = null;

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private ApplicationPreferences applicationPreferences;

    private CaustkProject project;

    @Override
    protected String getPreferenceId() {
        return APP_PREFERENCES;
    }

    @Override
    @Inject
    public void setApplication(ICaustkApplication application) {
        super.setApplication(application);
        APP_PREFERENCES = application.getApplicationId() + "/applicationModel";
        applicationPreferences = new ApplicationPreferences(getPreferences());
    }

    //--------------------------------------------------------------------------
    // Public :: Properties
    //--------------------------------------------------------------------------

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

    @Override
    public CaustkProject getProject() {
        return project;
    }

    @Override
    public void setProject(CaustkProject project) throws IOException {
        CaustkProject oldProject = this.project;
        if (oldProject != null) {
            closeProject(oldProject);
        }

        this.project = project;

        if (!project.isCreated()) {
            project.save();
            getEventBus().post(new ApplicationModelProjectCreateEvent(project));
        }

        openProject(project);

        getPreferences().putString(LAST_PROJECT_PATH, project.getFile().getAbsolutePath());

        getEventBus().post(new ApplicationModelProjectLoadEvent(project));
    }

    private void openProject(CaustkProject project) {
    }

    private void closeProject(CaustkProject project) {
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ApplicationModel() {
    }

    @Override
    protected void construct() {
    }

}
