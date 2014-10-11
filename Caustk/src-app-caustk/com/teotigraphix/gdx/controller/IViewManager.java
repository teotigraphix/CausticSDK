
package com.teotigraphix.gdx.controller;

import com.teotigraphix.caustk.controller.core.AbstractDisplay;

public interface IViewManager {

    ViewBase getSelectedView();

    ViewBase getView(int index);

    ViewBase getViewById(int index);

    //    void addView(ViewBase view);
    //
    //    ViewBase removeView(ViewBase view);

    void onArrowUp(boolean down);

    void onArrowRight(boolean down);

    void onArrowLeft(boolean down);

    void onArrowDown(boolean down);

    public static enum ViewManagerEventKind {
        ViewAdd,

        ViewRemove,

        SelectedViewChange
    }

    public static class ViewManagerEvent {

        private ViewBase view;

        public ViewBase getView() {
            return view;
        }

        public ViewManagerEvent(ViewBase view) {
            this.view = view;
        }

    }

    void addFlushListener(IViewManagerFlushListener listener);

    void removeFlushListener(IViewManagerFlushListener listener);

    public interface IViewManagerFlushListener {
        void flush();
    }

    void setSelectedView(int index);

    void flush();

    AbstractDisplay getSubDisplay();

    AbstractDisplay getDisplay();

}
