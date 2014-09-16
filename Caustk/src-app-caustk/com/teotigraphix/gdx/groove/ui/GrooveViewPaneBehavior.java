
package com.teotigraphix.gdx.groove.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.app.CaustkBehavior;
import com.teotigraphix.gdx.groove.ui.IContainerMap.TwoBarViewTrimLayout;
import com.teotigraphix.gdx.groove.ui.components.TopBar;
import com.teotigraphix.gdx.groove.ui.components.TopBarListener;
import com.teotigraphix.gdx.groove.ui.factory.UIFactory;

/**
 * Manages the TopBar, BottomBar and ViewPane transitions.
 */
@Singleton
public class GrooveViewPaneBehavior extends CaustkBehavior {

    @Inject
    private UIFactory factory;

    @Inject
    private IContainerMap containerMap;

    private TopBar topBar;

    public GrooveViewPaneBehavior() {
    }

    public void create() {

        Table parent = (Table)containerMap.get(TwoBarViewTrimLayout.TopBar);

        topBar = factory.createTopBar(getSkin());
        topBar.addListener(new TopBarListener() {
            @Override
            public void viewIndexChange(TopBarEvent event, int index) {
                getApplication().getLogger().log("GrooveViewPaneBehavior",
                        "viewIndexChange() " + index);
            }
        });

        parent.add(topBar).expand().fill();
    }
}
