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

package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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

        content = new Table();

        background = new Image(getStyle().background);

        stack(background, content).expand().fill();

        // Image | Label | Label | Image(expand arrow)

        MenuItem menuItem = getUserObject();

        if (!menuItem.isSeparator()) {
            Image image = new Image();
            image.setScaling(Scaling.none);
            String icon = menuItem.getIcon();
            if (icon != null && !icon.equals("")) {
                image.setDrawable(getSkin().getDrawable(icon));
            }
            content.add(image).minWidth(35f);

            LabelStyle labelStyle = getLabelStyle();
            menuLabel = new Label(getText(), labelStyle);
            menuLabel.setTouchable(Touchable.disabled);
            content.add(menuLabel).left().minWidth(125f);

            bindingLabel = new Label(menuItem.getKeyBinding(), labelStyle);
            bindingLabel.setAlignment(Align.right);
            bindingLabel.setTouchable(Touchable.disabled);
            content.add(bindingLabel).right().minWidth(50f).fillX().expandX();

            Image arrow = new Image();
            content.add(arrow).minWidth(15f);
            validate();
        } else {
            content.add().minWidth(35f);
            Image separator = new Image(getStyle().separator);
            setTouchable(Touchable.disabled);
            content.add(separator).fillX().expandX();
            content.add();
            content.add();
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
        } else if (isOver()) {
            labelStyle.fontColor = getStyle().fontOverColor;
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
