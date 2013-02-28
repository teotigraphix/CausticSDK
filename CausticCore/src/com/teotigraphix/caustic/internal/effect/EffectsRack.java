
package com.teotigraphix.caustic.internal.effect;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.util.Log;

import com.teotigraphix.caustic.effect.IEffect;
import com.teotigraphix.caustic.effect.IEffect.EffectType;
import com.teotigraphix.caustic.effect.IEffectsRack;
import com.teotigraphix.caustic.internal.core.CoreConstants;
import com.teotigraphix.caustic.internal.device.Device;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.EffectRackMessage;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustic.rack.IRack.MachineChangeKind;
import com.teotigraphix.caustic.rack.IRack.OnMachineChangeListener;
import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.IPersist;

@SuppressLint("UseSparseArrays")
public class EffectsRack extends Device implements IEffectsRack, OnMachineChangeListener {

    private static final String DEVICE_ID = "effects_rack";

    private final Map<Integer, EffectData> mEffectDataMap;

    //--------------------------------------------------------------------------
    //
    // IRackAware API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // rack
    //----------------------------------

    private IRack mRack;

    private boolean skipCreateMessage = false;

    @Override
    public IRack getRack() {
        return mRack;
    }

    @Override
    public void setRack(IRack value) {
        if (mRack != null) {
            mRack.removeOnMachineChangeListener(this);
            setEngine(null);
        }
        mRack = value;

        if (mRack != null) {
            mRack.addOnMachineChangeListener(this);
            setEngine(mRack.getEngine());
        }
    }

    @Override
    public boolean hasEffectsFor(IMachine machine) {
        boolean exists = mEffectDataMap.containsKey(machine.getIndex());
        if (!exists)
            return false;
        EffectData data = mEffectDataMap.get(machine.getIndex());
        return data.getEffects().size() > 0;
    }

    @Override
    public Map<Integer, EffectData> getEffects() {
        // only returns entries that contain IEffect instances
        Map<Integer, EffectData> map = new HashMap<Integer, EffectData>();

        for (EffectData data : mEffectDataMap.values()) {
            if (!data.isEmpty()) {
                map.put(data.getIndex(), data);
            }
        }
        return map;
    }

    @Override
    public Map<Integer, IEffect> getEffectsFor(IMachine machine) {
        Map<Integer, IEffect> result = new HashMap<Integer, IEffect>();

        Map<Integer, EffectData> map = getEffects();
        if (map.containsKey(machine.getIndex())) {
            result = map.get(machine.getIndex()).getEffects();
        }
        return result;
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public EffectsRack() {
        super();
        setId(DEVICE_ID);
        mEffectDataMap = new HashMap<Integer, EffectData>();
    }

    //--------------------------------------------------------------------------
    //
    // IPersistable API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        for (Entry<Integer, EffectData> entry : mEffectDataMap.entrySet()) {
            EffectData data = entry.getValue();
            IMemento machine = memento
                    .createChild(CoreConstants.TAG_MACHINE, data.mMachine.getId());
            machine.putInteger("index", data.mMachine.getIndex());
            data.copy(machine);
        }
    }

    @Override
    public void paste(IMemento memento) {
        IMemento[] machines = memento.getChildren(CoreConstants.TAG_MACHINE);
        for (IMemento child : machines) {
            // create the effect and pass the effect, save state
            // as with the mixer, the machine has to exist here
            EffectData info = mEffectDataMap.get(child.getInteger("index"));
            info.paste(child);
        }
    }

    //--------------------------------------------------------------------------
    //
    // Public :: Handlers
    //
    //--------------------------------------------------------------------------

    public void addMachine(IMachine machine) {
        EffectData info = new EffectData(machine);
        mEffectDataMap.put(machine.getIndex(), info);
    }

    public void removeMachine(IMachine machine) {
        mEffectDataMap.remove(machine.getIndex());
    }

    @Override
    public void onMachineChanged(IMachine machine, MachineChangeKind kind) {
        if (kind == MachineChangeKind.ADDED || kind == MachineChangeKind.LOADED) {
            addMachine(machine);
        } else if (kind == MachineChangeKind.REMOVED) {
            removeMachine(machine);
        }
    }

    private IEffect put(int index, int machineIndex, EffectType type) {
        IMachine machine = getRack().getMachine(machineIndex);
        return putEffect(machine, index, type);
    }

    @Override
    public IEffect putEffect(IMachine machine, int slot, EffectType type) {

        if (type == null) {
            Log.e("EffectsRack ", "[" + machine.getId() + "] type is null");
            return null;
        }

        if (slot > 1)
            slot = 1;

        EffectData info = mEffectDataMap.get(machine.getIndex());
        // already have an effect need a remove command in OSC
        if (info.contains(slot))
            return null;

        IEffect effect = getRack().getFactory().createEffect(this, slot, type);

        effect.setMachine(machine);
        info.put(slot, effect);

        if (!skipCreateMessage) {
            EffectRackMessage.CREATE.send(getEngine(), machine.getIndex(), slot, type.getValue());
        }
        return effect;
    }

    @Override
    public IEffect removeEffect(IMachine machine, int slot) {
        EffectData info = mEffectDataMap.get(machine.getIndex());
        if (info == null)
            return null;

        IEffect effect = info.remove(slot);
        EffectRackMessage.REMOVE.send(getEngine(), machine.getIndex(), slot);
        return effect;
    }

    @Override
    public IEffect getEffect(IMachine machine, int slot) {
        EffectData info = mEffectDataMap.get(machine.getIndex());
        // already have an effect need a remove command in OSC
        if (!info.contains(slot))
            return null;

        return info.get(slot);
    }

    public class EffectData implements IPersist {
        private IMachine mMachine;

        final Map<Integer, IEffect> effects = new HashMap<Integer, IEffect>();

        EffectData(IMachine machine) {
            mMachine = machine;
        }

        public Map<Integer, IEffect> getEffects() {
            return effects;
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

        void put(int index, IEffect effect) {
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
            effect.copy(memento);
        }

        @Override
        public void paste(IMemento memento) {
            for (IMemento child : memento.getChildren(EffectConstants.TAG_EFFECT)) {
                IEffect effect = EffectsRack.this.put(child.getInteger(EffectConstants.ATT_INDEX),
                        memento.getInteger(EffectConstants.ATT_INDEX),
                        EffectType.toType(child.getInteger(EffectConstants.ATT_TYPE)));
                effect.paste(child);
                // (mschmalle)  implement loadChannel()
            }
        }
    }

    @Override
    public void copyChannel(IMachine machine, IMemento memento) {
        memento.putInteger("index", machine.getIndex());
        IEffect e1 = getEffect(machine, 0);
        if (e1 != null) {
            e1.copy(memento.createChild("effect"));
        }
        IEffect e2 = getEffect(machine, 1);
        if (e2 != null) {
            e2.copy(memento.createChild("effect"));
        }
    }

    @Override
    public void pasteChannel(IMachine machine, IMemento memento) {
        EffectData data = mEffectDataMap.get(machine.getIndex());
        data.paste(memento);
    }

    @Override
    public void restore() {
        super.restore();
        skipCreateMessage = true;
        // loop through exsiting machines
        // find effects for slot 0, 1 load them up
        for (IMachine machine : getRack().getMachineMap().values()) {
            int effect0 = (int)EffectRackMessage.TYPE.send(getEngine(), machine.getIndex(), 0);
            int effect1 = (int)EffectRackMessage.TYPE.send(getEngine(), machine.getIndex(), 1);

            if (effect0 >= 0) {
                IEffect effect = putEffect(machine, 0, EffectType.toType(effect0));
                effect.restore();
            }
            if (effect1 >= 0) {
                IEffect effect = putEffect(machine, 1, EffectType.toType(effect1));
                effect.restore();
            }
        }
        skipCreateMessage = false;
    }

}
