
package com.teotigraphix.libgdx.application;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.libgdx.screen.IScreen;

/**
 * An {@link ApplicationListener} that delegates to a {@link Screen}. This
 * allows an application to easily have multiple screens. </p>
 * <p>
 * Screens are not disposed automatically. You must handle whether you want to
 * keep screens around or dispose of them when another screen is set.
 */
public abstract class GDXGame implements IGame {

    @Inject
    Injector injector;

    private ICaustkController controller;

    @Override
    public ICaustkController getController() {
        return controller;
    }

    protected void setController(ICaustkController value) {
        controller = value;
    }

    private IScreen screen;

    @Override
    public void dispose() {
        if (screen != null)
            screen.hide();
    }

    @Override
    public void pause() {
        if (screen != null)
            screen.pause();
    }

    @Override
    public void resume() {
        if (screen != null)
            screen.resume();
    }

    @Override
    public void render() {
        if (screen != null)
            screen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height) {
        if (screen != null)
            screen.resize(width, height);
    }

    /**
     * Sets the current screen. {@link IScreen#hide()} is called on any old
     * screen, and {@link IScreen#show()} is called on the new screen, if any.
     * 
     * @param screen may be {@code null}
     */
    @Override
    public void setScreen(IScreen screen) {
        if (this.screen != null)
            this.screen.hide();
        this.screen = screen;
        if (this.screen != null) {
            // this will only happen at first start up with the splash
            // TODO Can I fix this?
            if (injector != null)
                injector.injectMembers(this.screen);
            this.screen.initialize(this);
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    /**
     * @return the currently active {@link IScreen}.
     */
    @Override
    public IScreen getScreen() {
        return screen;
    }

}
