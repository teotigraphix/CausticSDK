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

import com.badlogic.gdx.Preferences;
import com.google.inject.Inject;
import com.teotigraphix.gdx.controller.IPreferenceManager;

/**
 * Injectable model's for application model, manager, state etc.
 */
public abstract class ApplicationComponent implements IApplicationComponent {

    @Inject
    private IPreferenceManager preferenceManager;

    private ICaustkApplication application;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // preferences
    //----------------------------------

    @Override
    public Preferences getPreferences() {
        return preferenceManager.get(getPreferenceId());
    }

    /**
     * Return the preference id for the sub class.
     * <p>
     * Override in subclass that uses preferences.
     */
    protected String getPreferenceId() {
        return null;
    }

    //----------------------------------
    // application
    //----------------------------------

    @Override
    public ICaustkApplication getApplication() {
        return application;
    }

    @Inject
    public void setApplication(ICaustkApplication application) {
        this.application = application;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new component.
     */
    public ApplicationComponent() {
    }

    //--------------------------------------------------------------------------
    // Protected Log :: Methods
    //--------------------------------------------------------------------------

    protected void log(String tag, String message) {
        getApplication().getLogger().log(tag, message);
    }

    protected void err(String tag, String message) {
        getApplication().getLogger().err(tag, message);
    }

    protected void err(String tag, String message, Throwable throwable) {
        getApplication().getLogger().err(tag, message, throwable);
    }
}
