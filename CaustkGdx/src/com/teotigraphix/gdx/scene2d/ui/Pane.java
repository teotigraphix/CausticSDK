
package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teotigraphix.gdx.scene2d.ControlTable;

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
