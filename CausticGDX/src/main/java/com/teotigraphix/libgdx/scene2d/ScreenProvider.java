
package com.teotigraphix.libgdx.scene2d;

import com.google.inject.Singleton;
import com.teotigraphix.libgdx.screen.IScreen;

@Singleton
public class ScreenProvider implements IScreenProvider {

    private IScreen screen;

    @Override
    public IScreen getScreen() {
        return screen;
    }

    @Override
    public void setScreen(IScreen value) {
        screen = value;
    }

    public ScreenProvider() {
    }

}
