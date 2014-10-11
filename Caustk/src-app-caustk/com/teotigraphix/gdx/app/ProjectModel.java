
package com.teotigraphix.gdx.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.utils.Array;
import com.teotigraphix.gdx.controller.ViewBase;
import com.teotigraphix.gdx.groove.ui.components.SceneViewChildData;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;

public class ProjectModel extends ApplicationComponent implements IProjectModel {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private ProjectState state;

    private Array<SceneViewChildData> views;

    private Array<ButtonBarItem> buttons;

    private Array<ButtonBarItem> viewButtons;

    //    @Override
    //    protected String getPreferenceId() {
    //        return getApplication().getApplicationId() + "/UIModel";
    //    }

    public ProjectState getState() {
        return state;
    }

    //--------------------------------------------------------------------------
    // Public Property :: API
    //--------------------------------------------------------------------------

    @Override
    public Collection<ViewBase> getViews() {
        ArrayList<ViewBase> values = new ArrayList<ViewBase>(state.getViews().values());
        Collections.sort(values, new Comparator<ViewBase>() {
            @Override
            public int compare(ViewBase lhs, ViewBase rhs) {
                return lhs.getIndex() > rhs.getIndex() ? 1 : -1;
            }
        });
        return values;
    }

    //----------------------------------
    // selectedView
    //----------------------------------

    @Override
    public ViewBase getSelectedView() {
        return getState().getSelectedView();
    }

    @Override
    public void setSelectedViewId(int viewId) {
        if (getState().getSelectedViewId() == viewId)
            return;
        getState().setSelectedViewId(viewId);
        getEventBus().post(new ProjectModelEvent(ProjectModelEventKind.ViewChange, this));
    }

    //----------------------------------
    // viewIndex
    //----------------------------------

    @Override
    public int getViewIndex() {
        return getSelectedView().getIndex();
    }

    /**
     * @param viewIndex
     * @see ProjectModelEventKind#ViewChange
     */
    @Override
    public void setViewIndex(int viewIndex) {
        if (getViewIndex() == viewIndex)
            return;
        state.setSelectedViewId(getViewIdByIndex(viewIndex));
        getEventBus().post(new ProjectModelEvent(ProjectModelEventKind.ViewChange, this));
    }

    @Override
    public ViewBase getViewByIndex(int viewIndex) {
        for (ViewBase view : state.getViews().values()) {
            if (view.getIndex() == viewIndex)
                return view;
        }
        return null;
    }

    private int getViewIdByIndex(int viewIndex) {
        for (ViewBase view : state.getViews().values()) {
            if (view.getIndex() == viewIndex)
                return view.getId();
        }
        return -1;
    }

    //----------------------------------
    // views
    //----------------------------------

    @Override
    public void setSceneViewIndex(int viewIndex) {
        if (state.getSceneViewIndex() == viewIndex)
            return;
        state.setSceneViewIndex(viewIndex);
        getEventBus().post(new ProjectModelEvent(ProjectModelEventKind.SceneViewChange, this));
    }

    @Override
    public int getSceneViewIndex() {
        return state.getSceneViewIndex();
    }

    @Override
    public Array<SceneViewChildData> getSceneViews() {
        return views;
    }

    @Override
    public void setSceneViews(Array<SceneViewChildData> views) {
        this.views = views;
    }

    //----------------------------------
    // buttons
    //----------------------------------

    @Override
    public Array<ButtonBarItem> getSceneButtons() {
        if (buttons != null)
            return buttons;

        buttons = new Array<ButtonBarItem>();
        for (SceneViewChildData data : views) {
            buttons.add(data.toButtonBarItem());
        }

        return buttons;
    }

    @Override
    public void setSceneButtons(Array<ButtonBarItem> buttons) {
        this.buttons = buttons;
    }

    //----------------------------------
    // viewButtons
    //----------------------------------

    @Override
    public Array<ButtonBarItem> getViewButtons() {
        return viewButtons;
    }

    @Override
    public void setViewButtons(Array<ButtonBarItem> viewButtons) {
        this.viewButtons = viewButtons;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ProjectModel() {
    }

    @Override
    public void restore(ProjectState state) {
        this.state = state;

        getEventBus().post(new ProjectModelEvent(ProjectModelEventKind.SceneViewChange, this));
        getEventBus().post(new ProjectModelEvent(ProjectModelEventKind.ViewChange, this));
    }
}
