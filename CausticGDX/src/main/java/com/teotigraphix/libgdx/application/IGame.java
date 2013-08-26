
package com.teotigraphix.libgdx.application;

import com.badlogic.gdx.ApplicationListener;
import com.google.inject.Module;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.sound.ISoundGenerator;
import com.teotigraphix.libgdx.screen.IScreen;

public interface IGame extends ApplicationListener {

    ISoundGenerator getSoundGenerator();

    ICaustkController getController();

    void setScreen(IScreen screen);

    IScreen getScreen();

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
