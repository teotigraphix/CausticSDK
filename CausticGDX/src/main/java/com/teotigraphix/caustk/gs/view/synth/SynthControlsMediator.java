
package com.teotigraphix.caustk.gs.view.synth;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.caustk.gs.ui.UI;
import com.teotigraphix.caustk.gs.ui.UIUtils;
import com.teotigraphix.libgdx.controller.CaustkMediator;
import com.teotigraphix.libgdx.screen.IScreen;

public abstract class SynthControlsMediator extends CaustkMediator {

    private Skin skin;

    private Table table;

    public Table getTable() {
        return table;
    }

    public Skin getSkin() {
        return skin;
    }

    public SynthControlsMediator() {
    }

    @Override
    public void create(IScreen screen) {
        skin = screen.getSkin();

        table = new Table();
        table.debug();

        createTable(table);

        UIUtils.setBounds(table, UI.boundsSynthControls);
        screen.getStage().addActor(table);
    }

    protected abstract void createTable(Table table);

}
