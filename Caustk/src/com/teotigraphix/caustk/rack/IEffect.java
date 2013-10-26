
package com.teotigraphix.caustk.rack;

import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.rack.effect.EffectType;

public interface IEffect extends IRestore {

    EffectType getType();

    IRack getRack();

    void setRack(IRack rack);

    int getToneIndex();

    int getSlot();

}
