
package com.teotigraphix.gdx.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.gdx.controller.ViewBase;
import com.teotigraphix.gdx.controller.ViewPane;

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

    @Tag(20)
    private Map<Integer, ViewBase> views = new HashMap<Integer, ViewBase>();

    @Tag(21)
    private int selectedViewId = 0;

    @Tag(22)
    private int sceneViewIndex = 0;

    @Tag(23)
    private Map<String, ViewPane> panes = new HashMap<String, ViewPane>();

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

    public Map<String, ViewPane> getPanes() {
        return panes;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    protected ProjectState() {
    }

    public ProjectState(Project project) {
        this.project = project;
    }

    //--------------------------------------------------------------------------
    // Public Callback :: Methods
    //--------------------------------------------------------------------------

    /**
     * @see Project#onInitialize()
     */
    public void initialize() {
    }

    /**
     * @see Project#onCreate()
     */
    public void create() {
    }

    /**
     * @see Project#onLoad()
     */
    public void load() {
    }

}
