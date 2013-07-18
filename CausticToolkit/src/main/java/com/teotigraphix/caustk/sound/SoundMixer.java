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

package com.teotigraphix.caustk.sound;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustic.core.IMemento;
import com.teotigraphix.caustic.effect.IEffectsRack;
import com.teotigraphix.caustic.internal.effect.EffectsRack;
import com.teotigraphix.caustic.internal.mixer.MixerPanel;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.mixer.IMixerDelay;
import com.teotigraphix.caustic.mixer.IMixerPanel;
import com.teotigraphix.caustic.mixer.IMixerReverb;
import com.teotigraphix.caustic.mixer.MixerEffectType;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.command.CommandContext;
import com.teotigraphix.caustk.controller.command.CommandUtils;
import com.teotigraphix.caustk.controller.command.UndoCommand;
import com.teotigraphix.caustk.sound.CaustkSoundSource.OnSoundSourceClear;
import com.teotigraphix.caustk.sound.CaustkSoundSource.OnSoundSourceToneAdd;
import com.teotigraphix.caustk.sound.CaustkSoundSource.OnSoundSourceToneRemove;

public class SoundMixer implements ICaustkSoundMixer {

    /**
     * sound_mixer/set_send [0] [1] [2]
     * <p>
     * param 0 - tone index
     * <p>
     * param 1 - type (reverb, delay, volume, low, mid, high)
     * <p>
     * param 2 - value
     */
    public static final String COMMAND_SET_SEND = "sound_mixer/set_send";

    private ICaustkController controller;

    private MixerPanel mixerPanel;

    private EffectsRack effectsRack;

    @Override
    public void pasteMasterChannel(IMemento memento) {
        mixerPanel.pasteMasterChannel(memento);
    }

    @Override
    public void pasteMixerChannel(IMachine machine, IMemento memento) {
        mixerPanel.pasteChannel(machine, memento);
    }

    @Override
    public void pasteEffectChannel(IMachine machine, IMemento memento) {
        effectsRack.pasteChannel(machine, memento);
    }

    @Override
    public void copyEffectChannel(IMachine machine, IMemento memento) {
        effectsRack.copyChannel(machine, memento);
    }

    @Override
    public IMixerPanel getMixerPanel() {
        return mixerPanel;
    }

    @Override
    public IEffectsRack getEffectsRack() {
        return effectsRack;
    }

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // Master
    //----------------------------------

    float getMasterVolume() {
        return mixerPanel.getMasterVolume();
    }

    void setMasterVolume(float value) {
        mixerPanel.setMasterVolume(value);
    }

    float getMasterBass() {
        return mixerPanel.getMasterBass();
    }

    void setMasterBass(float value) {
        mixerPanel.setMasterBass(value);
    }

    float getMasterMid() {
        return mixerPanel.getMasterMid();
    }

    void setMasterMid(float value) {
        mixerPanel.setMasterMid(value);
    }

    float getMasterHigh() {
        return mixerPanel.getMasterHigh();
    }

    void setMasterHigh(float value) {
        mixerPanel.setMasterHigh(value);
    }

    //----------------------------------
    // Sends
    //----------------------------------

    float getDelaySend(int index) {
        return mixerPanel.getDelaySend(index);
    }

    void setDelaySend(int index, float value) {
        mixerPanel.setDelaySend(index, value);
    }

    float getReverbSend(int index) {
        return mixerPanel.getReverbSend(index);
    }

    void setReverbSend(int index, float value) {
        mixerPanel.setReverbSend(index, value);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SoundMixer(ICaustkController controller) {
        this.controller = controller;

        createMixerPanel();
        createEffectsPanel();

        controller.getDispatcher().register(OnSoundSourceClear.class, resetObserver);
        controller.getDispatcher().register(OnSoundSourceToneAdd.class, addObserver);
        controller.getDispatcher().register(OnSoundSourceToneRemove.class, removeObserver);
    }

    private void createMixerPanel() {
        mixerPanel = (MixerPanel)controller.getFactory().createMixerPanel();
        mixerPanel.setEngine(controller);

        mixerPanel.setDelay((IMixerDelay)controller.getFactory().createMixerEffect(mixerPanel,
                MixerEffectType.DELAY));
        mixerPanel.setReverb((IMixerReverb)controller.getFactory().createMixerEffect(mixerPanel,
                MixerEffectType.REVERB));
    }

    private void createEffectsPanel() {
        effectsRack = (EffectsRack)controller.getFactory().createEffectRack();
        effectsRack.setEngine(controller);
    }

    //--------------------------------------------------------------------------
    // Public Observer Impl
    //--------------------------------------------------------------------------

    private EventObserver<OnSoundSourceClear> resetObserver = new EventObserver<OnSoundSourceClear>() {
        @Override
        public void trigger(OnSoundSourceClear object) {
        }
    };

    private EventObserver<OnSoundSourceToneAdd> addObserver = new EventObserver<OnSoundSourceToneAdd>() {
        @Override
        public void trigger(OnSoundSourceToneAdd object) {
//            IMachine machine = object.getTone().getMachine();
//            mixerPanel.addMachine(machine);
//            effectsRack.addMachine(machine);
        }
    };

    private EventObserver<OnSoundSourceToneRemove> removeObserver = new EventObserver<OnSoundSourceToneRemove>() {
        @Override
        public void trigger(OnSoundSourceToneRemove object) {
//            IMachine machine = object.getTone().getMachine();
//            mixerPanel.removeMachine(machine);
//            effectsRack.removeMachine(machine);
        }
    };

    public static class SoundMixerSetMasterCommand extends UndoCommand {

        private VO last;

        @Override
        protected void doExecute() {
            VO current = new VO(getContext());
            last = new VO(getContext().api(SoundMixerAPI.class), current);
            update(current);
        }

        @Override
        protected void undoExecute() {
            update(last);
        }

        private void update(VO vo) {
            if (vo.type.equals("volume"))
                getContext().api(SoundMixerAPI.class).setMasterVolume(vo.value);
            else if (vo.type.equals("bass"))
                getContext().api(SoundMixerAPI.class).setMasterBass(vo.value);
            else if (vo.type.equals("mid"))
                getContext().api(SoundMixerAPI.class).setMasterMid(vo.value);
            else if (vo.type.equals("high"))
                getContext().api(SoundMixerAPI.class).setMasterHigh(vo.value);
        }

        class VO {

            private String type;

            private float value;

            public VO(CommandContext context) {
                type = CommandUtils.getString(getContext(), 0);
                value = CommandUtils.getFloat(getContext(), 1);
            }

            public VO(SoundMixerAPI api, VO current) {
                type = current.type;
                if (type.equals("volume"))
                    value = api.getMasterVolume();
                else if (type.equals("bass"))
                    value = api.getMasterBass();
                else if (type.equals("mid"))
                    value = api.getMasterMid();
                else if (type.equals("high"))
                    value = api.getMasterHigh();
            }
        }
    }

    public static class SoundMixerSetSendCommand extends UndoCommand {

        private VO last;

        @Override
        protected void doExecute() {
            VO current = new VO(getContext());
            last = new VO(getContext().api(SoundMixerAPI.class), current);
            update(current);
        }

        @Override
        protected void undoExecute() {
            update(last);
        }

        private void update(VO vo) {
            if (vo.type.equals("delay"))
                getContext().api(SoundMixerAPI.class).setDelaySend(vo.index, vo.value);
            else if (vo.type.equals("reverb"))
                getContext().api(SoundMixerAPI.class).setReverbSend(vo.index, vo.value);
        }

        class VO {

            private int index;

            private String type;

            private float value;

            public VO(CommandContext context) {
                index = CommandUtils.getInteger(getContext(), 0);
                type = CommandUtils.getString(getContext(), 1);
                value = CommandUtils.getFloat(getContext(), 2);
            }

            public VO(SoundMixerAPI api, VO current) {
                index = current.index;
                type = current.type;
                if (type.equals("delay"))
                    value = api.getDelaySend(index);
                else if (type.equals("reverb"))
                    value = api.getReverbSend(index);
            }
        }
    }
}
