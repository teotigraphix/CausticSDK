
package com.teotigraphix.caustk.gs.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public final class UIUtils {

    public static void setBounds(Actor actor, Rectangle bounds) {
        actor.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}
