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

package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class KnobGroup extends ControlTable {

    private KnobItem[] items;

    public KnobGroup(KnobItem[] items, Skin skin) {
        super(skin);
        this.items = items;
        //styleClass = SliderGroupStyle.class;
    }

    @Override
    protected void createChildren() {
        for (KnobItem item : items) {
            TextKnob child = new TextKnob(item.getMin(), item.getMax(), item.getStepSize(),
                    item.getText(), getSkin());
            add(child).pad(4f);
        }
    }

    public static class KnobItem {

        private String text;

        public String getText() {
            return text;
        }

        private float min;

        public float getMin() {
            return min;
        }

        private float max;

        public float getMax() {
            return max;
        }

        private float stepSize;

        public float getStepSize() {
            return stepSize;
        }

        private Object data;

        public Object getData() {
            return data;
        }

        public KnobItem(String text, float min, float max, float stepSize, Object data) {
            this.text = text;
            this.min = min;
            this.max = max;
            this.stepSize = stepSize;
            this.data = data;
        }
    }

}
