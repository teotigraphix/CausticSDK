
package com.teotigraphix.libgdx.model;

import java.io.Serializable;

import com.teotigraphix.caustk.controller.ICaustkController;

/**
 * The serialized binary file root.
 * <p>
 * Each application creates a specific model used within it's
 * {@link ICaustkModel} implementations.
 */
public abstract class ApplicationModelState implements Serializable {

    private static final long serialVersionUID = 7409599794451779182L;

    private transient ICaustkController controller;

    public ICaustkController getController() {
        return controller;
    }

    public void setController(ICaustkController controller) {
        this.controller = controller;
    }

    public void save() {
    }

}
