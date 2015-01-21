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
import com.teotigraphix.caustk.gdx.app.controller.view.GrooveColor;

public abstract class ColorPickerListener implements EventListener {

    public ColorPickerListener() {
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof ColorPickerEvent) {
            ColorPickerEvent e = (ColorPickerEvent)event;
            switch (e.getKind()) {
                case ColorChange:
                    colorChange(e, e.getColor());
                    break;
            }
            return false;
        }
        return false;
    }

    public abstract void colorChange(ColorPickerEvent event, GrooveColor color);

    public enum ColorPickerEventKind {
        ColorChange
    }

    public static class ColorPickerEvent extends Event {

        private ColorPickerEventKind kind;

        private GrooveColor color;

        public ColorPickerEventKind getKind() {
            return kind;
        }

        public GrooveColor getColor() {
            return color;
        }

        public ColorPickerEvent(ColorPickerEventKind kind, GrooveColor colors) {
            this.kind = kind;
            this.color = colors;
        }

    }

}
