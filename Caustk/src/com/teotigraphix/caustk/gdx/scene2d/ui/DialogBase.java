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

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class DialogBase extends Dialog {

    private float explicitWidth = -1f;

    public float getExplicitWidth() {
        return explicitWidth;
    }

    private float explicitHeight = -1f;

    public float getExplicitHeight() {
        return explicitHeight;
    }

    public void setExplicitSize(float width, float height) {
        explicitWidth = width;
        explicitHeight = height;
    }

    @Override
    public float getPrefWidth() {
        if (explicitWidth != -1)
            return explicitWidth;
        return super.getPrefWidth();
    }

    @Override
    public float getPrefHeight() {
        if (explicitHeight != -1)
            return explicitHeight;
        return super.getPrefHeight();
    }

    public DialogBase(String title, Skin skin) {
        super(title, skin);
    }

    public DialogBase(String title, Skin skin, WindowStyle windowStyle) {
        super(title, windowStyle);
        setSkin(skin);
    }

    public DialogBase(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

}
