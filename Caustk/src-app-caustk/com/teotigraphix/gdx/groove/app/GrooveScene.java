
package com.teotigraphix.gdx.groove.app;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.inject.Inject;
import com.teotigraphix.gdx.app.CaustkScene;
import com.teotigraphix.gdx.app.IApplication;
import com.teotigraphix.gdx.groove.ui.IContainerKind;
import com.teotigraphix.gdx.groove.ui.IContainerMap;
import com.teotigraphix.gdx.groove.ui.IContainerMap.TwoBarViewTrimLayout;

public class GrooveScene extends CaustkScene {

    @Inject
    private IContainerMap containerMap;

    private TwoBarViewTrimLayout[] roots;

    protected void setRoots(TwoBarViewTrimLayout[] roots) {
        this.roots = roots;
    }

    public GrooveScene() {
    }

    @Override
    public void initialize(IApplication application) {
        containerMap.register(this);
        containerMap.setScene(this);
        super.initialize(application);
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
    }

}
