
package com.teotigraphix.gdx.groove.ui.components;

import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;

public class ViewStackData {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private String id;

    private String label;

    private String icon;

    private String helpText;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // id
    //----------------------------------

    public String getId() {
        return id;
    }

    //----------------------------------
    // label
    //----------------------------------

    public String getLabel() {
        return label;
    }

    //----------------------------------
    // icon
    //----------------------------------

    public String getIcon() {
        return icon;
    }

    //----------------------------------
    // helpText
    //----------------------------------

    public String getHelpText() {
        return helpText;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ViewStackData(String id, String label, String icon, String helpText) {
        this.id = id;
        this.label = label;
        this.icon = icon;
        this.helpText = helpText;
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    public ButtonBarItem toButtonBarItem() {
        return new ButtonBarItem(id, label, icon, helpText);
    }
}
