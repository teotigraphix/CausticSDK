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

package com.teotigraphix.caustk.gdx.scene2d.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.gdx.scene2d.ui.MenuBar.MenuBarStyle;
import com.teotigraphix.caustk.gdx.scene2d.ui.MenuBar.MenuItem;
import com.teotigraphix.caustk.gdx.scene2d.ui.MenuRowRenderer.MenuRowRendererStyle;

public class Menu extends Dialog {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private Array<MenuItem> menuItems;

    private MenuRowRendererStyle menuRowRendererStyle;

    private Skin skin;

    private AdvancedList<MenuRowRenderer> list;

    private Tooltip tooltip;

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public Menu(Array<MenuItem> menuItems, MenuBarStyle menuBarStyle, Skin skin) {
        super("", menuBarStyle.windowStyle);
        this.menuItems = menuItems;
        this.menuRowRendererStyle = menuBarStyle.rowRendererStyle;
        this.skin = skin;

        setModal(false);
        initialize();
    }

    public Menu(Array<MenuItem> menuItems, WindowStyle windowStyle,
            MenuRowRendererStyle menuRowRendererStyle, Skin skin) {
        super("", windowStyle);
        this.menuItems = menuItems;
        this.menuRowRendererStyle = menuRowRendererStyle;
        this.skin = skin;
        setModal(false);
        initialize();
    }

    private void initialize() {
        list = new AdvancedList<MenuRowRenderer>(menuItems.toArray(), MenuRowRenderer.class, skin,
                menuRowRendererStyle);
        list.setItems(menuItems.toArray());
        getContentTable().add(list);

        list.addListener(new AdvancedListListener() {
            @Override
            public void overChanged(AdvancedListEvent event, Actor actor) {
                int overIndex = event.getOverIndex();
                if (overIndex == -1) {
                    stopTooltip();
                } else {
                    if (tooltip != null)
                        stopTooltip();
                    startTooltip(overIndex);
                }
            }
        });
    }

    protected void stopTooltip() {
        clearActions();
        if (tooltip == null)
            return;
        tooltip.hide();
        tooltip = null;
    }

    protected void startTooltip(int selectedIndex) {
        addAction(Actions.sequence(Actions.delay(1f), Actions.run(new Runnable() {
            @Override
            public void run() {
                showTooltip();
            }
        })));
    }

    protected void showTooltip() {
        if (tooltip != null)
            return;
        if (list.getOverIndex() == -1)
            return;

        MenuItem menuItem = menuItems.get(list.getOverIndex());

        String helpText = menuItem.getHelpText();
        if (helpText != null && !helpText.equals("")) {
            tooltip = Tooltip.show(getStage(), skin, helpText);
            tooltip.setPosition(Gdx.input.getX() + 20f, getStage().getHeight() - Gdx.input.getY()
                    - tooltip.getHeight() - 20f);
        }
    }
}
