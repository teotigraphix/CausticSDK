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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.gdx.scene2d.ui.ButtonBar.ButtonBarItem;
import com.teotigraphix.caustk.gdx.scene2d.ui.PaneStackListener.PaneStackEvent;
import com.teotigraphix.caustk.gdx.scene2d.ui.PaneStackListener.PaneStackEventKind;

/**
 * The {@link PaneStack} holds a stack of panes and uses a selectedIndex to show
 * the top pane while hiding all other panes.
 * <p>
 * The PaneStack also carries a
 * {@link com.teotigraphix.caustk.gdx.scene2d.ui.ButtonBar} option for quick tab
 * bar like functionality.
 * 
 * @author Michael Schmalle
 */
public class PaneStack extends UITable {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private Table barContainer;

    private Table contentContainer;

    private ButtonBar buttonBar;

    private Stack stack;

    private Array<UITable> panes = new Array<UITable>();

    private int buttonBarAlign;

    private Float maxButtonSize;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // selectedIndex
    //----------------------------------

    private int selectedIndex;

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int value) {
        if (value == selectedIndex)
            return;
        int old = selectedIndex;
        selectedIndex = value;
        PaneStackEvent event = new PaneStackEvent(PaneStackEventKind.SelectedIndexChange,
                selectedIndex);
        if (fire(event)) {
            selectedIndex = old;
        }
        invalidate();
    }

    //----------------------------------
    // maxButtonSize
    //----------------------------------

    public void setMaxButtonSize(Float maxButtonSize) {
        this.maxButtonSize = maxButtonSize;
        invalidateHierarchy();
    }

    public Float getMaxButtonSize() {
        return maxButtonSize;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * @param skin
     * @param buttonBarAlign An
     *            {@link com.badlogic.gdx.scenes.scene2d.utils.Align} value, top
     *            or bottom.
     */
    public PaneStack(Skin skin, String styleName, int buttonBarAlign, float maxButtonSize) {
        super(skin);
        setStyleName(styleName);
        setStyleClass(PaneStackStyle.class);

        this.buttonBarAlign = buttonBarAlign;
        this.maxButtonSize = maxButtonSize;
    }

    public void addPane(UITable actor) {
        panes.add(actor);
    }

    @Override
    public void layout() {
        redrawSelectedIndex();
        super.layout();
    }

    @Override
    protected void createChildren() {
        PaneStackStyle style = getStyle();

        pad(style.padding);

        //debug();
        stack = new Stack();

        barContainer = new Table();
        barContainer.left();
        barContainer.setBackground(style.tabBarBackground);

        //------------------------------
        // Create content
        Array<ButtonBarItem> items = new Array<ButtonBarItem>();
        for (UITable pane : panes) {
            stack.addActor(pane);
            PaneInfo info = (PaneInfo)pane.getUserObject();
            items.add(new ButtonBarItem(info.getId(), info.getName(), info.getIcon(), info
                    .getHelpText()));
            String paneStyleName = "";
            pane.create(paneStyleName);
        }

        panes.clear();

        buttonBar = new ButtonBar(getSkin(), items, false, style.tabStyle);
        buttonBar.setMaxButtonSize(maxButtonSize);
        buttonBar.setGap(style.tabBarGap);
        buttonBar.addListener(new ButtonBarListener() {
            @Override
            public void selectedIndexChange(int selectedIndex) {
                setSelectedIndex(selectedIndex);
            }
        });

        barContainer.add(buttonBar).height(style.tabBarThickness);

        contentContainer = new Table();
        contentContainer.setBackground(style.background);
        // XXX contentContainer.pad(5f);

        if (buttonBarAlign == Align.top) {
            add(barContainer).fillX();
            row();
            add(contentContainer).expand().fill();
            contentContainer.add(stack).expand().fill();
        } else if (buttonBarAlign == Align.bottom) {
            add(contentContainer).expand().fill();
            contentContainer.add(stack).expand().fill();
            row();
            add(barContainer).fillX();
        }

        buttonBar.create("default");
    }

    protected void redrawSelectedIndex() {
        buttonBar.redrawSelection(selectedIndex, true);
        for (Actor actor : stack.getChildren()) {
            actor.setVisible(false);
        }
        stack.getChildren().get(selectedIndex).setVisible(true);
    }

    public static class PaneInfo {

        private String id;

        private String name;

        private String icon;

        private String helpText;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getHelpText() {
            return helpText;
        }

        public void setHelpText(String helpText) {
            this.helpText = helpText;
        }

        public PaneInfo(String id, String name, String icon, String helpText) {
            this.id = id;
            this.name = name;
            this.icon = icon;
            this.helpText = helpText;
        }

    }

    public static class PaneStackStyle {

        public Drawable tabBarBackground;

        public Drawable background;

        public TextButtonStyle tabStyle;

        public float tabBarGap = 2f;

        public float tabBarThickness = 50f;

        public float maxTabWidth = Float.NaN;

        public float padding = 0f;

        public PaneStackStyle() {
        }
    }

}
