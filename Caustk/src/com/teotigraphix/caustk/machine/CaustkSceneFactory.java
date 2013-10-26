
package com.teotigraphix.caustk.machine;

import java.io.File;
import java.util.UUID;

import com.teotigraphix.caustk.rack.IRack;

public class CaustkSceneFactory {

    private IRack rack;

    public CaustkSceneFactory(IRack rack) {
        this.rack = rack;
    }

    public CaustkScene createScene(String name) {
        CaustkScene caustkScene = new CaustkScene(UUID.randomUUID(), name);
        return caustkScene;
    }

    public CaustkScene createScene(File absoluteCausticFile) {
        CaustkScene caustkScene = new CaustkScene(UUID.randomUUID(), absoluteCausticFile);
        return caustkScene;
    }

}
