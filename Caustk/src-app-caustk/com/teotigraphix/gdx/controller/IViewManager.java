
package com.teotigraphix.gdx.controller;

import java.util.Map;

public interface IViewManager {

    void create();

    void load(Map<Integer, ViewBase> views);

    void addView(ViewBase view);

    ViewBase removeView(ViewBase view);

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
}
