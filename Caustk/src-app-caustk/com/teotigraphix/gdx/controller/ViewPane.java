
package com.teotigraphix.gdx.controller;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.gdx.app.ProjectState;

/**
 * A view pane is a child of the {@link IViewManager} and saves it's state on
 * the project.
 * <p>
 * A {@link ViewPane} will wrap more complex ui logic that exists between a UI
 * component (Pane) and it's Behavior (PaneBehavior).
 */
public class ViewPane {

    // subclass tags start at 50

    @Tag(0)
    private String id;

    private ProjectState state;

    private IViewManager viewManager;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    public String getId() {
        return id;
    }

    public ProjectState getState() {
        return state;
    }

    public IViewManager getViewManager() {
        return viewManager;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    protected ViewPane() {
    }

    public ViewPane(String id) {
        this.id = id;
    }

    /**
     * Attaches the manager to the view.
     * 
     * @param viewManager
     */
    public void attachTo(IViewManager viewManager, ProjectState state) {
        this.viewManager = viewManager;
        this.state = state;
    }

    /**
     * Called when the pane is activated/enabled and visible.
     */
    public void onActivate() {
    }

    /**
     * Called when the pane is deactivated/disabled and invisible.
     */
    public void onDeactivate() {
    }

    protected void post(Object event) {
        CaustkRuntime.getInstance().getApplication().getEventBus().post(event);
    }
}
