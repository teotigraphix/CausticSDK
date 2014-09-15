
package com.teotigraphix.gdx.groove.ui;

import java.util.HashMap;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.google.common.eventbus.EventBus;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.app.CaustkScene;

@Singleton
public class ContainerMap implements IContainerMap {

    private HashMap<IContainerKind, WidgetGroup> map = new HashMap<IContainerKind, WidgetGroup>();

    private EventBus eventBus;

    private CaustkScene scene;

    @Override
    public void setScene(CaustkScene scene) {
        this.scene = scene;
    }

    public ContainerMap() {
        eventBus = new EventBus();
    }

    void put(IContainerKind key, WidgetGroup group) {
        map.put(key, group);
    }

    WidgetGroup get(IContainerKind key) {
        return map.get(key);
    }

    @Override
    public void register(Object object) {
        eventBus.register(object);
    }

    @Override
    public void addActor(IContainerKind kind, WidgetGroup actor) {
        Rectangle bounds = kind.getBounds();
        scene.getRoot().addActor(actor);
        actor.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
        put(TwoBarViewTrimLayout.TopBar, actor);
    }

}
