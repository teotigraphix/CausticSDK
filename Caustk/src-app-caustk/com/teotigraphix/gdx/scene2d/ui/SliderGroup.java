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

package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teotigraphix.gdx.scene2d.ControlTable;

public class SliderGroup extends ControlTable {

    private SliderItem[] items;

    public SliderGroup(SliderItem[] items, Skin skin) {
        super(skin);
        this.items = items;
        //styleClass = SliderGroupStyle.class;
    }

    protected void createChildren() {
        for (SliderItem item : items) {
            TextSlider slider = new TextSlider(item.getText(), item.getMin(), item.getMax(),
                    item.getStepSize(), true, getSkin());
            add(slider).pad(4f);
        }
    }

    public static class SliderItem {

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

        public SliderItem(String text, float min, float max, float stepSize, Object data) {
            this.text = text;
            this.min = min;
            this.max = max;
            this.stepSize = stepSize;
            this.data = data;
        }
    }

    public static class SliderGroupStyle {

    }

    @Override
    public String getHelpText() {
        // TODO Auto-generated method stub
        return null;
    }
}
