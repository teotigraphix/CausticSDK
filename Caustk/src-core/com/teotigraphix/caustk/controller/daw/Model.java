
package com.teotigraphix.caustk.controller.daw;

import com.badlogic.gdx.graphics.Color;
import com.teotigraphix.caustk.controller.core.AbstractControlSurface;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.node.RackNode;

public class Model {

    public static final Color BLACK = Colors.Gray.getColor();

    public static final Color WHITE = Color.WHITE;

    public static final Color OCEAN = Colors.Cyan.getColor();

    public static final Color PUSH_COLOR2_GREEN_HI = Colors.DarkGreen.getColor();

    public static final Color PUSH_COLOR2_BLUE = Colors.ElectricBlue.getColor();

    public static final Color PUSH_COLOR2_BLACK = Colors.Gray.getColor();

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private ICaustkRack rack;

    private MachineBankProxy machineBank;

    private AbstractControlSurface surface;

    private int gridSize;

    protected AbstractControlSurface getSurface() {
        return surface;
    }

    protected void setSurface(AbstractControlSurface surface) {
        this.surface = surface;
    }

    public ICaustkRack getRack() {
        return rack;
    }

    //    public int getMidiRoot() {
    //        return midiRoot;
    //    }

    public int getGridSize() {
        return gridSize;
    }

    public int getNumRows() {
        return gridSize;
    }

    public int getNumColumns() {
        return gridSize;
    }

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

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
        return rack.getRackNode();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Model(ICaustkRack rack, int gridSize) {
        this.rack = rack;
        this.gridSize = gridSize;

        machineBank = new MachineBankProxy(this);
    }

    public void flush() {
        surface.flush();
    }

    public void flush(boolean forceRefresh) {
        surface.flush(forceRefresh);
    }

    public CursorClipProxy createCursorClip(int cols, int rows) {
        return new CursorClipProxy(this, cols, rows);
    }

}
