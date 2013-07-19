
package com.teotigraphix.caustk.sound;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerBase;
import com.teotigraphix.caustk.controller.SubControllerModel;
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
    public SoundMixerChannel getChannel(int index) {
        return getModel().getChannels().get(index);
    }

    @Override
    public SoundMixerChannel getChannel(Tone tone) {
        return getChannel(tone.getIndex());
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SoundMixer(ICaustkController controller) {
        super(controller);

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
    }

    protected void addTone(Tone tone) {
        getModel().toneAdded(tone);
    }

    protected void removeTone(Tone tone) {
        getModel().toneRemoved(tone);
    }
}
