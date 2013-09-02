
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class AutoRepeatButton extends GDXButton {

    public AutoRepeatButton(String text, Skin skin) {
        super(text, skin);
    }

    public AutoRepeatButton(String text, ButtonStyle style) {
        super(text, style);
    }

    private Task task;

    private OnValueChangeListener listener;

    @Override
    protected void init() {
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
