
package com.teotigraphix.gdx.groove.ui.behavior;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.common.eventbus.Subscribe;
import com.teotigraphix.gdx.app.CaustkBehavior;
import com.teotigraphix.gdx.app.IProjectModel.ProjectModelEvent;
import com.teotigraphix.gdx.app.SceneManager;
import com.teotigraphix.gdx.groove.ui.IContainerMap.TopBarViewStackLayout;
import com.teotigraphix.gdx.groove.ui.components.TopBar;
import com.teotigraphix.gdx.groove.ui.components.TopBarListener;
import com.teotigraphix.gdx.groove.ui.components.ViewStack;
import com.teotigraphix.gdx.groove.ui.components.ViewStackListener;

/**
 * Manages the TopBar, BottomBar and ViewPane transitions.
 */
public class TopBarViewStackBehavior extends CaustkBehavior {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private TopBar topBar;

    private ViewStack viewStack;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public TopBarViewStackBehavior() {
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    /**
     * @see SceneManager#setScene(int)
     * @see GrooveScene#createUI()
     */
    public void create() {

        Table topBarParent = (Table)getContainerMap().get(TopBarViewStackLayout.TopBar);
        Table viewPaneParent = (Table)getContainerMap().get(TopBarViewStackLayout.ViewPane);

        topBar = getFactory().createTopBar(getSkin());
        topBar.addListener(new TopBarListener() {
            @Override
            public void viewIndexChange(TopBarEvent event, int index) {
                getApplication().getLogger().log("MainTemplateBehavior",
                        "viewIndexChange() " + index);
                getProjectModel().setSceneViewIndex(index);
            }
        });

        topBarParent.add(topBar).expand().fill();

        //------------------------------

        viewStack = getFactory().createViewStack(getSkin());
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

    @Subscribe
    public void onProjectModelEvent(ProjectModelEvent event) {
        switch (event.getKind()) {
            case SceneViewChange:
                int sceneIndex = event.getModel().getSceneViewIndex();
                viewStack.setSelectedIndex(sceneIndex);
                topBar.getButtonBar().setSelectedIndex(sceneIndex);
                break;
            case ViewChange:
                break;
            case MachineSelectionChange:
                break;
        }
    }
}
