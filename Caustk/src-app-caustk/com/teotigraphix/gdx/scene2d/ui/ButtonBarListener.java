
package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public abstract class ButtonBarListener implements EventListener {

    public ButtonBarListener() {
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof ButtonBarChangeEvent) {
            ButtonBarChangeEvent e = (ButtonBarChangeEvent)event;
            selectedIndexChange(e.getSelectedIndex());
            //selectedIndexChange(e, e.getSelectedIndex());
            return false;
        }
        return false;
    }

    public abstract void selectedIndexChange(int selectedIndex);

    public void selectedIndexChange(ButtonBarChangeEvent event, int selectedIndex) {
    }

    public static class ButtonBarChangeEvent extends Event {

        private int selectedIndex;

        private ChangeEvent changeEvent;

        public int getSelectedIndex() {
            return selectedIndex;
        }

        void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
        }

        public ChangeEvent getChangeEvent() {
            return changeEvent;
        }

        public void setChangeEvent(ChangeEvent event) {
            changeEvent = event;
        }

        public ButtonBarChangeEvent() {
        }

        @Override
        public void reset() {
            super.reset();
            selectedIndex = -1;
            changeEvent = null;
        }
    }
}
