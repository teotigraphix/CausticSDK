
package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public abstract class PaneStackListener implements EventListener {

    public PaneStackListener() {
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof PaneStackEvent) {
            PaneStackEvent e = (PaneStackEvent)event;
            selecteIndexChange(e, e.getIndex());
        }
        return false;
    }

    public abstract void selecteIndexChange(PaneStackEvent event, int index);

    public enum PaneStackEventKind {
        SelectedIndexChange
    }

    public static class PaneStackEvent extends Event {

        private PaneStackEventKind kind;

        private int index;

        @Override
        public PaneStack getTarget() {
            return (PaneStack)super.getTarget();
        }

        public PaneStackEventKind getKind() {
            return kind;
        }

        public int getIndex() {
            return index;
        }

        public PaneStackEvent(PaneStackEventKind kind, int index) {
            this.kind = kind;
            this.index = index;
        }

        @Override
        public void reset() {
            super.reset();
            kind = null;
        }
    }
}
