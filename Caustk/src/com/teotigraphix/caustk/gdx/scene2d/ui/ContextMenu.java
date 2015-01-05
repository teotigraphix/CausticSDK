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

package com.teotigraphix.caustk.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.gdx.scene2d.ui.MenuBar.MenuItem;
import com.teotigraphix.caustk.gdx.scene2d.ui.MenuRowRenderer.MenuRowRendererStyle;

public class ContextMenu extends Menu {

    private InputListener stageListener = new InputListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            hide();
            return false;
        }
    };

    public ContextMenu(Array<MenuItem> menuItems, WindowStyle windowStyle,
            MenuRowRendererStyle menuRowRendererStyle, Skin skin) {
        super(menuItems, windowStyle, menuRowRendererStyle, skin);
    }

    @Override
    public Dialog show(Stage stage) {
        stage.addListener(stageListener);
        return super.show(stage);
    }

    @Override
    public void hide() {
        Stage stage = getStage();
        if (stage != null)
            getStage().removeListener(stageListener);
        super.hide();
    }
}
