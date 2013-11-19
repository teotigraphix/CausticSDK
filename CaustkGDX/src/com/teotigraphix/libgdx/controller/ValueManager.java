
package com.teotigraphix.libgdx.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
/**
 * @author Michael Schmalle
 */
public class ValueManager extends CaustkMediator implements IValueManager {

    @Inject
    IMessageManager messageManager;

    public ValueManager() {
    }

    @Override
    public void register(final IValueAware valueAware) {
        // down, change, up
        Actor actor = valueAware.getActor();
        actor.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                messageManager.setStatus(valueAware.getValue() + "");
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                messageManager.setStatus("");
            }

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (count == 2) {
                    valueAware.resetValue();
                }
            }
        });
        actor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                messageManager.setStatus(valueAware.getValue() + "");
            }
        });
    }

}
