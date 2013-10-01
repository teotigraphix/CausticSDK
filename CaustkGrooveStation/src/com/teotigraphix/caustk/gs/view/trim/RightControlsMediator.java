
package com.teotigraphix.caustk.gs.view.trim;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.caustk.gs.ui.UI;
import com.teotigraphix.caustk.gs.ui.UIUtils;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.screen.IScreen;

public class RightControlsMediator extends ScreenMediator {

    public RightControlsMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        Table table = new Table();
        table.debug();

        UIUtils.setBounds(table, UI.boundsRightControls);
        screen.getStage().addActor(table);
    }
}
