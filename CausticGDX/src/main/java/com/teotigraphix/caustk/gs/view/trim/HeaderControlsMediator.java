
package com.teotigraphix.caustk.gs.view.trim;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.caustk.gs.ui.UI;
import com.teotigraphix.caustk.gs.ui.UIUtils;
import com.teotigraphix.libgdx.controller.CaustkMediator;
import com.teotigraphix.libgdx.screen.IScreen;

public class HeaderControlsMediator extends CaustkMediator {

    public HeaderControlsMediator() {
    }

    @Override
    public void create(IScreen screen) {
        Table table = new Table();
        table.debug();

        UIUtils.setBounds(table, UI.boundsHeaderControls);
        screen.getStage().addActor(table);
    }
}
