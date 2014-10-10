
package com.teotigraphix.gdx.controller;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;
import com.teotigraphix.gdx.app.ApplicationComponent;

@Singleton
public class ViewManager extends ApplicationComponent implements IViewManager {

    private Map<Integer, ViewBase> views;

    private int selectedViewId = -1;

    public int getSelectedViewId() {
        return selectedViewId;
    }

    public void setSelectedViewId(int selectedViewId) {
        this.selectedViewId = selectedViewId;
    }

    public ViewManager() {
    }

    @Override
    public void create() {
        views = new HashMap<Integer, ViewBase>();
    }

    /**
     * Loads the deserialized views into the manager from the project.
     * 
     * @param views The deserialized views.
     */
    @Override
    public void load(Map<Integer, ViewBase> views) {
        this.views = views;
    }

    @Override
    public void addView(ViewBase view) {
        views.put(view.getId(), view);
    }

    @Override
    public ViewBase removeView(ViewBase view) {
        return views.remove(view.getId());
    }
}
