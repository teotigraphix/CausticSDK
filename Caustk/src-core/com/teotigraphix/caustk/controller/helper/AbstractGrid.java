
package com.teotigraphix.caustk.controller.helper;

import com.badlogic.gdx.graphics.Color;

public abstract class AbstractGrid {

    public AbstractGrid() {
    }

    public abstract void lightEx(int x, int y, Color color);

    public abstract void flush();

    public abstract void clear();

}
