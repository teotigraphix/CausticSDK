
package com.teotigraphix.caustk.gs.view.system;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.caustk.gs.ui.UI;
import com.teotigraphix.caustk.gs.ui.UIUtils;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.Dial;
import com.teotigraphix.libgdx.ui.Dial.OnDialListener;

public class SystemControlsMediator extends ScreenMediator {

    public SystemControlsMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        Table table = new Table();
        table.debug();

        table.add().expandX();

        Dial dial = new Dial(screen.getSkin());
        dial.setOnDialListener(new OnDialListener() {
            @Override
            public void onIncrement() {
                getController().getLogger().view("SystemControlsMediator", "Inc");
            }

            @Override
            public void onDialPositionChanged(Dial sender, int nicksChanged) {
            }

            @Override
            public void onDecrement() {
                getController().getLogger().view("SystemControlsMediator", "Dec");
            }
        });

        table.add(dial);

        UIUtils.setBounds(table, UI.boundsSystemControls);
        screen.getStage().addActor(table);
    }
}
