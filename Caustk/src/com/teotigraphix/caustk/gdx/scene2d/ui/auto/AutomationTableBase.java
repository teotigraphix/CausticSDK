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

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Pools;
import com.teotigraphix.caustk.gdx.scene2d.ui.app.UITable;
import com.teotigraphix.caustk.gdx.scene2d.ui.auto.AutomationTableListener.AutomationTableEvent;
import com.teotigraphix.caustk.gdx.scene2d.ui.auto.AutomationTableListener.AutomationTableEventKind;

public abstract class AutomationTableBase extends UITable {

    public abstract float getValue();

    public abstract void setValue(float value);

    private AutomationItem automationItem;

    public AutomationItem getAutomationItem() {
        return automationItem;
    }

    public AutomationTableBase(Skin skin, AutomationItem automationItem) {
        super(skin);
        this.automationItem = automationItem;
    }

    @Override
    protected void createChildren() {
        addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (count == 2) {
                    resetDefaultValue();
                }
            }
        });
    }

    protected void resetDefaultValue() {
        setValue(automationItem.getControl().getDefaultValue());
    }

    protected void fireChange() {
        automationItem.setValue(getValue());

        AutomationTableEvent event = Pools.obtain(AutomationTableEvent.class);
        event.setKind(AutomationTableEventKind.ValueChange);
        event.setAutomationItem(automationItem);
        fire(event);
        Pools.free(event);
    }

}
