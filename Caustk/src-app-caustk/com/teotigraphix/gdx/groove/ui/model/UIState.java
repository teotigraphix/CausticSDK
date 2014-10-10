
package com.teotigraphix.gdx.groove.ui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CaustkProject;
import com.teotigraphix.gdx.controller.ViewBase;

/**
 * The internal state for the {@link UIModel}.
 * <p>
 * Each {@link CaustkProject} owns an instance of this and will be serialized
 * with the project.
 */
public class UIState {

    //--------------------------------------------------------------------------
    // Serialization
    //--------------------------------------------------------------------------

    @Tag(0)
    private Map<Integer, ViewBase> views = new HashMap<Integer, ViewBase>();

    @Tag(1)
    private int selectedViewId = 0;

    @Tag(3)
    private int sceneViewIndex = 0;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

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

    public UIState() {
    }

}
