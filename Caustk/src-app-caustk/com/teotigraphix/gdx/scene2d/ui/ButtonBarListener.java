
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
            return false;
        }
        return false;
    }

    public abstract void selectedIndexChange(int selectedIndex);

    public static class ButtonBarChangeEvent extends ChangeEvent {

        private int selectedIndex;

        public int getSelectedIndex() {
            return selectedIndex;
        }

        void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
        }

        public ButtonBarChangeEvent() {
        }

        @Override
        public void reset() {
            super.reset();
            selectedIndex = -1;
        }
    }

}
