////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.gdx.skin;

import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teotigraphix.gdx.IGdxScene;

/**
 * The {@link SkinLibrary} creates the default skin through code instead of a
 * JSON file.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public abstract class SkinLibrary {

    public static final String BUTTON_DOWN = "button_down";

    public static final String BUTTON_UP = "button_up";

    public static final String BUTTON_CHECKED = "button_checked";

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public SkinLibrary() {
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Subclasses overridde to add custom styles on top of the defaults defined
     * here.
     * 
     * @param skin The {@link Skin} to add styles to.
     * @see IGdxScene#initialize(com.teotigraphix.gdx.IGdxApplication)
     */
    public void initialize(Skin skin) {
        initializeButtonStyle(skin);
    }

    //--------------------------------------------------------------------------
    // Protected Style Definition :: Methods
    //--------------------------------------------------------------------------

    protected void initializeButtonStyle(Skin skin) {
        ButtonStyle style = new ButtonStyle();
        style.up = skin.getDrawable(BUTTON_UP);
        style.down = skin.getDrawable(BUTTON_DOWN);
        style.checked = skin.getDrawable(BUTTON_CHECKED);
        skin.add("default", style);
    }
}
