
package com.teotigraphix.caustk.gs.view.system;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.gs.ui.UI;
import com.teotigraphix.caustk.gs.ui.UIUtils;
import com.teotigraphix.libgdx.controller.CaustkMediator;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.Dial;
import com.teotigraphix.libgdx.ui.Dial.OnDialListener;

public class SystemControlsMediator extends CaustkMediator {

    public SystemControlsMediator() {
    }

    @Override
    public void create(IScreen screen) {
        Table table = new Table();
        table.debug();

        table.add().expandX();

        Dial dial = new Dial(screen.getSkin());
        dial.setOnDialListener(new OnDialListener() {
            @Override
            public void onIncrement() {
                CtkDebug.log("Inc");
            }

            @Override
            public void onDialPositionChanged(Dial sender, int nicksChanged) {
            }

            @Override
            public void onDecrement() {
                CtkDebug.log("Dec");
            }
        });

        table.add(dial);

        UIUtils.setBounds(table, UI.boundsSystemControls);
        screen.getStage().addActor(table);
    }
}
