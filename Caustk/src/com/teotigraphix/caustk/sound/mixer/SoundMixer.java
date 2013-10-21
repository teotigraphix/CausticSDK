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

package com.teotigraphix.caustk.sound.mixer;

import java.io.Serializable;
import java.util.Map;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.command.CommandContext;
import com.teotigraphix.caustk.controller.command.CommandUtils;
import com.teotigraphix.caustk.controller.command.UndoCommand;
import com.teotigraphix.caustk.controller.core.Rack;
import com.teotigraphix.caustk.controller.core.RackComponent;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sound.ISoundMixer;
import com.teotigraphix.caustk.sound.source.SoundSource.OnSoundSourceToneAdd;
import com.teotigraphix.caustk.sound.source.SoundSource.OnSoundSourceToneRemove;
import com.teotigraphix.caustk.tone.Tone;

public class SoundMixer extends RackComponent implements ISoundMixer, Serializable {

    private static final long serialVersionUID = -7091528512172948750L;

    //----------------------------------
    // channels
    //----------------------------------

    Map<Integer, SoundMixerChannel> getChannels() {
        return masterMixer.getChannels();
    }

    //--------------------------------------------------------------------------
    // ISoundMixer API
    //--------------------------------------------------------------------------

    //----------------------------------
    // masterMixer
    //----------------------------------

    private MasterMixer masterMixer;

    @Override
    public MasterMixer getMasterMixer() {
        return masterMixer;
    }

    void setMasterMixer(MasterMixer value) {
        masterMixer = value;
    }

    @Override
    public boolean hasChannel(int index) {
        return getChannels().containsKey(index);
    }

    @Override
    public SoundMixerChannel getChannel(int index) {
        return getChannels().get(index);
    }

    @Override
    public SoundMixerChannel getChannel(Tone tone) {
        return getChannel(tone.getIndex());
    }

    @Override
    public void executeSetValue(int toneIndex, MixerInput input, Number value) {
        try {
            getRack().execute(COMMAND_SET_VALUE, toneIndex, input, value);
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SoundMixer(Rack rack) {
        super(rack);
        masterMixer = new MasterMixer(rack);
    }

    @Override
    public void registerObservers() {
        getLogger().err("SoundMixer", "registerObservers()");
        toneAdd = new EventObserver<OnSoundSourceToneAdd>() {
            @Override
            public void trigger(OnSoundSourceToneAdd object) {
                getLogger().err("SoundMixer", "OnSoundSourceToneAdd()");
                masterMixer.addTone(object.getTone());
            }
        };
        toneRemove = new EventObserver<OnSoundSourceToneRemove>() {
            @Override
            public void trigger(OnSoundSourceToneRemove object) {
                masterMixer.removeTone(object.getTone());
            }
        };
        getRack().register(OnSoundSourceToneAdd.class, toneAdd);
        getRack().register(OnSoundSourceToneRemove.class, toneRemove);
        getRack().put(ISoundMixer.COMMAND_SET_VALUE, SoundMixerSetSendCommand.class);
    }

    @Override
    public void unregisterObservers() {
        getRack().unregister(toneAdd);
        getRack().unregister(toneRemove);
        getRack().remove(ISoundMixer.COMMAND_SET_VALUE);
    }

    private transient EventObserver<OnSoundSourceToneAdd> toneAdd;

    private transient EventObserver<OnSoundSourceToneRemove> toneRemove;

    @Override
    public void restore() {
        // restores the already created channels from the just previous song load
        for (SoundMixerChannel channel : getChannels().values()) {
            channel.restore();
        }
    }

    public void update() {
        for (SoundMixerChannel channel : getChannels().values()) {
            channel.update();
        }
    }

    //--------------------------------------------------------------------------
    // Command API
    //--------------------------------------------------------------------------

    public static class SoundMixerSetSendCommand extends UndoCommand {

        private VO last;

        @Override
        protected void doExecute() {
            VO current = new VO(getContext());
            // save the current state into the 'last' state if this command
            // has redo() called on it
            last = new VO(getContext().getComponent(ISoundMixer.class), current);
            // execute the OSC command for this current command
            // the command is already in the command history right now
            update(current);
        }

        @Override
        protected void undoExecute() {
            update(last);
        }

        private void update(VO vo) {
            int index = vo.index;
            SoundMixerChannel channel = getContext().getComponent(ISoundMixer.class).getChannel(
                    index);

            final float value = vo.value;
            switch (vo.type) {
                case Bass:
                    channel.setBass(value);
                    break;
                case Mid:
                    channel.setMid(value);
                    break;
                case High:
                    channel.setHigh(value);
                    break;
                case DelaySend:
                    channel.setDelaySend(value);
                    break;
                case ReverbSend:
                    channel.setReverbSend(value);
                    break;
                case Pan:
                    channel.setPan(value);
                    break;
                case StereoWidth:
                    channel.setStereoWidth(value);
                    break;
                case Mute:
                    channel.setMute(value == 0f ? false : true);
                    break;
                case Solo:
                    channel.setSolo(value == 0f ? false : true);
                    break;
                case Volume:
                    channel.setVolume(value);
                    break;
            }
        }

        class VO {

            private int index;

            private MixerInput type;

            private float value;

            public VO(CommandContext context) {
                index = CommandUtils.getInteger(getContext(), 0);
                type = MixerInput.fromString(CommandUtils.getString(getContext(), 1));
                value = CommandUtils.getFloat(getContext(), 2);
            }

            public VO(ISoundMixer api, VO current) {
                index = current.index;
                type = current.type;
                SoundMixerChannel channel = api.getChannel(index);
                switch (type) {
                    case Bass:
                        value = channel.getBass();
                        break;
                    case Mid:
                        value = channel.getMid();
                        break;
                    case High:
                        value = channel.getHigh();
                        break;
                    case DelaySend:
                        value = channel.getDelaySend();
                        break;
                    case ReverbSend:
                        value = channel.getReverbSend();
                        break;
                    case Pan:
                        value = channel.getPan();
                        break;
                    case StereoWidth:
                        value = channel.getStereoWidth();
                        break;
                    case Mute:
                        value = channel.isMute() ? 1 : 0;
                        break;
                    case Solo:
                        value = channel.isSolo() ? 1 : 0;
                        break;
                    case Volume:
                        value = channel.getVolume();
                        break;
                }
            }
        }
    }

    public enum MixerInput {

        Bass("bass"),

        Mid("mid"),

        High("high"),

        DelaySend("delaySend"),

        ReverbSend("reverbSend"),

        Pan("pan"),

        StereoWidth("stereoWidth"),

        Mute("mute"),

        Solo("solo"),

        Volume("volume");

        private String value;

        public final String getValue() {
            return value;
        }

        public static MixerInput fromString(String value) {
            for (MixerInput mixerInput : values()) {
                if (mixerInput.getValue().equals(value))
                    return mixerInput;
            }
            return null;
        }

        MixerInput(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return getValue();
        }
    }
}
