
package com.teotigraphix.caustk.sound;

import com.teotigraphix.caustic.core.Dispatcher;
import com.teotigraphix.caustic.core.ICausticEngine;
import com.teotigraphix.caustic.core.IDispatcher;
import com.teotigraphix.caustk.controller.ICaustkController;

public class CaustkSoundSource implements ICaustkSoundSource {
    
    //----------------------------------
    // dispatcher
    //----------------------------------

    private final IDispatcher dispatcher;

    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    //----------------------------------
    // controller
    //----------------------------------

    private ICaustkController controller;

    protected ICaustkController getController() {
        return controller;
    }
    
    protected ICausticEngine getEngine() {
        return controller.getSoundGenerator();
    }


    public CaustkSoundSource(ICaustkController controller) {
        this.controller = controller;

        dispatcher = new Dispatcher();
    }

}
