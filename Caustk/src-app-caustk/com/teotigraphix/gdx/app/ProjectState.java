
package com.teotigraphix.gdx.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.groove.session.SessionManager;
import com.teotigraphix.gdx.controller.ViewBase;

/**
 * The internal state for the {@link IApplicationModel} held within the project.
 * <p>
 * Each {@link Project} owns an instance of this and will be serialized with the
 * project.
 */
public class ProjectState {

    //--------------------------------------------------------------------------
    // Serialization
    //--------------------------------------------------------------------------

    @Tag(0)
    private Project project;

    @Tag(10)
    private SessionManager sessionManager;

    @Tag(20)
    private Map<Integer, ViewBase> views = new HashMap<Integer, ViewBase>();

    @Tag(21)
    private int selectedViewId = 0;

    @Tag(22)
    private int sceneViewIndex = 0;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // project
    //----------------------------------

    public Project getProject() {
        return project;
    }

    //----------------------------------
    // sessionManager
    //----------------------------------

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    //----------------------------------
    // sceneViewIndex
    //----------------------------------

    /**
     * The view stack index of the Scene ViewStack.
     */
    public int getSceneViewIndex() {
        return sceneViewIndex;
    }

    public void setSceneViewIndex(int sceneViewIndex) {
        this.sceneViewIndex = sceneViewIndex;
    }

    //----------------------------------
    // views
    //----------------------------------

    public ViewBase getView(int viewId) {
        return views.get(viewId);
    }

    public Map<Integer, ViewBase> getViews() {
        return views;
    }

    public Collection<ViewBase> getViewsSorted() {
        ArrayList<ViewBase> values = new ArrayList<ViewBase>(views.values());
        Collections.sort(values, new Comparator<ViewBase>() {
            @Override
            public int compare(ViewBase lhs, ViewBase rhs) {
                return lhs.getIndex() > rhs.getIndex() ? 1 : -1;
            }
        });
        return values;
    }

    protected void addView(ViewBase view) {
        views.put(view.getId(), view);
    }

    //----------------------------------
    // selectedViewId
    //----------------------------------

    /**
     * Returns the selected {@link ViewBase} based on the
     * {@link #getSelectedViewId()}.
     */
    public ViewBase getSelectedView() {
        return views.get(selectedViewId);
    }

    public void setSelectedViewId(int selectedViewId) {
        this.selectedViewId = selectedViewId;
    }

    public int getSelectedViewId() {
        return selectedViewId;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    protected ProjectState() {
    }

    public ProjectState(Project project) {
        this.project = project;
    }

    public void initialize() {
    }

    public void create() {
        sessionManager = new SessionManager(project.getRackNode());
        sessionManager.initialize();
        sessionManager.create();
    }

    public void load() {
        sessionManager.load();
    }

}
