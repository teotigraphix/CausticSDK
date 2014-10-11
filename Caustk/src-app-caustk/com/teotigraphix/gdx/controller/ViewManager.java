
package com.teotigraphix.gdx.controller;

import com.badlogic.gdx.utils.Array;
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
    public void setSelectedView(int index) {
        uiModel.setViewIndex(index);
        flush();
    }

    @Override
    public ViewBase getView(int index) {
        return uiModel.getViewByIndex(index);
    }

    @Override
    public ViewBase getViewById(int viewId) {
        for (ViewBase view : uiModel.getViews()) {
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
}
