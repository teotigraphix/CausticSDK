
package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class AdvancedListListener implements EventListener {

    @Override
    public boolean handle(Event event) {
        if (!(event instanceof AdvancedListEvent))
            return false;

        AdvancedListEvent e = (AdvancedListEvent)event;

        if (e.getKind() == AdvancedListEventKind.Change)
            changed(e, event.getTarget());
        if (e.getKind() == AdvancedListEventKind.OverChange)
            overChanged(e, event.getTarget());
        if (e.getKind() == AdvancedListEventKind.LongPress)
            longPress(e, event.getTarget());
        if (e.getKind() == AdvancedListEventKind.DoubleTap)
            doubleTap(e, event.getTarget());

        return false;
    }

    public void changed(AdvancedListEvent event, Actor actor) {
    }

    public void overChanged(AdvancedListEvent event, Actor actor) {
    }

    public void longPress(AdvancedListEvent event, Actor actor) {
    }

    public void doubleTap(AdvancedListEvent event, Actor actor) {
    }

    public enum AdvancedListEventKind {
        Change, OverChange, LongPress, DoubleTap
    }

    public static class AdvancedListEvent extends Event {

        private AdvancedListEventKind kind;

        private int selectedIndex;

        private int overIndex;

        private Object overSelection;

        private Object selection;

        public AdvancedListEventKind getKind() {
            return kind;
        }

        public void setKind(AdvancedListEventKind kind) {
            this.kind = kind;
        }

        public int getSelectedIndex() {
            return selectedIndex;
        }

        public int getOverIndex() {
            return overIndex;
        }

        public Object getSelection() {
            return selection;
        }

        public Object getOverSelection() {
            return overSelection;
        }

        public void setSelectedIndex(int selectedIndex, Object selection) {
            this.selectedIndex = selectedIndex;
            this.selection = selection;
        }

        public void setOverIndex(int overIndex, Object overSelection) {
            this.overIndex = overIndex;
            this.overSelection = overSelection;
        }

        public AdvancedListEvent() {
        }

        @Override
        public void reset() {
            super.reset();
            kind = null;
            selectedIndex = -1;
            selection = null;
            overSelection = null;
            overIndex = -1;
        }
    }
}
