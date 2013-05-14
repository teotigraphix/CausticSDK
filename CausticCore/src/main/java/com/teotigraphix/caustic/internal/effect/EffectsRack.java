package com.teotigraphix.caustic.internal.effect;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.effect.IEffect;
import com.teotigraphix.caustic.effect.IEffect.EffectType;
import com.teotigraphix.caustic.effect.IEffectsRack;
import com.teotigraphix.caustic.effect.data.EffectData;
import com.teotigraphix.caustic.internal.device.Device;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.osc.EffectRackMessage;

public class EffectsRack extends Device implements IEffectsRack
{

    private static final String DEVICE_ID = "effects_rack";

    private final Map<Integer, EffectData> mEffectDataMap;

    private boolean skipCreateMessage = false;

    @Override
    public boolean hasEffectsFor(IMachine machine)
    {
        boolean exists = mEffectDataMap.containsKey(machine.getIndex());
        if (!exists)
            return false;
        EffectData data = mEffectDataMap.get(machine.getIndex());
        return data.getEffects().size() > 0;
    }

    @Override
    public Map<Integer, EffectData> getEffects()
    {
        // only returns entries that contain IEffect instances
        Map<Integer, EffectData> map = new HashMap<Integer, EffectData>();

        for (EffectData data : mEffectDataMap.values())
        {
            if (!data.isEmpty())
            {
                map.put(data.getIndex(), data);
            }
        }
        return map;
    }

    @Override
    public Map<Integer, IEffect> getEffectsFor(IMachine machine)
    {
        Map<Integer, IEffect> result = new HashMap<Integer, IEffect>();

        Map<Integer, EffectData> map = getEffects();
        if (map.containsKey(machine.getIndex()))
        {
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
    public EffectsRack()
    {
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
    public void copy(IMemento memento)
    {
        for (Entry<Integer, EffectData> entry : mEffectDataMap.entrySet())
        {
            EffectData data = entry.getValue();
            IMemento machine = memento.createChild("machine", data.mMachine
                    .getId());
            machine.putInteger("index", data.mMachine.getIndex());
            data.copy(machine);
        }
    }

    @Override
    public void paste(IMemento memento)
    {
        IMemento[] machines = memento.getChildren("machine");
        for (IMemento child : machines)
        {
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

    public void addMachine(IMachine machine)
    {
        EffectData info = new EffectData(this, machine);
        mEffectDataMap.put(machine.getIndex(), info);
    }

    public void removeMachine(IMachine machine)
    {
        mEffectDataMap.remove(machine.getIndex());
    }

    //    @Override
    //    public void onMachineChanged(IMachine machine, MachineChangeKind kind)
    //    {
    //        if (kind == MachineChangeKind.ADDED || kind == MachineChangeKind.LOADED)
    //        {
    //            addMachine(machine);
    //        }
    //        else if (kind == MachineChangeKind.REMOVED)
    //        {
    //            removeMachine(machine);
    //        }
    //    }

    public IEffect put(int index, int machineIndex, EffectType type)
    {
        // XXX FIXME SoundSource is NOT in caustic lib
        //IMachine machine = SoundSource.getMachine(machineIndex);
        //return putEffect(machine, index, type);
        return null;
    }

    @Override
    public IEffect putEffect(IMachine machine, int slot, EffectType type)
    {

        if (type == null)
        {
            //XXX Log.e("EffectsRack ", "[" + machine.getId() + "] type is null");
            return null;
        }

        if (slot > 1)
            slot = 1;

        EffectData info = mEffectDataMap.get(machine.getIndex());
        // already have an effect need a remove command in OSC
        if (info.contains(slot))
            return null;

        IEffect effect = getFactory().createEffect(this, slot, type);

        effect.setMachine(machine);
        info.put(slot, effect);

        if (!skipCreateMessage)
        {
            EffectRackMessage.CREATE.send(getEngine(), machine.getIndex(),
                    slot, type.getValue());
        }
        return effect;
    }

    @Override
    public IEffect removeEffect(IMachine machine, int slot)
    {
        EffectData info = mEffectDataMap.get(machine.getIndex());
        if (info == null)
            return null;

        IEffect effect = info.remove(slot);
        EffectRackMessage.REMOVE.send(getEngine(), machine.getIndex(), slot);
        return effect;
    }

    @Override
    public IEffect getEffect(IMachine machine, int slot)
    {
        EffectData info = mEffectDataMap.get(machine.getIndex());
        // already have an effect need a remove command in OSC
        if (!info.contains(slot))
            return null;

        return info.get(slot);
    }

    @Override
    public void copyChannel(IMachine machine, IMemento memento)
    {
        memento.putInteger("index", machine.getIndex());
        IEffect e1 = getEffect(machine, 0);
        if (e1 != null)
        {
            IMemento child1 = memento.createChild("effect");
            child1.putInteger("type", e1.getType().getValue());
            child1.putInteger("index", e1.getIndex());
            e1.copy(child1);
        }
        IEffect e2 = getEffect(machine, 1);
        if (e2 != null)
        {
            IMemento child2 = memento.createChild("effect");
            child2.putInteger("type", e2.getType().getValue());
            child2.putInteger("index", e2.getIndex());
            e2.copy(child2);
        }
    }

    @Override
    public void pasteChannel(IMachine machine, IMemento memento)
    {
        EffectData data = mEffectDataMap.get(machine.getIndex());
        data.paste(memento);
    }

    @Override
    public void restore()
    {
        super.restore();
        skipCreateMessage = true;
        // loop through exsiting machines
        // find effects for slot 0, 1 load them up
        for (EffectData data : mEffectDataMap.values())
        {
            int effect0 = (int) EffectRackMessage.TYPE.send(getEngine(), data
                    .getIndex(), 0);
            int effect1 = (int) EffectRackMessage.TYPE.send(getEngine(), data
                    .getIndex(), 1);

            if (effect0 >= 0)
            {
                IEffect effect = putEffect(data.getMachine(), 0, EffectType
                        .toType(effect0));
                effect.restore();
            }
            if (effect1 >= 0)
            {
                IEffect effect = putEffect(data.getMachine(), 1, EffectType
                        .toType(effect1));
                effect.restore();
            }
        }
        skipCreateMessage = false;
    }

}
