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

package com.teotigraphix.gdx.groove.ui.components.auto;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public abstract class AutomationTableListener implements EventListener {

    public AutomationTableListener() {
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof AutomationTableEvent) {
            AutomationTableEvent e = (AutomationTableEvent)event;
            switch (e.getKind()) {
                case ValueChange:
                    valueChange(e, e.getAutomationItem());
                    break;
            }
        }
        return false;
    }

    public abstract void valueChange(AutomationTableEvent event, AutomationItem item);

    public enum AutomationTableEventKind {
        ValueChange
    }

    public static class AutomationTableEvent extends Event {

        private AutomationTableEventKind kind;

        private AutomationItem automationItem;

        public AutomationTableEventKind getKind() {
            return kind;
        }

        public void setKind(AutomationTableEventKind kind) {
            this.kind = kind;
        }

        public AutomationItem getAutomationItem() {
            return automationItem;
        }

        public void setAutomationItem(AutomationItem automationItem) {
            this.automationItem = automationItem;
        }

        public AutomationTableEvent() {
        }

        @Override
        public void reset() {
            super.reset();
            kind = null;
            automationItem = null;
        }
    }

}
