
package com.teotigraphix.caustic.effect.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.core.IPersist;
import com.teotigraphix.caustic.effect.IEffect;
import com.teotigraphix.caustic.effect.IEffect.EffectType;
import com.teotigraphix.caustic.effect.IEffectsRack;
import com.teotigraphix.caustic.internal.effect.EffectConstants;
import com.teotigraphix.caustic.internal.effect.EffectsRack;
import com.teotigraphix.caustic.machine.IMachine;

public class EffectData implements IPersist {
    public IMachine mMachine;

    final Map<Integer, IEffect> effects = new HashMap<Integer, IEffect>();

    private IEffectsRack effectsRack;

    public EffectData(IEffectsRack effectsRack, IMachine machine) {
        this.effectsRack = effectsRack;
        mMachine = machine;
    }

    public Map<Integer, IEffect> getEffects() {
        return effects;
    }

    public IMachine getMachine() {
        return mMachine;
    }

    public int getIndex() {
        return mMachine.getIndex();
    }

    public boolean isEmpty() {
        return effects.isEmpty();
    }

    public boolean contains(int index) {
        return effects.containsKey(index);
    }

    public void put(int index, IEffect effect) {
        effects.put(index, effect);
    }

    public IEffect get(int index) {
        return effects.get(index);
    }

    public IEffect remove(int index) {
        return effects.remove(index);
    }

    @Override
    public void copy(IMemento memento) {
        for (Entry<Integer, IEffect> entry : effects.entrySet()) {
            IEffect effect = entry.getValue();
            saveChannel(effect, memento.createChild(EffectConstants.TAG_EFFECT));
        }
    }

    private void saveChannel(IEffect effect, IMemento memento) {
        memento.putInteger("channel", getIndex());
        memento.putInteger("type", effect.getType().getValue());
        effect.copy(memento);
    }

    @Override
    public void paste(IMemento memento) {
        for (IMemento child : memento.getChildren(EffectConstants.TAG_EFFECT)) {
            IEffect effect = ((EffectsRack)effectsRack).put(
                    child.getInteger(EffectConstants.ATT_INDEX),
                    memento.getInteger(EffectConstants.ATT_INDEX),
                    EffectType.toType(child.getInteger(EffectConstants.ATT_TYPE)));
            effect.paste(child);
            // (mschmalle)  implement loadChannel()
        }
    }
}
