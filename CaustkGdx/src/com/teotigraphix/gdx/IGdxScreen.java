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

package com.teotigraphix.gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teotigraphix.gdx.app.ScreenManager;

/**
 * The {@link IGdxScreen} API allows an application to display states as UI
 * screens.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public interface IGdxScreen extends Screen {

    /**
     * Returns the owning {@link IGdxApplication}.
     */
    IGdxApplication getApplication();

    /**
     * Returns the screen's {@link Stage}.
     * <p>
     * There is a one to one relationship with a {@link GdxScreen} and
     * {@link Stage}, the screen owns the stage.
     */
    Stage getStage();

    /**
     * Returns the themed skin used with this screen.
     * 
     * @return
     */
    Skin getSkin();

    /**
     * Returns whether this screen has been initialized.
     * <p>
     * The screen will only be created once during the call to
     * <code>setScreen()</code>. This template creation process is as follows
     * (only when uninitialized);
     * <ul>
     * <li>{@link #initialize(IGdxApplication)}</li>
     * <li>{@link #create()}</li>
     * </ul>
     * After initialized;
     * <ul>
     * <li>{@link #show()}</li>
     * <li>{@link #resize(int, int)}</li>
     * </ul>
     * 
     * @see #initialize(IGdxApplication)
     * @see ScreenManager#setScreen(IGdxScreen)
     */
    boolean isInitialized();

    /**
     * Initializes the {@link IGdxScreen}.
     * 
     * @param gdxApplication The owning {@link IGdxApplication}.
     */
    void initialize(IGdxApplication gdxApplication);

    /**
     * Creates the screen and it's contents.
     */
    void create();
}
