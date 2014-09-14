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
import com.google.common.eventbus.EventBus;
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
    // Private :: Variables
    //--------------------------------------------------------------------------

    private EventBus eventBus;

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
        construct();
    }

    //----------------------------------
    // eventBus
    //----------------------------------

    /**
     * The model's local {@link EventBus}.
     */
    @Override
    public final EventBus getEventBus() {
        return eventBus;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new component.
     */
    public ApplicationComponent() {
        eventBus = new EventBus();
    }

    /**
     * Initialize the model after the {@link ICaustkApplication} has been
     * injected through {@link #setApplication(ICaustkApplication)}.
     * <p>
     * Do not add anything here that is dependent on project state.
     */
    protected void construct() {
    }

}
