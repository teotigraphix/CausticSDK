
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SliderGroup extends ControlTable {

    private SliderItem[] items;

    public SliderGroup(SliderItem[] items, Skin skin) {
        super(skin);
        this.items = items;
        styleClass = SliderGroupStyle.class;
    }

    @Override
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
}
