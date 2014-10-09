
package com.teotigraphix.gdx.groove.ui.behavior;

import com.google.common.eventbus.Subscribe;
import com.teotigraphix.caustk.node.RackNode.RackNodeSelectionEvent;
import com.teotigraphix.gdx.app.CaustkBehavior;
import com.teotigraphix.gdx.groove.ui.components.SoundPane;
import com.teotigraphix.gdx.groove.ui.components.SoundSelectionListener;
import com.teotigraphix.gdx.groove.ui.factory.StylesDefault;

public abstract class SoundPaneBehavior extends CaustkBehavior {

    private SoundPane view;

    public SoundPaneBehavior() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onAwake() {
        super.onAwake();
        getRack().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getRack().unregister(this);
    }

    public SoundPane create() {
        view = new SoundPane(getSkin());
        view.create(StylesDefault.SoundPane);
        view.addListener(new SoundSelectionListener() {
            @Override
            public void selectedIndexChange(SoundSelectionEvent event, int index) {
                getRackNode().setSelectedIndex(index);
            }
        });
        return view;
    }

    @Subscribe
    public void onRackNodeSelectionEvent(RackNodeSelectionEvent event) {
        view.select(event.getMachineNode().getIndex());
    }
}
