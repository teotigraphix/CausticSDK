
package com.teotigraphix.caustk.gdx.scene2d.ui.app;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public abstract class ActionBarListener implements EventListener {

    public ActionBarListener() {
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof ActionBarEvent) {
            ActionBarEvent e = (ActionBarEvent)event;
            switch (e.getKind()) {
                case ActionExecute:
                    actionExecute(e);
                    break;
                case BackTap:
                    backTap(e);
                    break;
            }
        }
        return false;
    }

    public abstract void actionExecute(ActionBarEvent event);

    public abstract void backTap(ActionBarEvent event);

    public enum ActionBarEventKind {
        ActionExecute, BackTap
    }

    public static class ActionBarEvent extends Event {

        private ActionBarEventKind kind;

        private Object data;

        public ActionBarEventKind getKind() {
            return kind;
        }

        public void setKind(ActionBarEventKind kind) {
            this.kind = kind;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public ActionBarEvent() {
        }

        @Override
        public void reset() {
            super.reset();
            kind = null;
            data = null;
        }
    }
}
