
package com.teotigraphix.gdx.groove.ui.behavior;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.app.CaustkBehavior;
import com.teotigraphix.gdx.app.SceneManager;
import com.teotigraphix.gdx.groove.app.GrooveScene;
import com.teotigraphix.gdx.groove.ui.IContainerMap;
import com.teotigraphix.gdx.groove.ui.IContainerMap.MainTemplateLayout;
import com.teotigraphix.gdx.groove.ui.components.TopBar;
import com.teotigraphix.gdx.groove.ui.components.TopBarListener;
import com.teotigraphix.gdx.groove.ui.components.ViewStack;
import com.teotigraphix.gdx.groove.ui.components.ViewStackListener;
import com.teotigraphix.gdx.groove.ui.factory.UIFactory;
import com.teotigraphix.gdx.groove.ui.model.UIModel;
import com.teotigraphix.gdx.groove.ui.model.UIModel.UIModelEvent;

/**
 * Manages the TopBar, BottomBar and ViewPane transitions.
 */
@Singleton
public class MainTemplateBehavior extends CaustkBehavior {

    //--------------------------------------------------------------------------
    // Inject
    //--------------------------------------------------------------------------

    @Inject
    private UIFactory uiFactory;

    @Inject
    private UIModel uiModel;

    @Inject
    private IContainerMap containerMap;

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private TopBar topBar;

    private ViewStack viewStack;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MainTemplateBehavior() {
    }

    //--------------------------------------------------------------------------
    // Overridden Public :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void onAwake() {
        super.onAwake();
        uiModel.getEventBus().register(this);
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    /**
     * @see SceneManager#setScene(int)
     * @see GrooveScene#createUI()
     */
    public void create() {

        Table topBarParent = (Table)containerMap.get(MainTemplateLayout.TopBar);
        Table viewPaneParent = (Table)containerMap.get(MainTemplateLayout.ViewPane);

        topBar = uiFactory.createTopBar(getSkin());
        topBar.addListener(new TopBarListener() {
            @Override
            public void viewIndexChange(TopBarEvent event, int index) {
                getApplication().getLogger().log("MainTemplateBehavior",
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

    //--------------------------------------------------------------------------
    // Event
    //--------------------------------------------------------------------------

    /**
     * @param event
     * @see UIModel#setViewIndex(int)
     * @see UIModel#restore(com.teotigraphix.gdx.groove.ui.model.UIModelState)
     */
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
