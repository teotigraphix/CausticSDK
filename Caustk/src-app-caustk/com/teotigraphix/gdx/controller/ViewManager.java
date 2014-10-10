
package com.teotigraphix.gdx.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.app.ApplicationComponent;
import com.teotigraphix.gdx.groove.ui.model.IUIModel;

@Singleton
public class ViewManager extends ApplicationComponent implements IViewManager {

    @Inject
    private IUIModel uiModel;

    @Override
    public ViewBase getSelectedView() {
        return uiModel.getSelectedView();
    }

    @Override
    public void setSelectedViewId(int selectedViewId) {
        uiModel.setSelectedViewId(selectedViewId);
    }

    @Override
    public ViewBase getView(int index) {
        return uiModel.getViewByIndex(index);
    }

    public ViewManager() {
    }

    //    @Override
    //    public void create() {
    //    }
    //
    //    /**
    //     * Loads the deserialized views into the manager from the project.
    //     * 
    //     * @param views The deserialized views.
    //     */
    //    @Override
    //    public void load(Map<Integer, ViewBase> views) {
    //        //this.views = views;
    //    }

    //    @Override
    //    public void addView(ViewBase view) {
    //        views.put(view.getId(), view);
    //    }
    //
    //    @Override
    //    public ViewBase removeView(ViewBase view) {
    //        return views.remove(view.getId());
    //    }
}
