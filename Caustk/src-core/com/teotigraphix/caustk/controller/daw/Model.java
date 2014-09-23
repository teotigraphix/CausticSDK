
package com.teotigraphix.caustk.controller.daw;

import com.badlogic.gdx.graphics.Color;
import com.teotigraphix.caustk.controller.helper.Scales;
import com.teotigraphix.caustk.node.RackNode;

public class Model {

    public static final Color PUSH_COLOR_BLACK = Colors.Gray.getColor();

    public static final Color PUSH_COLOR2_WHITE = Color.WHITE;

    public static final Color PUSH_COLOR2_OCEAN_HI = Colors.Cyan.getColor();

    public static final Color PUSH_COLOR2_GREEN_HI = Colors.DarkGreen.getColor();

    public static final Color PUSH_COLOR2_BLUE = Colors.ElectricBlue.getColor();

    public static final Color PUSH_COLOR2_BLACK = Colors.Gray.getColor();

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private Scales scales;

    private RackNode rackNode;

    private MachineBankProxy machineBank;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // pads
    //----------------------------------

    public Scales getScales() {
        return scales;
    }

    //----------------------------------
    // machineBank
    //----------------------------------

    public MachineBankProxy getMachineBank() {
        return machineBank;
    }

    //----------------------------------
    // machineBank
    //----------------------------------

    RackNode getRackNode() {
        return rackNode;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Model(RackNode rackNode, Scales scales) {
        this.rackNode = rackNode;
        this.scales = scales;

        machineBank = new MachineBankProxy(this);
    }

    public CursorClipProxy createCursorClip(int cols, int rows) {
        return new CursorClipProxy(this, cols, rows);
    }

}
