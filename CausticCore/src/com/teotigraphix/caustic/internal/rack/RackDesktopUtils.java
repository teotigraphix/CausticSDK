package com.teotigraphix.caustic.internal.rack;

import com.teotigraphix.caustic.core.ICausticEngine;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustic.rack.IRackFactory;

public class RackDesktopUtils
{
    public static IRack createRack(IRackFactory factory, ICausticEngine engine)
    {
        Rack rack = new Rack(factory);
        rack.setEngine(engine);
        return rack;
    }

    public static void setFactory(IRack rack, IRackFactory factory)
    {
        ((Rack) rack).setFactory(factory);
    }

}
