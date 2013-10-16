
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Base for all panes held within a {@link PaneStack}.
 * 
 * @author Michael Schmalle
 */
public class Pane extends ControlTable {

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // label
    //----------------------------------

    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Pane(Skin skin, String label) {
        super(skin);
        this.label = label;
    }

}
