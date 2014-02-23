
package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.teotigraphix.gdx.scene2d.ui.MenuBar.MenuItem;

public class MenuRowRenderer extends ListRowRenderer {

    private Label menuLabel;

    private Label bindingLabel;

    @Override
    public MenuRowRendererStyle getStyle() {
        return (MenuRowRendererStyle)super.getStyle();
    }

    @Override
    public MenuItem getUserObject() {
        return (MenuItem)super.getUserObject();
    }

    public MenuRowRenderer(Skin skin) {
        super(skin, null);
    }

    // skin.get(styleName, ListRowRendererStyle.class)
    @Override
    public void createChildren() {

        // Image | Label | Label | Image(expand arrow)

        MenuItem menuItem = getUserObject();

        if (!menuItem.isSeparator()) {
            Image image = new Image();
            image.setScaling(Scaling.none);
            String icon = menuItem.getIcon();
            if (icon != null && !icon.equals("")) {
                image.setDrawable(getSkin().getDrawable(icon));
            }
            add(image).minWidth(35f);

            LabelStyle labelStyle = getLabelStyle();
            menuLabel = new Label(getText(), labelStyle);
            menuLabel.setTouchable(Touchable.disabled);
            add(menuLabel).left().minWidth(125f);

            bindingLabel = new Label(menuItem.getKeyBinding(), labelStyle);
            bindingLabel.setAlignment(Align.right);
            bindingLabel.setTouchable(Touchable.disabled);
            add(bindingLabel).right().minWidth(50f).fillX().expandX();

            Image arrow = new Image();
            add(arrow).minWidth(15f);
            validate();
        } else {
            add().minWidth(35f);
            Image separator = new Image(getStyle().separator);
            setTouchable(Touchable.disabled);
            add(separator).fillX().expandX();
            add();
            add();
        }
    }

    @Override
    public void layout() {
        super.layout();

        if (menuLabel != null)
            menuLabel.setStyle(getLabelStyle());
        if (bindingLabel != null)
            bindingLabel.setStyle(getLabelStyle());
    }

    private LabelStyle getLabelStyle() {
        LabelStyle labelStyle = new LabelStyle(getStyle().font, getStyle().fontColor);
        if (isSelected()) {
            labelStyle.fontColor = getStyle().fontSelectedColor;
        }
        return labelStyle;
    }

    @Override
    public float getPrefHeight() {
        return 22f;
    }

    public static class MenuRowRendererStyle extends ListRowRendererStyle {

        public Drawable separator;

        public MenuRowRendererStyle() {
        }

    }
}
