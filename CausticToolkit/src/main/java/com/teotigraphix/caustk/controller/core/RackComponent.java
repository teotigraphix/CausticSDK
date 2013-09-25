
package com.teotigraphix.caustk.controller.core;

import com.teotigraphix.caustk.controller.ICaustkController;

public abstract class RackComponent implements IRackComponent {

    private transient ICaustkController controller;

    @Override
    public ICaustkController getController() {
        return controller;
    }

    @Override
    public void setController(ICaustkController controller) {
        setControllerInternal(controller);
        commitController();
    }

    private void setControllerInternal(ICaustkController controller) {
        this.controller = controller;
        if (controller != null) {
            onAttach();
        } else {
            onDetach();
        }
    }

    public RackComponent() {
        construct();
    }

    public RackComponent(ICaustkController controller) {
        setControllerInternal(controller);
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
