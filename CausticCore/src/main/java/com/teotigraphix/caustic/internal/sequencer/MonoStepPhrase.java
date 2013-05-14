package com.teotigraphix.caustic.internal.sequencer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.teotigraphix.caustic.osc.PatternSequencerMessage;
import com.teotigraphix.caustic.sequencer.ITrigger;

public class MonoStepPhrase extends StepPhraseBase
{

    public MonoStepPhrase(int bank, int index)
    {
        super(bank, index);
        // this initializes the map with with 16 steps at 60 pitch
        setLength(1);
    }

    @Override
    public final boolean hasTriggers()
    {
        return true;
    }

    @Override
    public void clear()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ITrigger getTriggerAtStep(int step, int pitch)
    {
        return getMonoTrigger(step);
    }

    @Override
    public ITrigger getTriggerAtBeat(float beat, int pitch)
    {
        int step = Resolution.toStep(beat, getResolution());
        return getTriggerAtStep(step, pitch);
    }

    @Override
    public Collection<ITrigger> getTriggersAtStep(int step)
    {
        Map<Integer, ITrigger> triggers = getPitchMapAt(step);
        return triggers.values();
    }

    @Override
    public Collection<ITrigger> getTriggersAtBeat(float beat)
    {
        int step = Resolution.toStep(beat, getResolution());
        return getTriggersAtStep(step);
    }

    @Override
    public List<ITrigger> getTriggers()
    {
        ArrayList<ITrigger> result = new ArrayList<ITrigger>();
        for (Map<Integer, ITrigger> map : getStepMap().values())
        {
            for (ITrigger trigger : map.values())
                result.add(trigger);
        }
        return result;
    }

    @Override
    public void addNote(int pitch, float start, float end, float velocity,
            int flags)
    {
        throw new IllegalStateException(
                "MonoTriggerMap.addNote() not implemented");
    }

    @Override
    public void removeNote(int pitch, float start)
    {
        throw new IllegalStateException(
                "MonoTriggerMap.removeNote() not implemented");
    }

    @Override
    public void triggerOn(int step, int pitch, float gate, float velocity,
            int flags)
    {
        Trigger trigger = (Trigger) getMonoTrigger(step);
        if (trigger == null)
            throw new IllegalStateException(
                    "Trigger must not be null in mono mode");
        
        trigger.setPitch(pitch);
        trigger.setGate(gate);
        trigger.setVelocity(velocity);
        trigger.setFlags(flags);
        trigger.setSelected(true);

        float start = Resolution.toBeat(step, getResolution());
        float end = start + gate;

        PatternSequencerMessage.NOTE_DATA.send(getSequencer().getDevice()
                .getEngine(), getSequencer().getDevice().getIndex(), start,
                pitch, velocity, end, flags);

        fireTriggerDataChange(trigger, TriggerChangeKind.RESET);
    }

    @Override
    public void triggerOff(int step, int pitch)
    {
        Trigger trigger = (Trigger) getMonoTrigger(step);
        if (trigger == null)
            throw new IllegalStateException(
                    "Trigger must not be null in mono mode");

        trigger.setSelected(false);

        float start = Resolution.toBeat(step, getResolution());

        PatternSequencerMessage.NOTE_DATA_REMOVE.send(getSequencer()
                .getDevice().getEngine(),
                getSequencer().getDevice().getIndex(), start, trigger
                        .getPitch());

        fireTriggerDataChange(trigger, TriggerChangeKind.RESET);
    }

    ITrigger getMonoTrigger(int step)
    {
        return getTriggersAtStep(step).iterator().next();
    }
}
