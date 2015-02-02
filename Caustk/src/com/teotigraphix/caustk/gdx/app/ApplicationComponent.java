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

import com.badlogic.gdx.Preferences;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.gdx.app.controller.command.CommandExecutionException;
import com.teotigraphix.caustk.node.RackInstance;

/**
 * Injectable model's for application model, manager, state etc.
 */
public abstract class ApplicationComponent implements IApplicationComponent {

    private static final String TAG = "ApplicationComponent";

    private CaustkApplication application;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // preferences
    //----------------------------------

    public Preferences getPreferences() {
        return application.getPreferenceManager().get(getPreferenceId());
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

    protected CaustkApplication getApplication() {
        return application;
    }

    @Inject
    public void setApplication(ICaustkApplication application) {
        this.application = (CaustkApplication)application;
    }

    //----------------------------------
    // eventBus
    //----------------------------------

    protected IProjectModel getProjectModel() {
        return getApplication().getProjectModel();
    }

    public final Preferences getGlobalPreferences() {
        return application.getGlobalPreferences();
    }

    public final EventBus getEventBus() {
        return getProjectModel().getRackAPI().getRackEventBus();
    }

    protected final ICaustkRack getRack() {
        return getProjectModel().getRackAPI().getRack();
    }

    protected final RackInstance getRackInstance() {
        return getProjectModel().getRackAPI().getRackInstance();
    }

    public final void execute(String message, Object... args) {
        try {
            application.getCommandManager().execute(message, args);
        } catch (CommandExecutionException e) {
            err(TAG, "Failed to execute command " + message, e);
        }
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

    protected void debug(String tag, String message) {
        application.getLogger().debug(tag, message);
    }

    protected void log(String tag, String message) {
        application.getLogger().log(tag, message);
    }

    protected void err(String tag, String message) {
        application.getLogger().err(tag, message);
    }

    protected void err(String tag, String message, Throwable throwable) {
        application.getLogger().err(tag, message, throwable);
    }
}
