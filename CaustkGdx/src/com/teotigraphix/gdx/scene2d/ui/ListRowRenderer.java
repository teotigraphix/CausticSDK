
package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;
import com.teotigraphix.gdx.scene2d.ui.AdvancedList.AdvancedListEvent;
import com.teotigraphix.gdx.scene2d.ui.AdvancedList.AdvancedListEventKind;

public abstract class ListRowRenderer extends Table {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private boolean selected;

    private ListRowRendererStyle style;

    private Label label;

    private String text = "";

    private Skin skin;

    protected Image background;

    protected Table content;

    //--------------------------------------------------------------------------
    // Public Property :: API
    //--------------------------------------------------------------------------

    //----------------------------------
    // skin
    //----------------------------------

    public Skin getSkin() {
        return skin;
    }

    //----------------------------------
    // style
    //----------------------------------

    public ListRowRendererStyle getStyle() {
        return style;
    }

    public void setStyle(ListRowRendererStyle style) {
        this.style = style;

        //        if (style != null)
        //            setBackground(style.background);
    }

    //----------------------------------
    // text
    //----------------------------------

    public String getText() {
        return text;
    }

    public void setText(String value) {
        text = value;
        invalidate();
    }

    //----------------------------------
    // selected
    //----------------------------------

    public void setSelected(boolean selected) {
        if (selected == this.selected)
            return;

        this.selected = selected;

        if (style == null)
            return;

        if (background != null && style != null) {
            if (selected)
                background.setDrawable(style.selection);
            else
                background.setDrawable(style.background);
        }
    }

    public boolean isSelected() {
        return selected;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

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
                    AdvancedListEvent e = Pools.obtain(AdvancedListEvent.class);
                    e.setKind(AdvancedListEventKind.DoubleTap);
                    fire(e);
                    Pools.free(e);
                }
            }

            @Override
            public boolean longPress(Actor actor, float x, float y) {
                AdvancedListEvent e = Pools.obtain(AdvancedListEvent.class);
                e.setKind(AdvancedListEventKind.LongPress);
                fire(e);
                Pools.free(e);
                return true;
            }
        });
    }

    public void createChildren() {
        content = new Table();

        background = new Image(style.background);

        LabelStyle labelStyle = new LabelStyle(style.font, style.fontColor);
        label = new Label(text, labelStyle);
        label.setAlignment(Align.left);
        content.add(label).expand().fill().pad(4f);

        stack(background, content).expand().fill();
    }

    @Override
    public void layout() {
        super.layout();
        if (label != null)
            label.setText(text);
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
