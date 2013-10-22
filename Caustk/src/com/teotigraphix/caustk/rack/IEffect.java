
package com.teotigraphix.caustk.rack;

import java.io.Serializable;

import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.rack.effect.EffectType;

public interface IEffect extends Serializable, IRestore {

    EffectType getType();

    Rack getRack();

    void setRack(Rack rack);

    int getToneIndex();

    int getSlot();

}
