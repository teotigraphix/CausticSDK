////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.ui;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.teotigraphix.caustk.workstation.system.SystemState;
import com.teotigraphix.caustk.workstation.system.SystemStateItem;
import com.teotigraphix.libgdx.ui.ControlTable;
import com.teotigraphix.libgdx.ui.Led;
import com.teotigraphix.libgdx.ui.SelectButton;
import com.teotigraphix.libgdx.ui.SelectButton.OnSelectButtonListener;
import com.teotigraphix.libgdx.ui.SelectButton.SelectButtonStyle;

public class MatrixStateSelector extends ControlTable {

    private static final String INC_BUTTON_STYLE_NAME = "matrix-state-selector-inc-button";

    private static final String DEC_BUTTON_STYLE_NAME = "matrix-state-selector-dec-button";

    private SystemState[] states;

    private Button decButton;

    private Button incButton;

    private Array<SelectButton> buttons = new Array<SelectButton>();

    private Array<Led> leds = new Array<Led>();

    private ArrayMap<Integer, Integer> ledIndcies = new ArrayMap<Integer, Integer>();

    //--------------------------------------------------------------------------
    // Property :: API
    //--------------------------------------------------------------------------

    public SystemState getState() {
        return states[selectedColumn];
    }

    public SystemStateItem getStateItem() {
        return states[selectedColumn].getItem(getSelectedRow());
    }

    //----------------------------------
    // selectedColumn
    //----------------------------------

    private int selectedColumn;

    public int getSelectedColumn() {
        return selectedColumn;
    }

    public void setSelectedColumn(int value) {
        setSelectedColumn(value, false);
    }

    public void setSelectedColumn(int value, boolean noEvent) {
        if (value == selectedColumn)
            return;
        selectedColumn = value;
        if (!noEvent)
            fireChange();
        invalidate();
    }

    //----------------------------------
    // selectedRow of column
    //----------------------------------

    public int getSelectedRow() {
        return ledIndcies.get(selectedColumn);
    }

    public void setSelectedRow(int value) {
        setSelectedRow(value, false);
    }

    public void setSelectedRow(int value, boolean noEvent) {
        int old = getSelectedRow();
        if (value == old)
            return;
        ledIndcies.put(selectedColumn, value);
        if (!noEvent)
            fireChange();
        invalidate();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MatrixStateSelector(SystemState[] states, Skin skin) {
        super(skin);
        this.states = states;
        create(skin);
    }

    private void create(Skin skin) {

        skin.add("white", new Color(1, 1, 1, 1));
        skin.add("black", new Color(0, 0, 0, 1));

        //skin.add("default-font", new BitmapFont(Gdx.files.internal("skin/default.fnt"), false));
        //skin.add("eras-12-b", new BitmapFont(Gdx.files.internal("skin/Eras-12-B.fnt"), false));

        ButtonStyle decButtonStyle = new ButtonStyle();
        decButtonStyle.up = skin.getDrawable("button_up");
        decButtonStyle.down = skin.getDrawable("button_selected");
        skin.add(DEC_BUTTON_STYLE_NAME, decButtonStyle);

        ButtonStyle incButtonStyle = new ButtonStyle();
        incButtonStyle.up = skin.getDrawable("button_up");
        incButtonStyle.down = skin.getDrawable("button_selected");
        skin.add(INC_BUTTON_STYLE_NAME, incButtonStyle);

        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = skin.getFont("eras-12-b");
        labelStyle.fontColor = skin.getColor("white");
        skin.add("matrix-state-selector-label", labelStyle);
        // 
        // SelectButton [default]
        SelectButtonStyle selectButtonStyle = new SelectButtonStyle();
        selectButtonStyle.up = skin.getDrawable("button_up");
        selectButtonStyle.down = skin.getDrawable("button_selected");
        selectButtonStyle.checked = skin.getDrawable("button_selected");
        selectButtonStyle.font = skin.getFont("eras-12-b");
        selectButtonStyle.fontColor = skin.getColor("white");
        skin.add("default", selectButtonStyle);
    }

    //--------------------------------------------------------------------------
    // Overridden :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createChildren() {
        createIncDecButtons();
        createLeds();
        createMatrix();

        row().padTop(10f);
        add();
        add();

        createStateButtons();
    }

    @Override
    public void layout() {
        super.layout();

        Iterator<SelectButton> i = buttons.iterator();
        while (i.hasNext()) {
            SelectButton button = i.next();
            button.select(false);
        }

        SelectButton button = buttons.get(selectedColumn);
        if (!button.isSelected()) {
            button.select(true);
        }

        Iterator<Led> ledIterator = leds.iterator();
        while (ledIterator.hasNext()) {
            ledIterator.next().turnOff();
        }

        leds.get(getSelectedRow()).turnOn();
    }

    //--------------------------------------------------------------------------
    // Creation :: Methods
    //--------------------------------------------------------------------------

    private void createLeds() {
        Table table = new Table();

        for (int i = 0; i < 5; i++) {
            Led led = new Led(getSkin());
            led.setStyleName("default");
            table.add(led).center().fillY().expandY();
            table.row();
            leds.add(led);
        }

        add(table).fillY().expandY().padRight(10f);
    }

    private Table createIncDecButtons() {
        Table table = new Table();

        Image decImage = new Image(getSkin().getDrawable("matrix_state_selector_dec"));
        decButton = new Button(decImage, getSkin(), DEC_BUTTON_STYLE_NAME);
        decButton.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                decrement();
            }
        });
        table.add(decButton).size(40f, 27f).top().expand().fill();

        table.row();

        Image incImage = new Image(getSkin().getDrawable("matrix_state_selector_inc"));
        incButton = new Button(incImage, getSkin(), INC_BUTTON_STYLE_NAME);
        incButton.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                increment();
            }
        });

        table.add(incButton).size(40f, 27f).bottom();

        add(table).fillY().expandY().padRight(10f);

        return table;
    }

    private void createMatrix() {
        for (SystemState state : states) {
            Table table = new Table();
            table.align(Align.top);

            List<SystemStateItem> items = state.getItems();
            Iterator<SystemStateItem> i = items.iterator();
            while (i.hasNext()) {
                SystemStateItem item = i.next();
                Label label = new Label(item.getName(), getSkin(), "matrix-state-selector-label");
                table.add(label).left().top().width(75f);
                table.row().top();
            }

            add(table).expandY().fillY().padRight(10f);

            // initialize the led map
            ledIndcies.put(state.getIndex(), 0);
        }
    }

    private void createStateButtons() {
        for (SystemState state : states) {
            SelectButton selectButton = new SelectButton(state.getName(), "default", getSkin());
            selectButton.setIsToggle(true);
            selectButton.setIsGroup(true);
            selectButton.setOnSelectButtonListener(new OnSelectButtonListener() {
                @Override
                public void onChange(SelectButton button) {
                    onSelectionChange(buttons.indexOf(button, true));
                }
            });

            buttons.add(selectButton);
            add(selectButton).left();
        }
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    protected void increment() {
        int index = getSelectedRow() + 1;
        final int len = getState().getItemCount();
        if (index > len - 1)
            index = 0;
        setSelectedRow(index);
    }

    protected void decrement() {
        int index = getSelectedRow() - 1;
        final int len = getState().getItemCount();
        if (index < 0)
            index = len - 1;
        setSelectedRow(index);
    }

    //--------------------------------------------------------------------------
    // Listener
    //--------------------------------------------------------------------------

    private OnMatrixStateSelectorListener onMatrixStateSelectorListener;

    public void setOnMatrixStateSelectorListener(OnMatrixStateSelectorListener l) {
        onMatrixStateSelectorListener = l;
    }

    public interface OnMatrixStateSelectorListener {
        void onChange(SystemState state, SystemStateItem item);
    }

    private void fireChange() {
        SystemState state = states[selectedColumn];
        SystemStateItem item = state.getItem(getSelectedRow());
        onMatrixStateSelectorListener.onChange(state, item);
    }

    protected void onSelectionChange(int index) {
        setSelectedColumn(index);
    }

    //--------------------------------------------------------------------------
    // Model
    //--------------------------------------------------------------------------

}
