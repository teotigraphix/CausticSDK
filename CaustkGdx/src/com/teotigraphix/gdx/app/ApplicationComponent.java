
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

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private EventBus eventBus;

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
     */
    protected abstract String getPreferenceId();

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
     * Initialize the model after the {@link ICaustkApplication} has been injected.
     */
    protected abstract void construct();

}
