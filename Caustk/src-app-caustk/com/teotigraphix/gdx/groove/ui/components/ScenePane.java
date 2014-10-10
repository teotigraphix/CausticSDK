
package com.teotigraphix.gdx.groove.ui.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.groove.session.Scene;
import com.teotigraphix.gdx.groove.ui.components.PatternPane.PatternPaneStyle;
import com.teotigraphix.gdx.groove.ui.components.SceneSelectionListener.SceneSelectionEvent;
import com.teotigraphix.gdx.groove.ui.components.SceneSelectionListener.SceneSelectionEventKind;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;
import com.teotigraphix.gdx.scene2d.ui.ButtonBarListener;

public class ScenePane extends UITable {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private ButtonBar bankBar;

    private ButtonGroup gridGroup;

    private boolean updating;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ScenePane(Skin skin) {
        super(skin);
        setStyleClass(PatternPaneStyle.class);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createChildren() {
        gridGroup = new ButtonGroup();

        Table topRow = new Table();
        topRow.pad(4f);
        Table bottomRow = new Table();
        bottomRow.pad(4f);

        bankBar = createBankBar();
        topRow.add(bankBar).expand().fill();

        createPatternGrid(bottomRow);

        add(topRow).height(40f).expandX().fillX();
        row();
        add(bottomRow).expand().fill();

        disable(true);
    }

    public void refresh(Scene scene) {
        disable(scene.getMatrixIndex(), !scene.hasClips());
        if (scene.hasClips()) {
            TextButton button = (TextButton)gridGroup.getButtons().get(scene.getMatrixIndex());
            button.setText(scene.getName());
        }
    }

    public void disable(int matrixIndex, boolean isDisabled) {
        gridGroup.getButtons().get(matrixIndex).setDisabled(isDisabled);
    }

    public void disable(boolean disabled) {
        bankBar.setDisabled(disabled);
        for (Button button : gridGroup.getButtons()) {
            button.setDisabled(disabled);
        }
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    private ButtonBar createBankBar() {
        PatternPaneStyle style = getStyle();
        Array<ButtonBarItem> banks = new Array<ButtonBarItem>();
        banks.add(new ButtonBarItem("a", "A", "", ""));
        banks.add(new ButtonBarItem("b", "B", "", ""));
        banks.add(new ButtonBarItem("c", "C", "", ""));
        banks.add(new ButtonBarItem("d", "D", "", ""));
        ButtonBar buttonBar = new ButtonBar(getSkin(), banks, false, style.bankButtonStyle);
        buttonBar.setGap(4f);
        buttonBar.addListener(new ButtonBarListener() {
            @Override
            public void selectedIndexChange(int selectedIndex) {
                SceneSelectionEvent e = new SceneSelectionEvent(SceneSelectionEventKind.bankChange);
                e.setIndex(selectedIndex);
                fire(e);
            }
        });

        buttonBar.create("default");
        return buttonBar;
    }

    private ButtonGroup createPatternGrid(Table parent) {
        PatternPaneStyle style = getStyle();
        parent.pad(4f);
        // XXX until you get a new ToggleButton impl, buttonGroup is trying to check in add()
        updating = true;
        for (int i = 0; i < 16; i++) {
            final int index = i;
            TextButton button = new TextButton("", style.padButtonStyle);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (updating)
                        return;
                    SceneSelectionEvent e = new SceneSelectionEvent(
                            SceneSelectionEventKind.matrixChange);
                    e.setIndex(index);
                    fire(e);
                }
            });
            parent.add(button).expand().fill().space(4f);
            gridGroup.add(button);
            if (i % 4 == 3)
                parent.row();
        }
        updating = false;
        return null;
    }

    public TextButton getScenePad(int index) {
        return (TextButton)gridGroup.getButtons().get(index);
    }

}
