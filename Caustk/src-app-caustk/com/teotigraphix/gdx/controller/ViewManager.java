
package com.teotigraphix.gdx.controller;

import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.controller.core.AbstractDisplay;
import com.teotigraphix.gdx.app.ApplicationComponent;
import com.teotigraphix.gdx.app.IProjectModel;

@Singleton
public abstract class ViewManager extends ApplicationComponent implements IViewManager {

    @Inject
    private IProjectModel projectModel;

    private AbstractDisplay display;

    private AbstractDisplay subDisplay;

    //----------------------------------
    // display
    //----------------------------------

    @Override
    public AbstractDisplay getDisplay() {
        return display;
    }

    protected void setDisplay(AbstractDisplay display) {
        this.display = display;
    }

    //----------------------------------
    // subDisplay
    //----------------------------------

    @Override
    public AbstractDisplay getSubDisplay() {
        return subDisplay;
    }

    protected void setSubDisplay(AbstractDisplay subDisplay) {
        this.subDisplay = subDisplay;
    }

    @Override
    public ViewBase getSelectedView() {
        return projectModel.getSelectedView();
    }

    @Override
    public void setSelectedView(int index) {
        projectModel.setViewIndex(index);
        getSelectedView().onActivate();
        flush();
    }

    @Override
    public ViewBase getView(int index) {
        return projectModel.getViewByIndex(index);
    }

    @Override
    public ViewBase getViewById(int viewId) {
        for (ViewBase view : projectModel.getViews()) {
            if (view.getId() == viewId)
                return view;
        }
        return null;
    }

    public ViewManager() {
    }

    @Override
    public void onArrowUp(boolean down) {
        getSelectedView().onArrowUp(down);
    }

    @Override
    public void onArrowRight(boolean down) {
        getSelectedView().onArrowRight(down);
    }

    @Override
    public void onArrowLeft(boolean down) {
        getSelectedView().onArrowLeft(down);
    }

    @Override
    public void onArrowDown(boolean down) {
        getSelectedView().onArrowDown(down);
    }

    @Override
    public void flush() {
        ViewBase selectedView = getSelectedView();
        selectedView.updateArrows();

        getDisplay().flush();
        getSubDisplay().flush();

        for (IViewManagerFlushListener listener : flushListeners) {
            listener.flush();
        }
    }

    private Array<IViewManagerFlushListener> flushListeners = new Array<IViewManagerFlushListener>();

    @Override
    public void addFlushListener(IViewManagerFlushListener listener) {
        flushListeners.add(listener);
    }

    @Override
    public void removeFlushListener(IViewManagerFlushListener listener) {
        flushListeners.removeValue(listener, false);
    }

    public void onStartUI() {

    }
}
