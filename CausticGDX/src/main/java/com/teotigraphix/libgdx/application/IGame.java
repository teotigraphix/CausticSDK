
package com.teotigraphix.libgdx.application;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.inject.Module;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.sound.ISoundGenerator;
import com.teotigraphix.libgdx.dialog.IDialogManager;
import com.teotigraphix.libgdx.screen.IScreen;

/**
 * The {@link IGame} API is the toplevel container for all user interface and
 * Caustic Core logic.
 */
public interface IGame extends ApplicationListener {

    /**
     * Returns the Application name passed during the game's construction.
     * <p>
     * The app name is used for the root application directory of the game's
     * resources and projects.
     */
    String getAppName();

    /**
     * Sets the current {@link IScreen} of the {@link IGame}, each screen has a
     * {@link Stage} filled with actors of the screen.
     * 
     * @param screen The {@link IScreen} to set current.
     */
    void setScreen(IScreen screen);

    /**
     * Returns the current {@link IScreen} of the {@link IGame}.
     */
    IScreen getScreen();

    /**
     * The platform specifc sound generator impl.
     * <p>
     * Subclasses of the {@link IGame} will accept the {@link ISoundGenerator}
     * impl through their constructor and save the reference.
     * <p>
     * <strong>Desktop</strong>
     * 
     * <pre>
     * MyGame listener = new Tones(new DesktopSoundGenerator());
     * new LwjglApplication(listener, config);
     * listener.initialize(new MyGameModule());
     * </pre>
     * <p>
     * <strong>Android</strong>
     * 
     * <pre>
     * MyGame listener = new MyGame(new AndroidSoundGenerator(this, 0x92D308C4));
     * initialize(listener, config);
     * listener.initialize(new MyGameModule());
     * </pre>
     */
    ISoundGenerator getSoundGenerator();

    /**
     * Returns the application controller.
     */
    ICaustkController getController();

    /**
     * Returns the dialog manager that works with the {@link IScreen}.
     */
    IDialogManager getDialogManager();

    /**
     * Called from the desktop or android bootstrap class.
     * <p>
     * The Desktop bootstrap is <code>main()</code> and the Android bootstrap is
     * <code>onCreate()</code>.
     * 
     * @param modules
     */
    void initialize(Module... modules);

}
