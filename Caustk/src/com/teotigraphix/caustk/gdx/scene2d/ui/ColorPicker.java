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

package com.teotigraphix.caustk.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.teotigraphix.caustk.gdx.controller.view.GrooveColor;
import com.teotigraphix.caustk.gdx.groove.ui.components.UITable;
import com.teotigraphix.caustk.gdx.groove.ui.factory.StylesDefault;
import com.teotigraphix.caustk.gdx.scene2d.ui.ColorPickerListener.ColorPickerEvent;
import com.teotigraphix.caustk.gdx.scene2d.ui.ColorPickerListener.ColorPickerEventKind;

public class ColorPicker extends UITable {

    public ColorPicker(Skin skin) {
        super(skin);
    }

    @Override
    protected void createChildren() {

        GrooveColor[] values = GrooveColor.values();

        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                final GrooveColor color = values[index];
                Image image = new Image(getSkin().getDrawable(
                        StylesDefault.ColorPicker_swatch_background));
                image.setColor(color.getColor());
                image.addListener(new ActorGestureListener() {
                    @Override
                    public void tap(InputEvent event, float x, float y, int count, int button) {
                        ColorPickerEvent e = new ColorPickerEvent(ColorPickerEventKind.ColorChange,
                                color);
                        fire(e);
                    }
                });
                add(image).expand().fill().pad(1f);
                index++;
            }
            row();
        }
    }

}
