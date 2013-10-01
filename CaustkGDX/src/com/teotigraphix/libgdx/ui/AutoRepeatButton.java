////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class AutoRepeatButton extends TextButton {

    public AutoRepeatButton(String text, Skin skin) {
        super(text, skin);
        init();
    }

    public AutoRepeatButton(String text, TextButtonStyle style) {
        super(text, style);
        init();
    }

    private Task task;

    private OnValueChangeListener listener;

    protected void init() {
        addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                event.cancel();
            }
        });

        addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // start the timer
                task = new Task() {
                    @Override
                    public void run() {
                        listener.valueChange();
                    }
                };

                Timer.schedule(task, 0f, 0.1f);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                task.cancel();
                task = null;
            }
        });
    }

    public AutoRepeatButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
        init();
    }

    public interface OnValueChangeListener {
        void valueChange();
    }

    public void setOnValueChangeListener(OnValueChangeListener l) {
        listener = l;
    }
}
