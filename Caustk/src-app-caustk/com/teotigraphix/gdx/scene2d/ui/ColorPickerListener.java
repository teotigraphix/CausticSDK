
package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.teotigraphix.caustk.controller.daw.Colors;

public abstract class ColorPickerListener implements EventListener {

    public ColorPickerListener() {
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof ColorPickerEvent) {
            ColorPickerEvent e = (ColorPickerEvent)event;
            switch (e.getKind()) {
                case ColorChange:
                    colorChange(e, e.getColor());
                    break;
            }
            return false;
        }
        return false;
    }

    public abstract void colorChange(ColorPickerEvent event, Colors color);

    public enum ColorPickerEventKind {
        ColorChange
    }

    public static class ColorPickerEvent extends Event {

        private ColorPickerEventKind kind;

        private Colors color;

        public ColorPickerEventKind getKind() {
            return kind;
        }

        public Colors getColor() {
            return color;
        }

        public ColorPickerEvent(ColorPickerEventKind kind, Colors colors) {
            this.kind = kind;
            this.color = colors;
        }

    }

}
