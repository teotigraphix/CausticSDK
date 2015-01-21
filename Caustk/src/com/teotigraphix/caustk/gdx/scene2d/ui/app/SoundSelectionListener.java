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
