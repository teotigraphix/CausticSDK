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

package com.teotigraphix.caustk.machine;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.CaustkFactory;
import com.teotigraphix.caustk.controller.command.CommandContext;
import com.teotigraphix.caustk.controller.command.CommandUtils;
import com.teotigraphix.caustk.controller.command.UndoCommand;
import com.teotigraphix.caustk.core.IRackAware;
import com.teotigraphix.caustk.core.IRackSerializer;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.rack.mixer.MasterDelay;
import com.teotigraphix.caustk.rack.mixer.MasterEqualizer;
import com.teotigraphix.caustk.rack.mixer.MasterLimiter;
import com.teotigraphix.caustk.rack.mixer.MasterReverb;
import com.teotigraphix.caustk.rack.mixer.MasterVolume;
import com.teotigraphix.caustk.utils.ExceptionUtils;

/**
 * @author Michael Schmalle
 */
public class CastkMasterMixer implements IRackSerializer, IRackAware {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private IRack rack;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private CaustkScene scene;

    @Tag(1)
    private MasterDelay delay;

    @Tag(2)
    private MasterReverb reverb;

    @Tag(3)
    private MasterEqualizer equalizer;

    @Tag(4)
    private MasterLimiter limiter;

    @Tag(5)
    private MasterVolume volume;

    public CaustkScene getScene() {
        return scene;
    }

    //--------------------------------------------------------------------------
    // IRackAware API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // rack
    //----------------------------------

    @Override
    public IRack getRack() {
        return rack;
    }

    @Override
    public void setRack(IRack value) {
        rack = value;

        equalizer.setRack(rack);
        limiter.setRack(rack);
        delay.setRack(rack);
        reverb.setRack(rack);
        volume.setRack(rack);
    }

    CastkMasterMixer() {
    }

    CastkMasterMixer(CaustkScene caustkScene) {
        this.scene = caustkScene;

        equalizer = new MasterEqualizer();
        limiter = new MasterLimiter();
        delay = new MasterDelay();
        reverb = new MasterReverb();
        volume = new MasterVolume();
    }

    @Override
    public void load(CaustkFactory factory) {
        equalizer.load(factory);
        limiter.load(factory);
        delay.load(factory);
        reverb.load(factory);
        volume.load(factory);

        restore();
    }

    @Override
    public void restore() {
        equalizer.restore();
        limiter.restore();
        delay.restore();
        reverb.restore();
        volume.restore();
    }

    @Override
    public void update() {
        equalizer.update();
        limiter.update();
        delay.update();
        reverb.update();
        volume.update();
    }

    protected final RuntimeException newRangeException(String control, String range, Object value) {
        return ExceptionUtils.newRangeException(control, range, value);
    }

    //--------------------------------------------------------------------------
    // Command API
    //--------------------------------------------------------------------------

    public static class MasterMixerSetSendCommand extends UndoCommand {

        private VO last;

        @Override
        protected void doExecute() {
            VO current = new VO(getContext());
            // save the current state into the 'last' state if this command
            // has redo() called on it

            CaustkMachine machine = getContext().getRack().getScene().getMachine(current.index);
            MixerPreset channel = machine.getMixer();

            last = new VO(channel, current);
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
            CaustkMachine machine = getContext().getRack().getScene().getMachine(index);
            MixerPreset channel = machine.getMixer();

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

            public VO(MixerPreset channel, VO current) {
                index = current.index;
                type = current.type;
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
