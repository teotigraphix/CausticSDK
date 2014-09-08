
package com.teotigraphix.gdx.app;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ApplicationModel extends ApplicationComponent implements IApplicationModel {

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
        // XXX        applicationPreferences = new ApplicationPreferences(getPreferences());
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
    public void setProject(ApplicationProject state) {
        this.project = state;
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
