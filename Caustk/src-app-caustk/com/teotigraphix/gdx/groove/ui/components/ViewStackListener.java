
package com.teotigraphix.gdx.groove.ui.components;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public abstract class ViewStackListener implements EventListener {

    @Override
    public boolean handle(Event event) {
        if (event instanceof ViewStackEvent) {
            ViewStackEvent e = (ViewStackEvent)event;
            switch (e.getKind()) {
                case SelectedIndexChange:
                    selectedIndexChange(e, e.getIndex(), e.getOldIndex());
                    break;
            }
        }
        return false;
    }

    public abstract void selectedIndexChange(ViewStackEvent event, int index, int oldIndex);

    public enum ViewStackEventKind {
        SelectedIndexChange
    }

    public static class ViewStackEvent extends Event {

        private ViewStackEventKind kind;

        private int index;

        private int oldIndex;

        @Override
        public ViewStack getTarget() {
            return (ViewStack)super.getTarget();
        }

        public ViewStackEventKind getKind() {
            return kind;
        }

        public int getIndex() {
            return index;
        }

        public int getOldIndex() {
            return oldIndex;
        }

        public ViewStackEvent(ViewStackEventKind kind, int index, int oldIndex) {
            this.kind = kind;
            this.index = index;
            this.oldIndex = oldIndex;
        }

        @Override
        public void reset() {
            super.reset();
            kind = null;
            index = -1;
            oldIndex = -1;
        }
    }
}
