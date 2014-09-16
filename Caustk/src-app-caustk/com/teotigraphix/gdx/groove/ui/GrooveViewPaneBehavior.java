
package com.teotigraphix.gdx.groove.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.app.CaustkBehavior;
import com.teotigraphix.gdx.groove.ui.IContainerMap.TwoBarViewTrimLayout;
import com.teotigraphix.gdx.groove.ui.UIModel.UIModelEvent;
import com.teotigraphix.gdx.groove.ui.components.TopBar;
import com.teotigraphix.gdx.groove.ui.components.TopBarListener;
import com.teotigraphix.gdx.groove.ui.components.ViewStack;
import com.teotigraphix.gdx.groove.ui.components.ViewStackListener;
import com.teotigraphix.gdx.groove.ui.factory.UIFactory;

/**
 * Manages the TopBar, BottomBar and ViewPane transitions.
 */
@Singleton
public class GrooveViewPaneBehavior extends CaustkBehavior {

    @Inject
    private UIFactory uiFactory;

    @Inject
    private UIModel uiModel;

    @Inject
    private IContainerMap containerMap;

    private TopBar topBar;

    private ViewStack viewStack;

    public GrooveViewPaneBehavior() {
    }

    @Override
    public void onAwake() {
        super.onAwake();
        uiModel.getEventBus().register(this);
    }

    public void create() {

        Table topBarParent = (Table)containerMap.get(TwoBarViewTrimLayout.TopBar);
        Table viewPaneParent = (Table)containerMap.get(TwoBarViewTrimLayout.ViewPane);

        topBar = uiFactory.createTopBar(getSkin());
        topBar.addListener(new TopBarListener() {
            @Override
            public void viewIndexChange(TopBarEvent event, int index) {
                getApplication().getLogger().log("GrooveViewPaneBehavior",
                        "viewIndexChange() " + index);
                uiModel.setViewIndex(index);
            }
        });

        topBarParent.add(topBar).expand().fill();

        //------------------------------

        viewStack = uiFactory.createViewStack(getSkin());
        viewStack.addListener(new ViewStackListener() {
            @Override
            public void selectedIndexChange(ViewStackEvent event, int index, int oldIndex) {
            }
        });

        viewPaneParent.add(viewStack).expand().fill();
    }

    @Subscribe
    public void onUIModelEventHandler(UIModelEvent event) {
        switch (event.getKind()) {
            case ViewIndexChange:
                int viewIndex = event.getModel().getViewIndex();
                viewStack.setSelectedIndex(viewIndex);
                topBar.getButtonBar().setSelectedIndex(viewIndex);
                break;
        }
    }
}
