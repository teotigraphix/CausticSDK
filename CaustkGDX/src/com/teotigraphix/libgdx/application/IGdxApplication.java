////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.libgdx.application;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.rack.ISoundGenerator;
import com.teotigraphix.libgdx.dialog.IDialogManager;
import com.teotigraphix.libgdx.screen.IScreen;

/**
 * The {@link IGdxApplication} API is the toplevel container for all user
 * interface and Caustic Core logic.
 */
public interface IGdxApplication extends ApplicationListener {

    /**
     * Returns the Application name passed during the game's construction.
     * <p>
     * The app name is used for the root application directory of the game's
     * resources and projects.
     */
    String getAppName();

    void setScreen(int id);

    /**
     * Sets the current {@link IScreen} of the {@link IGdxApplication}, each
     * screen has a {@link Stage} filled with actors of the screen.
     * 
     * @param screen The {@link IScreen} to set current.
     */
    void setScreen(IScreen screen);

    /**
     * Returns the current {@link IScreen} of the {@link IGdxApplication}.
     */
    IScreen getScreen();

    /**
     * The platform specifc sound generator impl.
     * <p>
     * Subclasses of the {@link IGdxApplication} will accept the
     * {@link ISoundGenerator} impl through their constructor and save the
     * reference.
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
    //void initialize(Module... modules);

}
