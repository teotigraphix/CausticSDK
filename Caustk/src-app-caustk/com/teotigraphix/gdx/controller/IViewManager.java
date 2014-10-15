
package com.teotigraphix.gdx.controller;

import com.teotigraphix.caustk.controller.core.AbstractDisplay;
import com.teotigraphix.caustk.controller.core.AbstractSequencerView;
import com.teotigraphix.caustk.controller.helper.AbstractGrid;
import com.teotigraphix.caustk.groove.session.SceneManager;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.gdx.app.CaustkApplication;
import com.teotigraphix.gdx.app.ProjectState;

public interface IViewManager {

    PatternNode getSelectedPattern();

    AbstractSequencerView getSequencerView();

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

    void flush(boolean force);

    AbstractDisplay getSubDisplay();

    AbstractDisplay getDisplay();

    AbstractGrid getPads();

    void restore(ProjectState state);

    /**
     * Called when projects are loaded and the ui needs a clean redraw.
     * 
     * @see ViewManagerRefreshUIEvent
     */
    void onRefresh();

    /**
     * Called the last starup frame after behaviors have been created.
     * 
     * @see ApplicationStates#startUI()
     * @see ViewManagerStartUIEvent
     */
    void onStartUI();

    /**
     * {@link CaustkApplication#startScene()}
     * 
     * @see SceneManager#reset()
     * @see SceneManager#setScene(getInitialScene());
     * @see ViewManagerReStartUIEvent
     */
    void onRestartUI();

    public static class ViewManagerStartUIEvent {
    }

    public static class ViewManagerReStartUIEvent {
    }

    public static class ViewManagerRefreshUIEvent {
    }

    boolean isCurrent(int viewId);

}
