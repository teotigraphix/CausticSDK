
package com.teotigraphix.gdx.groove.ui.components;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public abstract class SceneSelectionListener implements EventListener {

    public SceneSelectionListener() {
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof SceneSelectionEvent) {
            SceneSelectionEvent e = (SceneSelectionEvent)event;
            switch (e.getKind()) {

                case bankChange:
                    bankChange(e, e.getIndex());
                    break;

                case matrixChange:
                    matrixChange(e, e.getIndex());
                    break;
            }
        }
        return false;
    }

    public abstract void bankChange(SceneSelectionEvent event, int index);

    public abstract void matrixChange(SceneSelectionEvent event, int index);

    public enum SceneSelectionEventKind {
        bankChange,

        matrixChange;
    }

    public static class SceneSelectionEvent extends Event {

        private SceneSelectionEventKind kind;

        private int index;

        public SceneSelectionEventKind getKind() {
            return kind;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public SceneSelectionEvent(SceneSelectionEventKind kind) {
            this.kind = kind;
        }
    }
}
