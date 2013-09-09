
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class SelectButton extends ControlTable {

    private Button button;

    private String text;

    private Label label;

    private boolean isToggle;

    public String getText() {
        return text;
    }

    public SelectButton(String text, String styleName, Skin skin) {
        super(skin);
        this.text = text;
        setStyleName(styleName);
        styleClass = SelectButtonStyle.class;
    }

    @Override
    protected void createChildren() {
        SelectButtonStyle style = getStyle();
        button = new Button(style);
        button.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!isToggle)
                    event.cancel();
            }
        });
        add(button).size(style.width, style.height);
        label = new Label(text, new LabelStyle(style.font, style.fontColor));
        row();
        add(label);
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
