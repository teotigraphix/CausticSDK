package com.teotigraphix.caustk.sound;

import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.sound.effect.EffectType;

public interface IEffect extends ISerialize, IRestore {

    EffectType getType();

    int getToneIndex();

    int getSlot();

}
