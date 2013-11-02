////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.live;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.UUID;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.tone.BasslineTone;
import com.teotigraphix.caustk.rack.tone.BeatboxTone;
import com.teotigraphix.caustk.rack.tone.EightBitSynth;
import com.teotigraphix.caustk.rack.tone.FMSynthTone;
import com.teotigraphix.caustk.rack.tone.ModularTone;
import com.teotigraphix.caustk.rack.tone.OrganTone;
import com.teotigraphix.caustk.rack.tone.PCMSynthTone;
import com.teotigraphix.caustk.rack.tone.PadSynthTone;
import com.teotigraphix.caustk.rack.tone.SubSynthTone;
import com.teotigraphix.caustk.rack.tone.Tone;
import com.teotigraphix.caustk.rack.tone.ToneDescriptor;
import com.teotigraphix.caustk.rack.tone.ToneType;
import com.teotigraphix.caustk.rack.tone.ToneUtils;
import com.teotigraphix.caustk.rack.tone.VocoderTone;

/**
 * @author Michael Schmalle
 */
public class ToneFactory extends CaustkSubFactoryBase {

    private static final int NUM_MACHINES = 14;

    public ToneFactory() {
    }

    Tone createTone(Machine machine, int index, String toneName, ToneType toneType)
            throws CausticException {
        final IRack rack = getFactory().getRack();

        if (index > NUM_MACHINES - 1)
            throw new CausticException("Only 14 machines allowed in a rack");

        //        if (tones.containsKey(index))
        //            throw new CausticException("{" + index + "} tone is already defined");

        //        if (!restoring)
        RackMessage.CREATE.send(rack, toneType.getValue(), toneName, index);

        Tone tone = null;
        switch (toneType) {
            case Bassline:
                tone = new BasslineTone(machine);
                initializeTone(tone, toneName, toneType, index);
                ToneUtils.setup(tone);
                break;
            case Beatbox:
                tone = new BeatboxTone(machine);
                initializeTone(tone, toneName, toneType, index);
                ToneUtils.setup(tone);
                break;
            case PCMSynth:
                tone = new PCMSynthTone(machine);
                initializeTone(tone, toneName, toneType, index);
                ToneUtils.setup(tone);
                break;
            case SubSynth:
                tone = new SubSynthTone(machine);
                initializeTone(tone, toneName, toneType, index);
                ToneUtils.setup(tone);
                break;
            case PadSynth:
                tone = new PadSynthTone(machine);
                initializeTone(tone, toneName, toneType, index);
                ToneUtils.setup(tone);
                break;
            case Organ:
                tone = new OrganTone(machine);
                initializeTone(tone, toneName, toneType, index);
                ToneUtils.setup(tone);
                break;
            case Vocoder:
                tone = new VocoderTone(machine);
                initializeTone(tone, toneName, toneType, index);
                ToneUtils.setup(tone);
                break;
            case EightBitSynth:
                tone = new EightBitSynth(machine);
                initializeTone(tone, toneName, toneType, index);
                ToneUtils.setup(tone);
                break;
            case Modular:
                tone = new ModularTone(machine);
                initializeTone(tone, toneName, toneType, index);
                ToneUtils.setup(tone);
                break;
            case FMSynth:
                tone = new FMSynthTone(machine);
                initializeTone(tone, toneName, toneType, index);
                ToneUtils.setup(tone);
                break;
            default:
                break;
        }

        return tone;
    }

    @SuppressWarnings("unchecked")
    <T extends Tone> T createTone(int index, String name, Class<? extends Tone> toneClass)
            throws CausticException {
        final IRack rack = getFactory().getRack();

        T tone = null;
        try {
            Constructor<? extends Tone> constructor = toneClass.getConstructor(IRack.class);
            tone = (T)constructor.newInstance(rack);
            initializeTone(tone, name, tone.getToneType(), index);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        ToneUtils.setup(toneClass.cast(tone));

        RackMessage.CREATE.send(rack, tone.getToneType().getValue(), tone.getName(),
                tone.getIndex());

        return tone;
    }

    private void initializeTone(Tone tone, String toneName, ToneType toneType, int index) {
        tone.setId(UUID.randomUUID());
        tone.setIndex(index);
        ToneUtils.setName(tone, toneName);
    }

    public static int nextIndex(Map<Integer, Machine> machines) {
        int index = 0;
        for (index = 0; index < NUM_MACHINES; index++) {
            if (!machines.containsKey(index))
                break;
        }
        return index;
    }

    public Tone createTone(Machine machine, ToneDescriptor descriptor) throws CausticException {
        return createTone(machine, descriptor.getIndex(), descriptor.getName(),
                descriptor.getToneType());
    }
}
