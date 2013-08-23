
package com.teotigraphix.libgdx.model;

import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.application.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkController;

public interface IApplicationModel {

    /**
     * The {@link IApplicationModel}'s local dispatcher.
     */
    IDispatcher getDispatcher();
    
    /**
     * Returns the application's name.
     */
    String getName();
    
    /**
     * Returns whether the application state is dirty.
     */
    boolean isDirty();

    /**
     * Calls {@link #setDirty(boolean)} with true.
     */
    void setDirty();

    /**
     * Sets the application state dirty.
     * 
     * @param value true sets dirty, false resets state after save.
     */
    void setDirty(boolean value);

    /**
     * Starts the model hierarchy.
     * 
     * @see OnApplicationModelStart
     * @see ICaustkController#getDispatcher()
     */
    void start();

    /**
     * Called as the final phase of startup.
     * <p>
     * All mediators react to this final assignment of model data.
     */
    void run();

    //--------------------------------------------------------------------------
    // Events
    //--------------------------------------------------------------------------

    /**
     * Dispatched after the {@link ICaustkApplication#start()} has been called
     * and before the project is loaded.
     * 
     * @see ICaustkController#getDispatcher()
     */
    public static class OnApplicationModelStart {
    }

    /**
     * Dispatched at the end of the startup phase, right before the window/view
     * is shown.
     * <p>
     * All final model load can be done here, mediators are all wired and
     * listening to model changes.
     */
    public static class OnApplicationModelRun {
    }

    /**
     * Dispatched when the Application has become dirty or was cleaned by a
     * save.
     * 
     * @see ApplicationModel#getDispatcher()
     */
    public static class OnApplicationModelDirtyChanged {

        private boolean dirty;

        public final boolean isDirty() {
            return dirty;
        }

        public OnApplicationModelDirtyChanged(boolean dirty) {
            this.dirty = dirty;
        }
    }


}
