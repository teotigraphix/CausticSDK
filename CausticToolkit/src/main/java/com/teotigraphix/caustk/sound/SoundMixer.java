
package com.teotigraphix.caustk.sound;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerBase;
import com.teotigraphix.caustk.controller.SubControllerModel;
import com.teotigraphix.caustk.controller.command.CommandContext;
import com.teotigraphix.caustk.controller.command.CommandUtils;
import com.teotigraphix.caustk.controller.command.UndoCommand;
import com.teotigraphix.caustk.sound.ISoundSource.OnSoundSourceSongLoad;
import com.teotigraphix.caustk.sound.SoundSource.OnSoundSourceToneAdd;
import com.teotigraphix.caustk.sound.SoundSource.OnSoundSourceToneRemove;
import com.teotigraphix.caustk.tone.Tone;

public class SoundMixer extends SubControllerBase implements ISoundMixer {

    //----------------------------------
    // modelType
    //----------------------------------

    @Override
    protected Class<? extends SubControllerModel> getModelType() {
        return SoundMixerModel.class;
    }

    //----------------------------------
    // model
    //----------------------------------

    SoundMixerModel getModel() {
        return (SoundMixerModel)getInternalModel();
    }

    //--------------------------------------------------------------------------
    // ISoundMixer API
    //--------------------------------------------------------------------------
    @Override
    public MasterMixer getMasterMixer() {
        return getModel().getMasterMixer();
    }

    @Override
    public SoundMixerChannel getChannel(int index) {
        return getModel().getChannels().get(index);
    }

    @Override
    public SoundMixerChannel getChannel(Tone tone) {
        return getChannel(tone.getIndex());
    }

    @Override
    public void executeSetValue(int toneIndex, MixerInput input, Number value) {
        getController().execute(COMMAND_SET_VALUE, toneIndex, input, value);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SoundMixer(ICaustkController controller) {
        super(controller);

        controller.addComponent(ISoundMixer.class, this);

        // listen for tone add/remove
        controller.getDispatcher().register(OnSoundSourceToneAdd.class,
                new EventObserver<OnSoundSourceToneAdd>() {
                    @Override
                    public void trigger(OnSoundSourceToneAdd object) {
                        addTone(object.getTone());
                    }
                });

        controller.getDispatcher().register(OnSoundSourceToneRemove.class,
                new EventObserver<OnSoundSourceToneRemove>() {
                    @Override
                    public void trigger(OnSoundSourceToneRemove object) {
                        removeTone(object.getTone());
                    }
                });

        controller.getSoundSource().getDispatcher()
                .register(OnSoundSourceSongLoad.class, new EventObserver<OnSoundSourceSongLoad>() {
                    @Override
                    public void trigger(OnSoundSourceSongLoad object) {
                        restore();
                    }
                });

        controller.getCommandManager().put(ISoundMixer.COMMAND_SET_VALUE,
                SoundMixerSetSendCommand.class);
    }

    @Override
    public void restore() {
        // restores the already created channels from the just previous song load
        for (SoundMixerChannel channel : getModel().getChannels().values()) {
            channel.restore();
        }
    }

    @Override
    public String serialize() {
        String data = getController().getSerializeService().toString(getModel());
        return data;
    }

    protected void addTone(Tone tone) {
        getModel().toneAdded(tone);
    }

    protected void removeTone(Tone tone) {
        getModel().toneRemoved(tone);
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
