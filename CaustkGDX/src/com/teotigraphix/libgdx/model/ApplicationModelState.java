
package com.teotigraphix.libgdx.model;

import java.io.Serializable;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.libgdx.application.CausticSongFile;

/**
 * The serialized binary file root.
 * <p>
 * Each application creates a specific model used within it's
 * {@link ICaustkModel} implementations.
 */
public abstract class ApplicationModelState implements Serializable {

    private static final long serialVersionUID = 7409599794451779182L;

    //----------------------------------
    // controller
    //----------------------------------

    private transient ICaustkController controller;

    public ICaustkController getController() {
        return controller;
    }

    public void setController(ICaustkController controller) {
        this.controller = controller;
    }

    //----------------------------------
    // id
    //----------------------------------

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //----------------------------------
    // songFile
    //----------------------------------

    private CausticSongFile songFile;

    public CausticSongFile getSongFile() {
        return songFile;
    }

    public void setSongFile(CausticSongFile value) {
        songFile = value;
    }

    public ApplicationModelState() {
    }

    public void save() {
    }

}
