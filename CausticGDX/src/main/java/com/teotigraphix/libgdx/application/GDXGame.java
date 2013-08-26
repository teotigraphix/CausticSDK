
package com.teotigraphix.libgdx.application;

import java.io.IOException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.sound.ISoundGenerator;
import com.teotigraphix.libgdx.screen.IScreen;

/**
 * An {@link ApplicationListener} that delegates to a {@link Screen}. This
 * allows an application to easily have multiple screens. </p>
 * <p>
 * Screens are not disposed automatically. You must handle whether you want to
 * keep screens around or dispose of them when another screen is set.
 */
public abstract class GDXGame implements IGame {

    private StartupExecutor executor;

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

    private boolean printFPS;

    private FPSLogger fpsLogger;

    public static final boolean DEV_MODE = true;

    private ISoundGenerator soundGenerator;

    public ISoundGenerator getSoundGenerator() {
        return soundGenerator;
    }

    public GDXGame(ISoundGenerator soundGenerator) {
        this.soundGenerator = soundGenerator;
        executor = new StartupExecutor();
        fpsLogger = new FPSLogger();
    }

    @Override
    public void initialize(Module... modules) {
        try {
            for (Module module : modules) {
                executor.addModule(module);
            }
            executor.start(this);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        setController(executor.getController());
    }

    @Override
    public void dispose() {
        soundGenerator.onDestroy();
        if (screen != null)
            screen.hide();
        try {
            getController().getApplication().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        // output the current FPS
        if (printFPS)
            fpsLogger.log();

        if (screen != null)
            screen.render(Gdx.graphics.getDeltaTime());

        if (getController() != null) {
            final float beat = getController().getSoundGenerator().getCurrentBeat();
            getController().getSongPlayer().setCurrentBeat(beat);
        }
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
        CtkDebug.err("Creating Screen " + screen);
        CtkDebug.err("injector " + injector);
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
