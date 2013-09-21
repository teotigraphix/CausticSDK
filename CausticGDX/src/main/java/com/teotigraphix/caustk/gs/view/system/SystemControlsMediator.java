
package com.teotigraphix.caustk.gs.view.system;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.caustk.gs.ui.UI;
import com.teotigraphix.caustk.gs.ui.UIUtils;
import com.teotigraphix.libgdx.controller.CaustkMediator;
import com.teotigraphix.libgdx.screen.IScreen;

public class SystemControlsMediator extends CaustkMediator {

    public SystemControlsMediator() {
    }

    @Override
    public void create(IScreen screen) {
        Table table = new Table();
        table.debug();

        UIUtils.setBounds(table, UI.boundsSystemControls);
        screen.getStage().addActor(table);
    }
}