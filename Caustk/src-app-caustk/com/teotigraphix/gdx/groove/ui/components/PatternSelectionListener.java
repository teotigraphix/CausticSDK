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

package com.teotigraphix.gdx.groove.ui.components;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public abstract class PatternSelectionListener implements EventListener {

    public PatternSelectionListener() {
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof PatternSelectionEvent) {
            PatternSelectionEvent e = (PatternSelectionEvent)event;
            switch (e.getKind()) {

                case bankChange:
                    bankChange(e, e.getIndex());
                    break;

                case patternChange:
                    patternChange(e, e.getIndex());
                    break;

                case lengthChange:
                    lengthChange(e, e.getLength());
                    break;
            }
        }
        return false;
    }

    public abstract void bankChange(PatternSelectionEvent event, int index);

    public abstract void patternChange(PatternSelectionEvent event, int index);

    public abstract void lengthChange(PatternSelectionEvent event, int length);

    public enum PatternSelectionEventKind {
        bankChange,

        patternChange,

        lengthChange;
    }

    public static class PatternSelectionEvent extends Event {

        private PatternSelectionEventKind kind;

        private int index;

        private int length;

        public PatternSelectionEventKind getKind() {
            return kind;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public PatternSelectionEvent(PatternSelectionEventKind kind) {
            this.kind = kind;
        }
    }
}
