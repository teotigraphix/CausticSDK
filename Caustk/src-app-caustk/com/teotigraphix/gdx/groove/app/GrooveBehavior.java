
package com.teotigraphix.gdx.groove.app;

import com.badlogic.gdx.Gdx;
import com.google.common.eventbus.Subscribe;
import com.teotigraphix.gdx.app.CaustkBehavior;
import com.teotigraphix.gdx.controller.IViewManager.ViewManagerReStartUIEvent;
import com.teotigraphix.gdx.controller.IViewManager.ViewManagerRefreshUIEvent;
import com.teotigraphix.gdx.controller.IViewManager.ViewManagerStartUIEvent;

public class GrooveBehavior extends CaustkBehavior {

    public GrooveBehavior() {
    }

    @Subscribe
    public void onViewManagerStartUIEvent(ViewManagerStartUIEvent event) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                refresh(true);
            }
        });
    }

    @Subscribe
    public void onViewManagerReStartUIEvent(ViewManagerReStartUIEvent event) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                refresh(false);
            }
        });
    }

    @Subscribe
    public void onViewManagerRefreshUIEvent(final ViewManagerRefreshUIEvent event) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                refresh(false);
                redrawView(event.getKind());
            }
        });
    }

    protected void redrawView(Object kind) {
    }

    /**
     * Called with view manager's start and restart ui callbacks.
     * 
     * @param isStartup Whether the refresh originated from a start or restart
     *            event.
     */
    protected final void refresh(boolean isStartup) {
        redraw(isStartup);
    }

    /**
     * Redraw the content pane, refreshing done in this method could possibly be
     * called every frame.
     * 
     * @param isStartup Whether the refresh originated from a start or restart
     *            event.
     */
    protected void redraw(boolean isStartup) {
    }

    /**
     * Redraws the content pane as a non startup state.
     */
    protected void redraw() {
        redraw(false);
    }
}
