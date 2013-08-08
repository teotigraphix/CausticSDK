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

package com.teotigraphix.caustk.tone;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.components.PatternSequencerComponent;
import com.teotigraphix.caustk.core.components.SynthComponent;
import com.teotigraphix.caustk.core.components.modular.AREnvelope;
import com.teotigraphix.caustk.core.components.modular.Arpeggiator;
import com.teotigraphix.caustk.core.components.modular.Crossfader;
import com.teotigraphix.caustk.core.components.modular.CrossoverModule;
import com.teotigraphix.caustk.core.components.modular.DADSREnvelope;
import com.teotigraphix.caustk.core.components.modular.DecayEnvelope;
import com.teotigraphix.caustk.core.components.modular.DelayModule;
import com.teotigraphix.caustk.core.components.modular.FMPair;
import com.teotigraphix.caustk.core.components.modular.FormantFilter;
import com.teotigraphix.caustk.core.components.modular.LagProcessor;
import com.teotigraphix.caustk.core.components.modular.MiniLFO;
import com.teotigraphix.caustk.core.components.modular.ModularBayComponent;
import com.teotigraphix.caustk.core.components.modular.ModularComponentBase;
import com.teotigraphix.caustk.core.components.modular.ModularPanel;
import com.teotigraphix.caustk.core.components.modular.NoiseGenerator;
import com.teotigraphix.caustk.core.components.modular.Oscillator;
import com.teotigraphix.caustk.core.components.modular.PanModule;
import com.teotigraphix.caustk.core.components.modular.PulseGenerator;
import com.teotigraphix.caustk.core.components.modular.SVFilter;
import com.teotigraphix.caustk.core.components.modular.SampleAndHold;
import com.teotigraphix.caustk.core.components.modular.Saturator;
import com.teotigraphix.caustk.core.components.modular.SixInputMixer;
import com.teotigraphix.caustk.core.components.modular.StereoLPF;
import com.teotigraphix.caustk.core.components.modular.SubOscillator;
import com.teotigraphix.caustk.core.components.modular.ThreeInputMixer;
import com.teotigraphix.caustk.core.components.modular.TwoInputMixer;
import com.teotigraphix.caustk.core.osc.ModularMessage;

public class ModularTone extends Tone {

    @Override
    public ToneType getToneType() {
        return ToneType.Modular;
    }

    private transient ModularPanel modularPanel;

    public ModularPanel getPanel() {
        return modularPanel;
    }

    public enum ComponentType {

        Empty(0),

        TwoToOneMixerModulator(1),

        ThreeToOneMixer(2),

        SixToOneMixer(3),

        Oscillator(4),

        SubOscillator(5),

        PulseGenerator(6),

        DADSREnvelope(7),

        AREnvelope(8),

        DecayEnvelope(9),

        SVFilter(10),

        StereoLPF(11),

        FormantFilter(12),

        MiniLFO(13),

        NoiseGenerator(14),

        PanModule(15),

        CrossFade(16),

        LagProcessor(17),

        Delay(18),

        SampleAndHold(19),

        CrossOver(20),

        // stand-alone effects ommited

        Saturator(24),

        FMPair(25),

        Arpeggiator(26);

        private int value;

        public int getValue() {
            return value;
        }

        ComponentType(int value) {
            this.value = value;
        }

    }

    public ModularComponentBase create(ComponentType componentType, int bay) {
        ModularComponentBase component = null;
        switch (componentType) {
            case TwoToOneMixerModulator:
                component = new TwoInputMixer(getController(), bay);
                component.setTone(this);
                break;
            case ThreeToOneMixer:
                component = new ThreeInputMixer(getController(), bay);
                component.setTone(this);
                break;
            case SixToOneMixer:
                component = new SixInputMixer(getController(), bay);
                component.setTone(this);
                break;
            case Oscillator:
                component = new Oscillator(getController(), bay);
                component.setTone(this);
                break;
            case SubOscillator:
                component = new SubOscillator(getController(), bay);
                component.setTone(this);
                break;
            case PulseGenerator:
                component = new PulseGenerator(getController(), bay);
                component.setTone(this);
                break;
            case DADSREnvelope:
                component = new DADSREnvelope(getController(), bay);
                component.setTone(this);
                break;
            case AREnvelope:
                component = new AREnvelope(getController(), bay);
                component.setTone(this);
                break;
            case DecayEnvelope:
                component = new DecayEnvelope(getController(), bay);
                component.setTone(this);
                break;
            case SVFilter:
                component = new SVFilter(getController(), bay);
                component.setTone(this);
                break;
            case StereoLPF:
                component = new StereoLPF(getController(), bay);
                component.setTone(this);
                break;
            case FormantFilter:
                component = new FormantFilter(getController(), bay);
                component.setTone(this);
                break;
            case MiniLFO:
                component = new MiniLFO(getController(), bay);
                component.setTone(this);
                break;
            case NoiseGenerator:
                component = new NoiseGenerator(getController(), bay);
                component.setTone(this);
                break;
            case PanModule:
                component = new PanModule(getController(), bay);
                component.setTone(this);
                break;
            case CrossFade:
                component = new Crossfader(getController(), bay);
                component.setTone(this);
                break;
            case LagProcessor:
                component = new LagProcessor(getController(), bay);
                component.setTone(this);
                break;
            case Delay:
                component = new DelayModule(getController(), bay);
                component.setTone(this);
                break;
            case SampleAndHold:
                component = new SampleAndHold(getController(), bay);
                component.setTone(this);
                break;
            case CrossOver:
                component = new CrossoverModule(getController(), bay);
                component.setTone(this);
                break;
            case Saturator:
                component = new Saturator(getController(), bay);
                component.setTone(this);
                break;
            case FMPair:
                component = new FMPair(getController(), bay);
                component.setTone(this);
                break;
            case Arpeggiator:
                component = new Arpeggiator(getController(), bay);
                component.setTone(this);
                break;

            default:
                break;
        }
        ModularMessage.CREATE.send(getEngine(), getIndex(), bay, componentType.getValue());
        return component;
    }

    // To create:
    // /caustic/create modular <name> [slot#]

    // to change the synth's only real "global" control
    // /caustic/modular/volume [value] where value's range is [0..2]

    // To delete a component
    // /caustic/modular/remove <component bay#>

    // To query the type of a component
    // /caustic/modular/type <component bay#> returns the type# from the list above

    public ModularTone(ICaustkController controller) {
        super(controller);

        modularPanel = new ModularPanel(controller);
        modularPanel.setTone(this);
    }

    public static void setup(Tone tone) {
        tone.addComponent(SynthComponent.class, new SynthComponent());
        tone.addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());
        //tone.addComponent(VolumeComponent.class, new VolumeComponent());
        tone.addComponent(ModularBayComponent.class, new ModularBayComponent());
    }

}
