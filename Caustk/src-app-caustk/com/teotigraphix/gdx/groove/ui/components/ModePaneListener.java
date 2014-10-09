
package com.teotigraphix.gdx.groove.ui.components;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public abstract class ModePaneListener implements EventListener {

    public ModePaneListener() {
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof ModePaneEvent) {
            ModePaneEvent e = (ModePaneEvent)event;
            switch (e.getKind()) {
                case selectedIndexChange:
                    selectedIndexChange(e, e.getIndex());
                    break;
            }
        }
        return false;
    }

    public abstract void selectedIndexChange(ModePaneEvent event, int index);

    public enum ModePaneEventKind {
        selectedIndexChange
    }

    public static class ModePaneEvent extends Event {

        private ModePaneEventKind kind;

        private int index;

        public ModePaneEventKind getKind() {
            return kind;
        }

        public int getIndex() {
            return index;
        }

        public ModePaneEvent(ModePaneEventKind kind, int index) {
            this.kind = kind;
            this.index = index;
        }
    }
}
