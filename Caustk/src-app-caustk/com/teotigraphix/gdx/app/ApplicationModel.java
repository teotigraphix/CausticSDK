
package com.teotigraphix.gdx.app;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ApplicationModel extends ApplicationComponent implements IApplicationModel {

    public static final String LAST_PROJECT_PATH = "last-project-path";

    protected String APP_PREFERENCES = null;

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private ApplicationProject project;

    private ApplicationPreferences applicationPreferences;

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
    public ApplicationProject getProject() {
        return project;
    }

    @Override
    public void setProject(ApplicationProject project) {
        if (this.project != project) {
            closeProject(project);
        }

        this.project = project;

        if (!project.isCreated()) {
            project.setIsCreated();
            getEventBus().post(new ApplicationModelProjectCreateEvent(this.project));
        }

        openProject(project);

        getPreferences().putString(LAST_PROJECT_PATH, project.getLocation().getAbsolutePath());

        getEventBus().post(new ApplicationModelProjectLoadEvent(this.project));
    }

    private void openProject(ApplicationProject project) {
    }

    private void closeProject(ApplicationProject project) {
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
