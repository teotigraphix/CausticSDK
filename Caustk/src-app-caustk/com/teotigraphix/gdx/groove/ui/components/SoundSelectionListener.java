
package com.teotigraphix.gdx.groove.ui.components;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public abstract class SoundSelectionListener implements EventListener {

    public SoundSelectionListener() {
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof SoundSelectionEvent) {
            SoundSelectionEvent e = (SoundSelectionEvent)event;
            switch (e.getKind()) {
                case selectedIndexChange:
                    selectedIndexChange(e, e.getIndex());
                    break;
            }
        }
        return false;
    }

    public abstract void selectedIndexChange(SoundSelectionEvent event, int index);

    public enum SoundSelectionEventKind {
        selectedIndexChange
    }

    public static class SoundSelectionEvent extends Event {

        private SoundSelectionEventKind kind;

        private int index;

        public SoundSelectionEventKind getKind() {
            return kind;
        }

        public int getIndex() {
            return index;
        }

        public SoundSelectionEvent(SoundSelectionEventKind kind, int index) {
            this.kind = kind;
            this.index = index;
        }
    }
}
