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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.teotigraphix.caustk.gdx.scene2d.ui.TextKnob;

public class AutoTextKnob extends AutomationTableBase {

    private TextKnob knob;

    @Override
    public float getValue() {
        return knob.getValue();
    }

    @Override
    public boolean setValue(float value) {
        knob.setValue(value);
        return true;
    }

    public AutoTextKnob(Skin skin, AutomationItem automationItem) {
        super(skin, automationItem);
    }

    @Override
    protected void createChildren() {
        super.createChildren();
        AutomationItem item = getAutomationItem();
        knob = new TextKnob(item.getMin(), item.getMax(), 0.01f,
                item.getControl().getDisplayName(), getSkin(), "default");
        knob.create();
        knob.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fireChange();
            }
        });
        add(knob).expand().fill();
    }
}
