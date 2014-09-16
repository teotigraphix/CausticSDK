
package com.teotigraphix.gdx.groove.app;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.inject.Inject;
import com.teotigraphix.gdx.app.CaustkScene;
import com.teotigraphix.gdx.app.IApplication;
import com.teotigraphix.gdx.groove.ui.GrooveViewPaneBehavior;
import com.teotigraphix.gdx.groove.ui.IContainerKind;
import com.teotigraphix.gdx.groove.ui.IContainerMap;
import com.teotigraphix.gdx.groove.ui.IContainerMap.TwoBarViewTrimLayout;
import com.teotigraphix.gdx.groove.ui.factory.UIFactory;

public class GrooveScene extends CaustkScene {

    @Inject
    private UIFactory factory;

    @Inject
    private IContainerMap containerMap;

    @Inject
    private GrooveViewPaneBehavior grooveViewPaneBehavior;

    private TwoBarViewTrimLayout[] roots;

    protected IContainerMap getContainerMap() {
        return containerMap;
    }

    protected void setRoots(TwoBarViewTrimLayout[] roots) {
        this.roots = roots;
    }

    public GrooveScene() {
    }

    @Override
    public void initialize(IApplication application) {
        super.initialize(application);

        setRoots(TwoBarViewTrimLayout.values());

        addComponent(grooveViewPaneBehavior);

        containerMap.register(this);
        containerMap.setScene(this);

        factory.createFonts(getSkin());
    }

    @Override
    protected void createUI() {
        for (IContainerKind kind : roots) {
            Table table = new Table(getSkin());
            table.debug();
            table.setName(kind.getId());
            table.setUserObject(kind);
            containerMap.addActor(kind, table);
        }

        createTopBar();
    }

    protected void createTopBar() {
        grooveViewPaneBehavior.create();
    }

}
