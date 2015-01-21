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

package com.teotigraphix.caustk.gdx.scene2d.ui.auto;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.teotigraphix.caustk.gdx.scene2d.ui.TextSlider;

public class AutoTextSlider extends AutomationTableBase {

    private TextSlider slider;

    @Override
    public float getValue() {
        return slider.getValue();
    }

    @Override
    public void setValue(float value) {
        slider.setValue(value);
    }

    public AutoTextSlider(Skin skin, AutomationItem automationItem) {
        super(skin, automationItem);
    }

    @Override
    protected void createChildren() {
        super.createChildren();
        AutomationItem item = getAutomationItem();
        slider = new TextSlider(item.getLabel(), item.getMin(), item.getMax(), item.getStep(),
                true, getSkin());
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fireChange();
            }
        });
        add(slider).expand().fill();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        slider.setColor(getColor());
        super.draw(batch, parentAlpha);
    }

}
