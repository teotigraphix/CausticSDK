
package com.teotigraphix.gdx.groove.app;

import com.badlogic.gdx.Gdx;
import com.google.common.eventbus.Subscribe;
import com.teotigraphix.gdx.app.CaustkBehavior;
import com.teotigraphix.gdx.controller.IViewManager.ViewManagerRedrawUIEvent;

public abstract class GrooveBehavior extends CaustkBehavior {

    public GrooveBehavior() {
    }

    @Subscribe
    public void onViewManagerRefreshUIEvent(final ViewManagerRedrawUIEvent event) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                redrawView(event.getKind());
            }
        });
    }

    /**
     * Override in subclass and make kind concrete enum type.
     * 
     * @param kind the project's redraw enum for all its behaviors.
     */
    protected abstract void redrawView(Object kind);
}
