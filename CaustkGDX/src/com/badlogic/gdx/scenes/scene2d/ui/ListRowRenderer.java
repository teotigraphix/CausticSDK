
package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

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
        this.isSelected = isSelected;

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
