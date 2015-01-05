////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.gdx.scene2d.ui;

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
