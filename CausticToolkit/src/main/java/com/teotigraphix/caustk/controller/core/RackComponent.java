
package com.teotigraphix.caustk.controller.core;

import com.teotigraphix.caustk.controller.ICaustkController;

public abstract class RackComponent implements IRackComponent {

    private static final long serialVersionUID = 3033291731368710036L;

    private Rack rack;

    @Override
    public ICaustkController getController() {
        return rack.getController();
    }

    @Override
    public void setRack(Rack rack) {
        setRackInternal(rack);
        commitController();
    }

    private void setRackInternal(Rack rack) {
        this.rack = rack;
        if (rack != null) {
            onAttach();
        } else {
            onDetach();
        }
    }

    public RackComponent() {
        construct();
    }

    public RackComponent(Rack rack) {
        setRackInternal(rack);
        construct();
    }

    protected void construct() {
    }

    protected void onAttach() {
    }

    protected void onDetach() {
    }

    protected void commitController() {
    }

    @Override
    public void restore() {
    }

}
