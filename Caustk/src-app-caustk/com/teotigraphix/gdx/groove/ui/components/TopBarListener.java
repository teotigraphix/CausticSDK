
package com.teotigraphix.gdx.groove.ui.components;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public abstract class TopBarListener implements EventListener {

    @Override
    public boolean handle(Event event) {
        if (event instanceof TopBarEvent) {
            TopBarEvent e = (TopBarEvent)event;
            switch (e.getKind()) {
                case ViewIndexChange:
                    viewIndexChange(e, e.getIndex());
                    break;
            }
        }
        return false;
    }

    public abstract void viewIndexChange(TopBarEvent event, int index);

    public enum TopBarEventKind {
        ViewIndexChange
    }

    public static class TopBarEvent extends Event {

        private TopBarEventKind kind;

        private int index;

        public TopBarEventKind getKind() {
            return kind;
        }

        public int getIndex() {
            return index;
        }

        public TopBarEvent(TopBarEventKind kind, int index) {
            this.kind = kind;
            this.index = index;
        }

        @Override
        public void reset() {
            super.reset();
            kind = null;
            index = -1;
        }
    }
}
