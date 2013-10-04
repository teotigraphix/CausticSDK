
package com.teotigraphix.caustk.controller.core;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IRackComponent;

public class RackComponent implements IRackComponent {

    private static final long serialVersionUID = 3033291731368710036L;

    protected Rack rack;

    @Override
    public ICaustkController getController() {
        return rack.getController();
    }

    public RackComponent() {
    }

    public RackComponent(Rack rack) {
        this.rack = rack;
    }

    protected void commitController() {
    }

    @Override
    public void restore() {
    }

    @Override
    public void registerObservers() {
    }

    @Override
    public void unregisterObservers() {
    }

}
