////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustic.internal.rack;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.effect.IEffect;
import com.teotigraphix.caustic.effect.IEffect.EffectType;
import com.teotigraphix.caustic.effect.IEffectComponent;
import com.teotigraphix.caustic.effect.IEffectsRack;
import com.teotigraphix.caustic.internal.effect.AutowahEffect;
import com.teotigraphix.caustic.internal.effect.BitcrusherEffect;
import com.teotigraphix.caustic.internal.effect.ChorusEffect;
import com.teotigraphix.caustic.internal.effect.CompressorEffect;
import com.teotigraphix.caustic.internal.effect.DistortionEffect;
import com.teotigraphix.caustic.internal.effect.EffectsRack;
import com.teotigraphix.caustic.internal.effect.FlangerEffect;
import com.teotigraphix.caustic.internal.effect.ParametricEQEffect;
import com.teotigraphix.caustic.internal.effect.PhaserEffect;
import com.teotigraphix.caustic.internal.machine.Bassline;
import com.teotigraphix.caustic.internal.machine.Beatbox;
import com.teotigraphix.caustic.internal.machine.PCMSynth;
import com.teotigraphix.caustic.internal.machine.SubSynth;
import com.teotigraphix.caustic.internal.mixer.MixerDelay;
import com.teotigraphix.caustic.internal.mixer.MixerPanel;
import com.teotigraphix.caustic.internal.mixer.MixerReverb;
import com.teotigraphix.caustic.internal.output.OutputPanel;
import com.teotigraphix.caustic.internal.sequencer.PatternSequencer;
import com.teotigraphix.caustic.internal.sequencer.Sequencer;
import com.teotigraphix.caustic.internal.sequencer.StepSequencer;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.caustic.mixer.IMixerPanel;
import com.teotigraphix.caustic.mixer.MixerEffectType;
import com.teotigraphix.caustic.output.IOutputPanel;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustic.rack.IRackFactory;
import com.teotigraphix.caustic.sequencer.IPatternSequencer;
import com.teotigraphix.caustic.sequencer.ISequencer;
import com.teotigraphix.common.IPersist;

/**
 * The default implementation of the {@link RackFactory} factory for the Rack
 * class.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class RackFactory implements IRackFactory
{

    public RackFactory()
    {
    }

    @Override
    public IMixerPanel createMixerPanel()
    {
        return new MixerPanel();
    }

    @Override
    public IEffectsRack createEffectRack()
    {
        return new EffectsRack();
    }

    @Override
    public IOutputPanel createOutputPanel()
    {
        return new OutputPanel();
    }

    @Override
    public ISequencer createSequencer()
    {
        return new Sequencer();
    }

    @Override
    public IPatternSequencer createPatternSequencer(IMachine machine)
    {
        PatternSequencer instance = null;
        if (machine.getType().equals(MachineType.BASSLINE))
            instance = new StepSequencer(machine);
        else if (machine.getType().equals(MachineType.BEATBOX)
                || machine.getType().equals(MachineType.PCMSYNTH)
                || machine.getType().equals(MachineType.SUBSYNTH))
            instance = new PatternSequencer(machine);

        return instance;
    }

    @Override
    public IMachine create(String machineId, MachineType machineType)
            throws CausticException
    {
        IMachine machine = null;

        if (machineType == MachineType.SUBSYNTH)
            machine = new SubSynth(machineId);
        else if (machineType == MachineType.PCMSYNTH)
            machine = new PCMSynth(machineId);
        else if (machineType == MachineType.BEATBOX)
            machine = new Beatbox(machineId);
        else if (machineType == MachineType.BASSLINE)
            machine = new Bassline(machineId);

        return machine;
    }

    @Override
    public IPersist createPersistable(IRack rack)
    {
        return new RackState((Rack) rack);
    }

    @Override
    public IEffectComponent createMixerEffect(IMixerPanel mixer,
            MixerEffectType type)
    {
        switch (type)
        {
        case DELAY:
            return new MixerDelay(mixer);
        case REVERB:
            return new MixerReverb(mixer);
        }
        return null;
    }

    @Override
    public IEffect createEffect(IEffectsRack panel, int index, EffectType type)
    {
        IEffect effect = null;
        switch (type)
        {
        case DISTORTION:
            effect = new DistortionEffect(index, panel);
            break;
        case COMPRESSOR:
            effect = new CompressorEffect(index, panel);
            break;
        case BITCRUSHER:
            effect = new BitcrusherEffect(index, panel);
            break;
        case FLANGER:
            effect = new FlangerEffect(index, panel);
            break;
        case PHASER:
            effect = new PhaserEffect(index, panel);
            break;
        case CHORUS:
            effect = new ChorusEffect(index, panel);
            break;
        case AUTOWAH:
            effect = new AutowahEffect(index, panel);
            break;
        case PARAMETRICEQ:
            effect = new ParametricEQEffect(index, panel);
            break;
        }

        return effect;
    }

}
