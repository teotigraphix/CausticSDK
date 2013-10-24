
package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.AdvancedList.AdvancedListChangeEvent;
import com.badlogic.gdx.scenes.scene2d.ui.AdvancedList.AdvancedListDoubleTapEvent;
import com.badlogic.gdx.scenes.scene2d.ui.AdvancedList.AdvancedListLongPressEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;

public abstract class ListRowRenderer extends Table {

    private boolean isSelected;

    private ListRowRendererStyle style;

    private Label label;

    private String text = "";

    private Skin skin;

    public String getText() {
        return text;
    }

    public void setText(String value) {
        text = value;
        invalidate();
    }

    public ListRowRenderer(Skin skin) {
        this(skin, "default");
    }

    public ListRowRenderer(Skin skin, String styleName) {
        super(skin);
        this.skin = skin;
        align(Align.left);
        setTouchable(Touchable.enabled);
        setStyle(skin.get(styleName, ListRowRendererStyle.class));
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
        label = new Label(text, skin);
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
            setBackground(style.selection);
        else
            setBackground(style.background);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public static class ListRowRendererStyle {

        public Drawable background;

        public Drawable selection;

        public ListRowRendererStyle() {

        }
    }
}
