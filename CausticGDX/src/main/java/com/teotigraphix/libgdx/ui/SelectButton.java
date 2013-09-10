
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class SelectButton extends ControlTable {

    private Button button;

    private Label label;

    private boolean noEvent;

    //--------------------------------------------------------------------------
    // Property :: API
    //--------------------------------------------------------------------------

    //----------------------------------
    // isGroup
    //----------------------------------

    private boolean isGroup;

    public boolean isGroup() {
        return isGroup;
    }

    public void setIsGroup(boolean value) {
        isGroup = value;
    }

    //----------------------------------
    // isToggle
    //----------------------------------

    private boolean isToggle;

    public boolean isToggle() {
        return isToggle;
    }

    public void setIsToggle(boolean value) {
        isToggle = value;
    }

    //----------------------------------
    // selected
    //----------------------------------

    public boolean isSelected() {
        return button.isChecked();
    }

    public void select(boolean selected) {
        if (button.isChecked() == selected)
            return;
        noEvent = true;
        button.setChecked(selected);
        noEvent = false;
    }

    //----------------------------------
    // text
    //----------------------------------

    private String text;

    public String getText() {
        return text;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SelectButton(String text, String styleName, Skin skin) {
        super(skin);
        this.text = text;
        setStyleName(styleName);
        styleClass = SelectButtonStyle.class;
    }

    //--------------------------------------------------------------------------
    // Overridden :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createChildren() {
        SelectButtonStyle style = getStyle();
        button = new Button(style);
        button.addCaptureListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int index) {
                if (isGroup && button.isChecked())
                    event.cancel();
            }
        });
        button.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!isToggle)
                    event.cancel();
                //if (isGroup && button.isChecked())
                //   event.cancel();
                if (!noEvent && onSelectButtonListener != null)
                    onSelectButtonListener.onChange(SelectButton.this);
            }
        });
        add(button).size(style.width, style.height);
        label = new Label(text, new LabelStyle(style.font, style.fontColor));
        row();
        add(label);
    }

    //--------------------------------------------------------------------------
    // Listener
    //--------------------------------------------------------------------------

    private OnSelectButtonListener onSelectButtonListener;

    public void setOnSelectButtonListener(OnSelectButtonListener l) {
        onSelectButtonListener = l;
    }

    public interface OnSelectButtonListener {
        void onChange(SelectButton button);
    }

    //--------------------------------------------------------------------------
    // Style
    //--------------------------------------------------------------------------

    public static class SelectButtonStyle extends TextButtonStyle {

        public Drawable progressOverlay;

        public float width = 50f;

        public float height = 30;

        public SelectButtonStyle() {
        }

        public SelectButtonStyle(Drawable up, Drawable down, Drawable checked, BitmapFont font) {
            super(up, down, checked, font);
        }

        public SelectButtonStyle(TextButtonStyle style) {
            super(style);
        }

    }

}
