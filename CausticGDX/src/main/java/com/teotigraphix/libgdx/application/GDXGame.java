
package com.teotigraphix.libgdx.application;

import java.io.IOException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.utils.ArrayMap;
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

    private ArrayMap<Integer, IScreen> screens = new ArrayMap<Integer, IScreen>();

    private ArrayMap<Integer, Class<? extends IScreen>> screenTypes = new ArrayMap<Integer, Class<? extends IScreen>>();

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

    private Module module;

    @Override
    public ISoundGenerator getSoundGenerator() {
        return soundGenerator;
    }

    public GDXGame(String appName, ISoundGenerator soundGenerator, Module module) {
        this.appName = appName;
        this.soundGenerator = soundGenerator;
        this.module = module;
        executor = new StartupExecutor();
        fps = new FPSLogger();
    }

    @Override
    public void initialize(Module... modules) {
        for (Module module : modules) {
            executor.addModule(module);
        }
        executor.initialize(this);
        setController(executor.getController());
    }

    @Override
    public void create() {
        initialize(module);
        controller.onStart();
        try {
            executor.start(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        controller.onDestroy();
    }

    @Override
    public void pause() {
        controller.onPause();
        if (screen != null)
            screen.pause();
    }

    @Override
    public void resume() {
        controller.onResume();
        if (screen != null)
            screen.resume();
    }

    @Override
    public void render() {

        if (pendingScreen != null) {
            setScreen(pendingScreen);
            pendingScreen = null;
        }

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

    protected void addScreen(int id, Class<? extends IScreen> type) {
        screenTypes.put(id, type);
    }

    private IScreen pendingScreen;

    @Override
    public void setScreen(int id) {
        IScreen screen = screens.get(id);
        if (screen == null) {
            Class<? extends IScreen> type = screenTypes.get(id);
            try {
                pendingScreen = type.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            screens.put(id, screen);
        }
    }

    /**
     * Sets the current screen. {@link IScreen#hide()} is called on any old
     * screen, and {@link IScreen#show()} is called on the new screen, if any.
     * 
     * @param value may be {@code null}
     */
    @Override
    public void setScreen(IScreen value) {
        if (screen != null)
            screen.hide();

        screen = value;

        if (screen != null) {
            // this will only happen at first start up with the splash
            // TODO Can I fix this?
            if (!screen.isInitialized()) {
                if (injector != null) {
                    injector.injectMembers(screen);
                }
                screen.initialize(this);
                screen.create();
            }
            screenProvider.setScreen(screen);
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
