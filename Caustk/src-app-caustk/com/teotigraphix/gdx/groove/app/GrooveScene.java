
package com.teotigraphix.gdx.groove.app;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.inject.Inject;
import com.teotigraphix.gdx.app.CaustkScene;
import com.teotigraphix.gdx.app.IApplication;
import com.teotigraphix.gdx.app.IApplicationModel;
import com.teotigraphix.gdx.groove.ui.IContainerKind;
import com.teotigraphix.gdx.groove.ui.IContainerMap;
import com.teotigraphix.gdx.groove.ui.IContainerMap.MainTemplateLayout;
import com.teotigraphix.gdx.groove.ui.behavior.MainTemplateBehavior;
import com.teotigraphix.gdx.groove.ui.factory.UIFactory;
import com.teotigraphix.gdx.groove.ui.model.IUIModel;

public class GrooveScene extends CaustkScene {

    //--------------------------------------------------------------------------
    // Inject
    //--------------------------------------------------------------------------

    @Inject
    private UIFactory uiFactory;

    @Inject
    private IUIModel uiModel;

    @Inject
    private IContainerMap containerMap;

    @Inject
    private IApplicationModel applicationModel;

    @Inject
    private MainTemplateBehavior mainTemplateBehavior;

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private MainTemplateLayout[] roots;

    protected IContainerMap getContainerMap() {
        return containerMap;
    }

    protected void setRoots(MainTemplateLayout[] roots) {
        this.roots = roots;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public GrooveScene() {
    }

    //--------------------------------------------------------------------------
    // Overridden Public :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void initialize(IApplication application) {
        super.initialize(application);

        setRoots(MainTemplateLayout.values());

        addComponent(mainTemplateBehavior);

        containerMap.register(this);
        containerMap.setScene(this);

        uiFactory.createDefaults(getSkin());
    }

    @Override
    public void start() {
        super.start();
        uiModel.restore(applicationModel.getProject().getUiState());
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createUI() {
        for (IContainerKind kind : roots) {
            Table table = new Table(getSkin());
            table.setName(kind.getId());
            table.setUserObject(kind);
            containerMap.addActor(kind, table);
        }

        mainTemplateBehavior.create();
    }
}
