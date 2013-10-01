
package com.teotigraphix.caustk.sound;

import java.io.Serializable;

import com.teotigraphix.caustk.controller.core.Rack;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.sound.effect.EffectType;

public interface IEffect extends Serializable, IRestore {

    EffectType getType();

    Rack getRack();

    void setRack(Rack rack);

    int getToneIndex();

    int getSlot();

}
