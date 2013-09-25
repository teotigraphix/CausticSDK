
package com.teotigraphix.caustk.controller.core;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.sound.ISoundGenerator;
import com.teotigraphix.caustk.sound.ISoundMixer;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.sound.mixer.SoundMixer;
import com.teotigraphix.caustk.sound.mixer.SoundMixerChannel;
import com.teotigraphix.caustk.sound.source.SoundSource;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.ToneType;

/**
 * The {@link Rack} is a fully serializable state instance.
 */
public class Rack implements Serializable, ICausticEngine {

    private static final long serialVersionUID = 3465185099859140754L;

    private transient ISoundGenerator soundGenerator;

    private transient ICaustkController controller;

    public ICaustkController getController() {
        return controller;
    }

    public void setController(ICaustkController controller) {
        this.controller = controller;

        updateSoundGenerator();

        soundSource.setController(controller);
        soundMixer.setController(controller);
    }

    private void updateSoundGenerator() {
        soundGenerator = controller.getApplication().getConfiguration().getSoundGenerator();
        // this does nothing on desktop and android at the moment
        // soundGenerator.initialize();
    }

    private ISoundSource soundSource;

    ISoundSource getSoundSource() {
        return soundSource;
    }

    private ISoundMixer soundMixer;

    ISoundMixer getSoundMixer() {
        return soundMixer;
    }

    public Rack() {
    }

    public Rack(ICaustkController controller) {
        this.controller = controller;
        updateSoundGenerator();
        soundSource = new SoundSource(controller);
        soundMixer = new SoundMixer(controller);
    }

    //--------------------------------------------------------------------------
    // SoundGenerator
    //--------------------------------------------------------------------------

    public final float getCurrentSongMeasure() {
        return soundGenerator.getCurrentSongMeasure();
    }

    public final float getCurrentBeat() {
        return soundGenerator.getCurrentBeat();
    }

    //--------------------------------------------------------------------------
    // SoundMixer
    //--------------------------------------------------------------------------

    public void setVolume(float value) {
        getSoundMixer().getMasterMixer().setVolume(value);
    }

    public float getVolume() {
        return getSoundMixer().getMasterMixer().getVolume();
    }

    public SoundMixerChannel getMixerChannel(int index) {
        return getSoundMixer().getChannel(index);
    }

    //--------------------------------------------------------------------------
    // Tones
    //--------------------------------------------------------------------------

    public void clearAndReset() {
        soundSource.clearAndReset();
    }

    public void loadSong(File absoluteCausticFile) throws CausticException {
        soundSource.loadSong(absoluteCausticFile);
    }

    public boolean hasTone(int index) {
        return soundSource.getTone(index) != null;
    }

    public Collection<Tone> getTones() {
        return soundSource.getTones();
    }

    public Tone getTone(int index) {
        return soundSource.getTone(index);
    }

    public Tone createTone(String name, Class<? extends Tone> toneClass) throws CausticException {
        return soundSource.createTone(name, toneClass);
    }

    public Tone createTone(String name, ToneType toneType) throws CausticException {
        return soundSource.createTone(name, toneType);
    }

    public void saveSong(String name) throws IOException {
        soundSource.saveSong(name);
    }

    public void saveSongAs(File absoluteTargetSongFile) throws IOException {
        soundSource.saveSongAs(absoluteTargetSongFile);
    }

    //--------------------------------------------------------------------------
    // IActivityCycle API
    //--------------------------------------------------------------------------

    @Override
    public void onStart() {
        soundGenerator.onStart();
    }

    @Override
    public void onResume() {
        soundGenerator.onResume();
    }

    @Override
    public void onPause() {
        soundGenerator.onPause();
    }

    @Override
    public void onStop() {
        soundGenerator.onStop();
    }

    @Override
    public void onDestroy() {
        soundGenerator.onDestroy();
    }

    @Override
    public void onRestart() {
        soundGenerator.onRestart();
    }

    @Override
    public void dispose() {
        soundGenerator.dispose();
    }

    //--------------------------------------------------------------------------
    // ICausticEngine API
    //--------------------------------------------------------------------------

    // we proxy the actual OSC impl so we can stop, or reroute
    @Override
    public final float sendMessage(String message) {
        return soundGenerator.sendMessage(message);
    }

    @Override
    public final String queryMessage(String message) {
        return soundGenerator.queryMessage(message);
    }

    public void close() {
    }

}
