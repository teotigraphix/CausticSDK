
package com.teotigraphix.caustk.sound;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerChange;
import com.teotigraphix.caustk.project.IProjectManager.ProjectManagerChangeKind;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.sound.SoundSource.OnSoundSourceToneAdd;
import com.teotigraphix.caustk.sound.SoundSource.OnSoundSourceToneRemove;
import com.teotigraphix.caustk.tone.Tone;

public class SoundMixer implements ISoundMixer {

    private ICaustkController controller;

    private SoundMixerState state;

    SoundMixerState getState() {
        return state;
    }

    @Override
    public SoundMixerChannel getChannel(int index) {
        return state.getChannels().get(index);
    }

    @Override
    public SoundMixerChannel getChannel(Tone tone) {
        return getChannel(tone.getIndex());
    }

    public final ICaustkController getController() {
        return controller;
    }

    public SoundMixer(ICaustkController controller) {
        this.controller = controller;

        state = new SoundMixerState(controller);

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

        controller.getDispatcher().register(OnProjectManagerChange.class,
                new EventObserver<OnProjectManagerChange>() {
                    @Override
                    public void trigger(OnProjectManagerChange object) {
                        if (object.getKind() == ProjectManagerChangeKind.SAVE) {
                            saveState(object.getProject());
                        } else if (object.getKind() == ProjectManagerChangeKind.LOAD) {
                            loadState(object.getProject());
                        }
                    }
                });
    }

    protected void addTone(Tone tone) {
        state.toneAdded(tone);
    }

    protected void removeTone(Tone tone) {
        state.toneRemoved(tone);
    }

    protected void loadState(Project project) {
        String data = project.getString("ISoundMixer.state");
        state = getController().getSerializeService().fromString(data, SoundMixerState.class);
    }

    protected void saveState(Project project) {
        String data = getController().getSerializeService().toString(state);
        project.put("ISoundMixer.state", data);
    }
}
