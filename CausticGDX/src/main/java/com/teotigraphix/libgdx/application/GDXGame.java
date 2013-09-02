
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
import com.teotigraphix.caustk.sound.ISoundGenerator;
import com.teotigraphix.libgdx.dialog.IDialogManager;
import com.teotigraphix.libgdx.scene2d.IScreenProvider;
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

    @Inject
    IDialogManager dialogManager;

    @Inject
    IScreenProvider screenProvider;

    @Override
    public IDialogManager getDialogManager() {
        return dialogManager;
    }

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

    private FPSLogger fps;

    public static final boolean DEV_MODE = true;

    private String appName;

    @Override
    public String getAppName() {
        return appName;
    }

    private ISoundGenerator soundGenerator;

    @Override
    public ISoundGenerator getSoundGenerator() {
        return soundGenerator;
    }

    public GDXGame(String appName, ISoundGenerator soundGenerator) {
        this.appName = appName;
        this.soundGenerator = soundGenerator;
        executor = new StartupExecutor();
        fps = new FPSLogger();
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
        if (screen != null)
            screen.hide();
        try {
            getController().getApplication().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        soundGenerator.onDestroy();
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
            fps.log();

        if (screen != null)
            screen.render(Gdx.graphics.getDeltaTime());

        if (getController() != null) {
            final float measure = getController().getSoundGenerator().getCurrentSongMeasure();
            final float beat = getController().getSoundGenerator().getCurrentBeat();
            getController().getSystemSequencer().beatUpdate((int)measure, beat);
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
     * @param value may be {@code null}
     */
    @Override
    public void setScreen(IScreen value) {
        //CtkDebug.err("Creating Screen " + value);
        //CtkDebug.err("injector " + injector);
        if (screen != null)
            screen.hide();
        screen = value;

        if (screen != null) {
            // this will only happen at first start up with the splash
            // TODO Can I fix this?
            if (injector != null) {
                injector.injectMembers(screen);
                // XXX FIX, either fix this or make the splash screen dumb with no injections
                screenProvider.setScreen(screen);
            }
            screen.initialize(this);
            screen.show();
            screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
