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

package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Pools;

public class ToggleButtonInternal extends TextButton {

    @Override
    public void setChecked(boolean isChecked) {
        setChecked(isChecked, true);
    }

    @SuppressWarnings("unchecked")
    public void setChecked(boolean isChecked, boolean fireEvent) {
        if (this.isChecked == isChecked)
            return;
        if (buttonGroup != null && !buttonGroup.canCheck(this, isChecked))
            return;
        this.isChecked = isChecked;
        if (fireEvent) {
            ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
            if (fire(changeEvent))
                this.isChecked = !isChecked;
            Pools.free(changeEvent);
        }
    }

    public ToggleButtonInternal(String text, Skin skin) {
        super(text, skin);
    }

    public ToggleButtonInternal(String text, TextButtonStyle style) {
        super(text, style);
    }

    public ToggleButtonInternal(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

}
