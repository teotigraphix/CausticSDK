
package com.teotigraphix.gdx.controller;

public interface IViewManager {

    //    void create();
    //
    //    void load(Map<Integer, ViewBase> views);
    ViewBase getSelectedView();

    void setSelectedViewId(int selectedViewId);

    ViewBase getView(int index);

    //    void addView(ViewBase view);
    //
    //    ViewBase removeView(ViewBase view);

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
