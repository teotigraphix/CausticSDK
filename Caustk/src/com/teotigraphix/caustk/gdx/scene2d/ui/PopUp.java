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

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class PopUp extends DialogBase {

    private String title;

    public PopUp(String title, Skin skin) {
        super("", skin);
        this.title = title;
        createChildren();
    }

    public PopUp(String title, WindowStyle windowStyle) {
        super("", windowStyle);
        this.title = title;
        createChildren();
    }

    public PopUp(String title, Skin skin, String windowStyleName) {
        super("", skin, windowStyleName);
        this.title = title;
        createChildren();
    }

    protected void createChildren() {
        debug();
        Label label = new Label(title, new LabelStyle(getStyle().titleFont,
                getStyle().titleFontColor));
        getContentTable().add(label).pad(4f).expandX().fillX();
        getContentTable().row();
    }

}
