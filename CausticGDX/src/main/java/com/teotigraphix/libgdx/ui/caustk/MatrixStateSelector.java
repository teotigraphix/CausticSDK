
package com.teotigraphix.libgdx.ui.caustk;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.teotigraphix.libgdx.ui.ControlTable;
import com.teotigraphix.libgdx.ui.Led;
import com.teotigraphix.libgdx.ui.SelectButton;
import com.teotigraphix.libgdx.ui.SelectButton.OnSelectButtonListener;
import com.teotigraphix.libgdx.ui.SelectButton.SelectButtonStyle;

public class MatrixStateSelector extends ControlTable {

    private static final String INC_BUTTON_STYLE_NAME = "matrix-state-selector-inc-button";

    private static final String DEC_BUTTON_STYLE_NAME = "matrix-state-selector-dec-button";

    private MatrixState[] states;

    private Button decButton;

    private Button incButton;

    private Array<SelectButton> buttons = new Array<SelectButton>();

    private Array<Led> leds = new Array<Led>();

    private ArrayMap<Integer, Integer> ledIndcies = new ArrayMap<Integer, Integer>();

    //--------------------------------------------------------------------------
    // Property :: API
    //--------------------------------------------------------------------------

    public MatrixState getState() {
        return states[selectedColumn];
    }

    public MatrixStateItem getStateItem() {
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
        if (value == selectedColumn)
            return;
        selectedColumn = value;
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
        int old = getSelectedRow();
        if (value == old)
            return;
        ledIndcies.put(selectedColumn, value);
        fireChange();
        invalidate();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MatrixStateSelector(MatrixState[] states, Skin skin) {
        super(skin);
        this.states = states;
        create(skin);
    }

    private void create(Skin skin) {

        skin.add("white", new Color(1, 1, 1, 1));
        skin.add("black", new Color(0, 0, 0, 1));

        skin.add("default-font", new BitmapFont(Gdx.files.internal("skin/default.fnt"), false));
        skin.add("eras-12-b", new BitmapFont(Gdx.files.internal("skin/Eras-12-B.fnt"), false));

        ButtonStyle decButtonStyle = new ButtonStyle();
        decButtonStyle.up = skin.getDrawable("pad_up");
        decButtonStyle.down = skin.getDrawable("pad_down");
        skin.add(DEC_BUTTON_STYLE_NAME, decButtonStyle);

        ButtonStyle incButtonStyle = new ButtonStyle();
        incButtonStyle.up = skin.getDrawable("pad_up");
        incButtonStyle.down = skin.getDrawable("pad_down");
        skin.add(INC_BUTTON_STYLE_NAME, incButtonStyle);

        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = skin.getFont("eras-12-b");
        labelStyle.fontColor = skin.getColor("white");
        skin.add("matrix-state-selector-label", labelStyle);
        // 
        // SelectButton [default]
        SelectButtonStyle selectButtonStyle = new SelectButtonStyle();
        selectButtonStyle.up = skin.getDrawable("pad_up");
        selectButtonStyle.down = skin.getDrawable("pad_down");
        selectButtonStyle.checked = skin.getDrawable("pad_down");
        selectButtonStyle.font = skin.getFont("default-font");
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
        table.add(decButton).size(55f, 35f).top().expand().fill();

        table.row();

        Image incImage = new Image(getSkin().getDrawable("matrix_state_selector_inc"));
        incButton = new Button(incImage, getSkin(), INC_BUTTON_STYLE_NAME);
        incButton.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                increment();
            }
        });

        table.add(incButton).size(55f, 35f).bottom();

        add(table).fillY().expandY().padRight(10f);

        return table;
    }

    private void createMatrix() {
        for (MatrixState state : states) {
            Table table = new Table();
            table.align(Align.top);

            Array<MatrixStateItem> items = state.items;
            Iterator<MatrixStateItem> i = items.iterator();
            while (i.hasNext()) {
                MatrixStateItem item = i.next();
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
        for (MatrixState state : states) {
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
        void onChange(MatrixState state, MatrixStateItem item);
    }

    private void fireChange() {
        MatrixState state = states[selectedColumn];
        MatrixStateItem item = state.getItem(getSelectedRow());
        onMatrixStateSelectorListener.onChange(state, item);
    }

    protected void onSelectionChange(int index) {
        setSelectedColumn(index);
    }

    //--------------------------------------------------------------------------
    // Model
    //--------------------------------------------------------------------------

    public static class MatrixState {

        private Array<MatrixStateItem> items = new Array<MatrixStateItem>();

        private String name;

        private int index;

        public int getIndex() {
            return index;
        }

        public int getItemCount() {
            return items.size;
        }

        public String getName() {
            return name;
        }

        public MatrixState(int index, String name) {
            this.index = index;
            this.name = name;
        }

        public MatrixStateItem getItem(int index) {
            return items.get(index);
        }

        public void addItem(String name) {
            int index = items.size;
            MatrixStateItem item = new MatrixStateItem(index, name);
            items.add(item);
        }

        @Override
        public String toString() {
            return "[MatrixState|" + name + "]";
        }
    }

    public static class MatrixStateItem {

        private String name;

        private int index;

        public int getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }

        public MatrixStateItem(int index, String name) {
            this.index = index;
            this.name = name;
        }

        @Override
        public String toString() {
            return "[MatrixStateItem|" + name + "]";
        }
    }
}
