
package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;
import com.teotigraphix.gdx.scene2d.ui.AdvancedList.AdvancedListChangeEvent;
import com.teotigraphix.gdx.scene2d.ui.AdvancedList.AdvancedListDoubleTapEvent;
import com.teotigraphix.gdx.scene2d.ui.AdvancedList.AdvancedListLongPressEvent;

public abstract class ListRowRenderer extends Table {

    private boolean isSelected;

    private ListRowRendererStyle style;

    private Label label;

    private String text = "";

    private Skin skin;

    public Skin getSkin() {
        return skin;
    }

    public ListRowRendererStyle getStyle() {
        return style;
    }

    public String getText() {
        return text;
    }

    public void setText(String value) {
        text = value;
        invalidate();
    }

    public ListRowRenderer(Skin skin) {
        super(skin);
        this.skin = skin;
    }

    public ListRowRenderer(Skin skin, ListRowRendererStyle style) {
        super(skin);
        this.skin = skin;
        align(Align.left);
        setTouchable(Touchable.enabled);
        setStyle(style);
        addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (count == 2) {
                    AdvancedListDoubleTapEvent e = Pools.obtain(AdvancedListDoubleTapEvent.class);
                    fire(e);
                    Pools.free(e);
                }
            }

            @Override
            public boolean longPress(Actor actor, float x, float y) {
                AdvancedListLongPressEvent e = Pools.obtain(AdvancedListLongPressEvent.class);
                fire(e);
                Pools.free(e);
                return true;
            }
        });
    }

    public void createChildren() {
        LabelStyle labelStyle = new LabelStyle(style.font, style.fontColor);
        label = new Label(text, labelStyle);
        add(label).left();
    }

    @Override
    public void layout() {
        super.layout();
        if (label != null)
            label.setText(text);
    }

    public void setStyle(ListRowRendererStyle style) {
        this.style = style;

        if (style != null)
            setBackground(style.background);
    }

    public void setIsSelected(boolean isSelected) {
        if (isSelected == this.isSelected)
            return;

        this.isSelected = isSelected;

        if (isSelected) {
            AdvancedListChangeEvent e = Pools.obtain(AdvancedListChangeEvent.class);
            fire(e);
            Pools.free(e);
        }

        if (style == null)
            return;

        if (isSelected)
            setBackground(style.selection, false);
        else
            setBackground(style.background, false);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public static class ListRowRendererStyle {

        public Drawable background;

        public Drawable selection;

        public BitmapFont font;

        public Color fontColor;

        public Color fontSelectedColor;

        public ListRowRendererStyle() {
        }
    }
}
