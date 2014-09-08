
package com.teotigraphix.caustk.node.machine.patch.modular;

import java.util.Collection;

import com.teotigraphix.caustk.core.osc.ModularMessage.ModularComponentType;
import com.teotigraphix.caustk.node.ICaustkNode;

public interface IModularComponent extends ICaustkNode {

    Collection<ModularControl> getControls();

    Collection<ModularControl> getFrontControls();

    Collection<ModularControl> getRearControls();

    int getBay();

    ModularComponentType getType();

    void connect(IModularJack out, IModularComponent destination, IModularJack in);

    int getMachineIndex();

}
