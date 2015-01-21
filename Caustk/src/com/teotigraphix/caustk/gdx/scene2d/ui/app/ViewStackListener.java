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

package com.teotigraphix.caustk.gdx.scene2d.ui.app;

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
