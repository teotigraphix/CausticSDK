
package com.teotigraphix.gdx.groove.ui.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.gdx.groove.ui.components.PatternPane.PatternPaneStyle;
import com.teotigraphix.gdx.groove.ui.components.PatternSelectionListener.PatternSelectionEvent;
import com.teotigraphix.gdx.groove.ui.components.PatternSelectionListener.PatternSelectionEventKind;

public class SoundPane extends UITable {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private ButtonGroup gridGroup;

    private boolean updating;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SoundPane(Skin skin) {
        super(skin);
        setStyleClass(PatternPaneStyle.class);
    }

    public void refresh(MachineNode machineNode, boolean isConnected) {
        int index = machineNode.getIndex();
        if (isConnected) {
            setMachineName(index, machineNode.getName());
        } else {
            setMachineName(index, "");
        }
        disable(index, !isConnected);
    }

    public void setMachineName(int index, String name) {
        ((TextButton)gridGroup.getButtons().get(index)).setText(name);
    }

    public void disable(int index, boolean disabled) {
        gridGroup.getButtons().get(index).setDisabled(disabled);
    }

    public void disable(boolean disabled) {
        for (int i = 0; i < 14; i++) {
            disable(i, disabled);
        }
    }

    public void select(int index) {
        gridGroup.getButtons().get(index).setChecked(true);
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createChildren() {
        gridGroup = new ButtonGroup();

        Table topRow = new Table();
        topRow.pad(4f);

        createPatternGrid(topRow);

        add(topRow).expand().fill();

        disable(true);
        disable(14, true);
        disable(15, true);
    }

    private ButtonGroup createPatternGrid(Table parent) {
        PatternPaneStyle style = getStyle();
        parent.pad(4f);
        // XXX until you get a new ToggleButton impl, buttonGroup is trying to check in add()
        updating = true;
        for (int i = 0; i < 16; i++) {
            final int index = i;
            TextButton button = new TextButton("", style.padButtonStyle);
            if (i < 14) {
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (updating)
                            return;
                        PatternSelectionEvent e = new PatternSelectionEvent(
                                PatternSelectionEventKind.patternChange);
                        e.setIndex(index);
                        fire(e);
                    }
                });
            }

            parent.add(button).expand().fill().space(4f);
            gridGroup.add(button);
            if (i % 4 == 3)
                parent.row();
        }
        updating = false;
        return null;
    }

}
