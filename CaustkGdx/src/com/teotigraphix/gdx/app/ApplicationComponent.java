
package com.teotigraphix.gdx.app;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

/**
 * Injectable model's for application model, manager, state etc.
 */
public abstract class ApplicationComponent implements IApplicationComponent {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private EventBus eventBus;

    private IApplication application;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // application
    //----------------------------------

    @Override
    public IApplication getApplication() {
        return application;
    }

    @Inject
    public void setApplication(IApplication application) {
        this.application = application;
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
}
